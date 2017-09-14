package play.db.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NoResultException;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Query;

import play.Play;
import play.classloading.enhancers.LVEnhancer;
import play.data.binding.BeanWrapper;
import play.data.binding.Binder;
import play.data.binding.ParamNode;
import play.data.validation.Validation;
import play.exceptions.UnexpectedException;
import play.mvc.Scope.Params;

/**
 * A super class for JPA entities
 * bran: lots of the method used to return <T extends JPABase>, I changed them to <T extends GenericModel>, forgot why....
 * 
 * Was it because the impl from the JPAEnhancer used the JPQL.finds which return generic model?
 * 
 */
@MappedSuperclass
@SuppressWarnings("unchecked")
public class GenericModel extends JPABase {
	private static final long serialVersionUID = -7724080574280776444L;

    /**
     * This method is deprecated. Use this instead:
     *
     *  public static <T extends JPABase> T create(ParamNode rootParamNode, String name, Class<?> type, Annotation[] annotations)
     */
    @Deprecated
    public static <T extends JPABase> T create(Class<?> type, String name, Map<String, String[]> params, Annotation[] annotations) {
        ParamNode rootParamNode = ParamNode.convert(params);
        return (T)create(rootParamNode, name, type, annotations);
    }

    public static <T extends JPABase> T create(ParamNode rootParamNode, String name, Class<?> type, Annotation[] annotations) {
        try {
            Constructor<?> c = type.getDeclaredConstructor();
            c.setAccessible(true);
            Object model = c.newInstance();
            return (T) edit(rootParamNode, name, model, annotations);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is deprecated. Use this instead:
     *
     *  public static <T extends JPABase> T edit(ParamNode rootParamNode, String name, Object o, Annotation[] annotations)
     *
     * @return
     */
    @Deprecated
    public static <T extends JPABase> T edit(Object o, String name, Map<String, String[]> params, Annotation[] annotations) {
        ParamNode rootParamNode = ParamNode.convert(params);
        return (T)edit( rootParamNode, name, o, annotations);
    }

    @SuppressWarnings("deprecation")
    public static <T extends JPABase> T edit(ParamNode rootParamNode, String name, Object o, Annotation[] annotations) {
        ParamNode paramNode = rootParamNode.getChild(name, true);
        // #1195 - Needs to keep track of whick keys we remove so that we can restore it before
        // returning from this method.
        List<ParamNode.RemovedNode> removedNodesList = new ArrayList<ParamNode.RemovedNode>();
        try {
            BeanWrapper bw = BeanWrapper.forClass(o.getClass());
            // Start with relations
            Set<Field> fields = new HashSet<Field>();
            Class<?> clazz = o.getClass();
            while (!clazz.equals(Object.class)) {
                Collections.addAll(fields, clazz.getDeclaredFields());
                clazz = clazz.getSuperclass();
            }
            for (Field field : fields) {
                boolean isEntity = false;
                String relation = null;
                boolean multiple = false;
                //
                if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class)) {
                    isEntity = true;
                    relation = field.getType().getName();
                }
                if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class)) {
                    Class<?> fieldType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    isEntity = true;
                    relation = fieldType.getName();
                    multiple = true;
                }

                if (isEntity) {

                    ParamNode fieldParamNode = paramNode.getChild(field.getName(), true);

                    Class<Model> c = (Class<Model>) Play.classloader.loadClass(relation);
                    if (JPABase.class.isAssignableFrom(c)) {
                        String keyName = Model.Manager.factoryFor(c).keyName();
                        EntityManager em = JPABase.getJPAConfig(c).getJPAContext().em();
                        if (multiple && Collection.class.isAssignableFrom(field.getType())) {
                            Collection<Object> l = new ArrayList<Object>();
                            if (SortedSet.class.isAssignableFrom(field.getType())) {
                                l = new TreeSet<Object>();
                            } else if (Set.class.isAssignableFrom(field.getType())) {
                                l = new HashSet<Object>();
                            }
                            String[] ids = fieldParamNode.getChild(keyName, true).getValues();
                            if (ids != null) {
                                // Remove it to prevent us from finding it again later
                                fieldParamNode.removeChild(keyName, removedNodesList);
                                for (String _id : ids) {
                                    if (_id.equals("")) {
                                        continue;
                                    }
                                    Query q = em.createQuery("from " + relation + " where " + keyName + " = ?");
                                    q.setParameter(1, Binder.directBind(rootParamNode.getOriginalKey(), annotations,_id, Model.Manager.factoryFor((Class<Model>) Play.classloader.loadClass(relation)).keyType(), null));
                                    try {
                                        l.add(q.getSingleResult());

                                    } catch (NoResultException e) {
                                        Validation.addError(name + "." + field.getName(), "validation.notFound", _id);
                                    }
                                }
                                bw.set(field.getName(), o, l);
                            }
                        } else {
                            String[] ids = fieldParamNode.getChild(keyName, true).getValues();
                            if (ids != null && ids.length > 0 && !ids[0].equals("")) {

                                Query q = em.createQuery("from " + relation + " where " + keyName + " = ?");
                                q.setParameter(1, Binder.directBind(rootParamNode.getOriginalKey(), annotations, ids[0], Model.Manager.factoryFor((Class<Model>) Play.classloader.loadClass(relation)).keyType(), null));
                                try {
                                    Object to = q.getSingleResult();
                                    edit(paramNode, field.getName(), to, field.getAnnotations());
                                    // Remove it to prevent us from finding it again later
                                    paramNode.removeChild( field.getName(), removedNodesList);
                                    bw.set(field.getName(), o, to);
                                } catch (NoResultException e) {
                                    Validation.addError(fieldParamNode.getOriginalKey(), "validation.notFound", ids[0]);
                                    // Remove only the key to prevent us from finding it again later
                                    // This how the old impl does it..
                                    fieldParamNode.removeChild(keyName, removedNodesList);
                                    if (fieldParamNode.getAllChildren().size()==0) {
                                        // remove the whole node..
                                        paramNode.removeChild( field.getName(), removedNodesList);
                                    }

                                }

                            } else if (ids != null && ids.length > 0 && ids[0].equals("")) {
                                bw.set(field.getName(), o, null);
                                // Remove the key to prevent us from finding it again later
                                fieldParamNode.removeChild(keyName, removedNodesList);
                            }
                        }
                    }
                }
            }
            ParamNode beanNode = rootParamNode.getChild(name, true);
            Binder.bindBean(beanNode, o, annotations);
            return (T) o;
        } catch (Exception e) {
            throw new UnexpectedException(e);
        } finally {
            // restoring changes to paramNode
            ParamNode.restoreRemovedChildren( removedNodesList );
        }
    }

    /**
     * This method is deprecated. Use this instead:
     *
     *  public <T extends GenericModel> T edit(ParamNode rootParamNode, String name)
     */
    @Deprecated
    public <T extends GenericModel> T edit(String name, Map<String, String[]> params) {
        ParamNode rootParamNode = ParamNode.convert(params);
        return (T)edit(rootParamNode, name, this, null);
    }

    public <T extends GenericModel> T edit(ParamNode rootParamNode, String name) {
        edit(rootParamNode, name, this, null);
        return (T) this;
    }

    public boolean validateAndSave() {
        if (Validation.valid(LVEnhancer.LVEnhancerRuntime.getParamNames().subject, this).ok) {
            save();
            return true;
        }
        return false;
    }

    public boolean validateAndCreate() {
		if (Validation.valid(this.getClass().getName(), this).ok) {
            return create();
        }
        return false;
    }

    /**
     * store (ie insert) the entity.
     */
    public <T extends JPABase> T save() {
        _save();
        return (T) this;
    }

    /**
     * store (ie insert) the entity.
     */
    public boolean create() {
        if (!em().contains(this)) {
            _save();
            return true;
        }
        return false;
    }

    /**
     * Refresh the entity state.
     */
    public <T extends GenericModel> T refresh() {
        em().refresh(this);
        return (T) this;
    }

    /**
     * Merge this object to obtain a managed entity (useful when the object comes from the Cache).
     * <em>Note</em>: must use the returned object to invoke any further methods, such as save(). The original object remain detached. 
     */
    public <T extends GenericModel> T merge() {
        return (T) em().merge(this);
    }

    /**
     * Delete the entity.
     * @return The deleted entity.
     */
    public <T extends GenericModel> T delete() {
        _delete();
        return (T) this;
    }

    public static <T extends GenericModel> T create(String name, Params params) {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Count entities
     * @return number of entities of this class
     */
    public static long count() {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Count entities with a special query.
     * Example : Long moderatedPosts = Post.count("moderated", true);
     * @param query HQL query or shortcut
     * @param params Params to bind to the query
     * @return A long
     */
    public static long count(String query, Object... params) {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Find all entities of this type
     */
    public static <T extends GenericModel> List<T> findAll() {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Find the entity with the corresponding id.
     * @param id The entity id
     * @return The entity
     */
    public static <T extends GenericModel> T findById(Object id) {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * get the first element from the query result set
     * 
     * bran: I added it back since the enhance would add this anyway
     * @param entity
     * @param query
     * @param params
     * @return
     */
    public static <T extends GenericModel> T findOneBy(String query, Object[] params) {
    	throw new UnsupportedOperationException("Model not enhanced. Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Prepare a query to find entities.
     * @param query HQL query or shortcut
     * @param params Params to bind to the query
     * @return A JPAQuery
     */
    public static JPAQuery find(String query, Object... params) {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Prepare a query to find *all* entities.
     * @return A JPAQuery
     */
    public static JPAQuery all() {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Batch delete of entities
     * @param query HQL query or shortcut
     * @param params Params to bind to the query
     * @return Number of entities deleted
     */
    public static int delete(String query, Object... params) {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * Delete all entities
     * @return Number of entities deleted
     */
    public static int deleteAll() {
        throw new UnsupportedOperationException("Please annotate your JPA model with @javax.persistence.Entity annotation.");
    }

    /**
     * A JPAQuery
     */
    public static class JPAQuery {

        public Query query;
        public String sq;

        public JPAQuery(String sq, Query query) {
            this.query = query;
            this.sq = sq;
        }

        public JPAQuery(Query query) {
            this.query = query;
            this.sq = query.toString();
        }

        public <T> T first() {
            try {
                List<T> results = query.setMaxResults(1).getResultList();
                if (results.isEmpty()) {
                    return null;
                }
                return results.get(0);
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

        /**
         * Bind a JPQL named parameter to the current query.
         */
        public JPAQuery bind(String name, Object param) {
            if (param.getClass().isArray()) {
                param = Arrays.asList((Object[]) param);
            }
            if (param instanceof Integer) {
                param = ((Integer) param).longValue();
            }
            query.setParameter(name, param);
            return this;
        }

        /**
         * Retrieve all results of the query
         * @return A list of entities
         */
        public <T> List<T> fetch() {
            try {
                return query.getResultList();
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

        /**
         * Retrieve results of the query
         * @param max Max results to fetch
         * @return A list of entities
         */
        public <T> List<T> fetch(int max) {
            try {
                query.setMaxResults(max);
                return query.getResultList();
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }

        /**
         * Set the position to start
         * @param position Position of the first element
         * @return A new query
         */
        public <T> JPAQuery from(int position) {
            query.setFirstResult(position);
            return this;
        }

        /**
         * Retrieve a page of result
         * @param page Page number (start at 1)
         * @param length (page length)
         * @return a list of entities
         */
        public <T> List<T> fetch(int page, int length) {
            if (page < 1) {
                page = 1;
            }
            query.setFirstResult((page - 1) * length);
            query.setMaxResults(length);
            try {
                return query.getResultList();
            } catch (Exception e) {
                throw new JPAQueryException("Error while executing query <strong>" + sq + "</strong>", JPAQueryException.findBestCause(e));
            }
        }
    }

    // ----- THIS CODE IS DEPRECATED AND WILL BE REMOVED IN NEXT VERSIONs
    @PostLoad
    @SuppressWarnings("deprecation")
    public void _setupAttachment() {
        Class<?> c = this.getClass();
        while (!c.equals(Object.class)) {
            for (Field field : c.getDeclaredFields()) {
                if (FileAttachment.class.isAssignableFrom(field.getType())) {
                    try {
                        field.setAccessible(true);
                        FileAttachment attachment = (FileAttachment) field.get(this);
                        if (attachment != null) {
                            attachment.model = this;
                            attachment.name = field.getName();
                        } else {
                            attachment = new FileAttachment();
                            attachment.model = this;
                            attachment.name = field.getName();
                            field.set(this, attachment);
                        }
                    } catch (Exception ex) {
                        throw new UnexpectedException(ex);
                    }
                }
            }
            c = c.getSuperclass();
        }
    }

    @PostPersist
    @PostUpdate
    @SuppressWarnings("deprecation")
    public void _saveAttachment() {
        Class<?> c = this.getClass();
        while (!c.equals(Object.class)) {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType().equals(FileAttachment.class)) {
                    try {
                        field.setAccessible(true);
                        FileAttachment attachment = (FileAttachment) field.get(this);
                        if (attachment != null) {
                            attachment.model = this;
                            attachment.name = field.getName();
                            attachment.save();
                        }
                    } catch (Exception ex) {
                        throw new UnexpectedException(ex);
                    }
                }
            }
            c = c.getSuperclass();
        }
    }
    // -----
}