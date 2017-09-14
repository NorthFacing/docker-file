package japidviews.more.Perf;
import java.util.*;
import java.io.*;
import cn.bran.japid.tags.Each;
import static play.templates.JavaExtensions.*;
import static cn.bran.play.JapidPlayAdapter.*;
import static play.data.validation.Validation.*;
import japidviews._layouts.*;
import play.i18n.Messages;
import play.data.validation.Validation;
import play.mvc.Scope.*;
import models.*;
import play.data.validation.Error;
import play.i18n.Lang;
import japidviews._tags.*;
import play.mvc.Http.*;
import controllers.*;
//
// NOTE: This file was generated from: japidviews/more/Perf/perf.html
// Change to this file will be lost next time the template file is compiled.
//
@cn.bran.play.NoEnhance
public class perf extends perfmain
{
	public static final String sourceTemplate = "japidviews/more/Perf/perf.html";
	{
		putHeader("Content-Type", "text/html; charset=utf-8");
		setContentType("text/html; charset=utf-8");
	}

// - add implicit fields with Play

	final play.mvc.Http.Request request = play.mvc.Http.Request.current(); 
	final play.mvc.Http.Response response = play.mvc.Http.Response.current(); 
	final play.mvc.Scope.Session session = play.mvc.Scope.Session.current();
	final play.mvc.Scope.RenderArgs renderArgs = play.mvc.Scope.RenderArgs.current();
	final play.mvc.Scope.Params params = play.mvc.Scope.Params.current();
	final play.data.validation.Validation validation = play.data.validation.Validation.current();
	final cn.bran.play.FieldErrors errors = new cn.bran.play.FieldErrors(validation);
	final play.Play _play = new play.Play(); 

// - end of implicit fields with Play 


	public perf() {
		super(null);
	}
	public perf(StringBuilder out) {
		super(out);
	}
/* based on https://github.com/branaway/Japid/issues/12
 */
	public static final String[] argNames = new String[] {/* args of the template*/"title", "user", "entries",  };
	public static final String[] argTypes = new String[] {/* arg types of the template*/"String", "DataModel.User", "ArrayList<DataModel.Entry>",  };
	public static final Object[] argDefaults= new Object[] {null,null,null, };
	public static java.lang.reflect.Method renderMethod = getRenderMethod(japidviews.more.Perf.perf.class);

	{
		setRenderMethod(renderMethod);
		setArgNames(argNames);
		setArgTypes(argTypes);
		setArgDefaults(argDefaults);
		setSourceTemplate(sourceTemplate);
	}
////// end of named args stuff

	private String title; // line 1
	private DataModel.User user; // line 1
	private ArrayList<DataModel.Entry> entries; // line 1
	public cn.bran.japid.template.RenderResult render(String title,DataModel.User user,ArrayList<DataModel.Entry> entries) {
		this.title = title;
		this.user = user;
		this.entries = entries;
		long t = -1;
		try {super.layout(user);} catch (RuntimeException e) { super.handleException(e);} // line 1
		return new cn.bran.japid.template.RenderResultPartial(getHeaders(), getOut(), t, actionRunners, sourceTemplate);
	}
	@Override protected void doLayout() {
		beginDoLayout(sourceTemplate);
//------
;// line 1

if (entries.size() > 0 ) {// line 11
	for (DataModel.Entry entry : entries) {// line 12
		p("		<div id=\"entries\">\n" + 
"		    Entry Id: ");// line 12
		p(entry.getEntryId());// line 14
		p(", Date: ");// line 14
		p(entry.getEntryDate());// line 14
		p("\n" + 
"		    <h2>");// line 14
		p(entry.getTitle());// line 15
		p("</h2>\n" + 
"		    <div>Submitted By: ");// line 15
		p(entry.getOwner().getUserName());// line 16
		p(" - ");// line 16
		p(entry.getOwner().getEmail());// line 16
		p("</div>\n" + 
"		    <div>");// line 16
		p(entry.getBody());// line 17
		p("</div>\n" + 
"		    ");// line 17
		if(entry.getComments().size() > 0) {// line 18
                for (DataModel.Comment comment: entry.getComments()) {// line 19
		p("				    <div>\n" + 
"				        <div>Comment by: ");// line 19
		p(comment.getOwner().getUserName());// line 21
		p("</div>\n" + 
"				        <div>");// line 21
		p(comment.getCommentText());// line 22
		p("</div>\n" + 
"				    </div>\n" + 
"                ");// line 22
		}// line 24
            } else {// line 25
		p("		      <div>No comments yet</div>\n" + 
"		    ");// line 25
		}// line 27
		p("		</div>\n" + 
"	");// line 27
		}// line 29
} else {// line 30
		p("	<div>\n" + 
"	    <div>No Entries Found</div>\n" + 
"	</div>\n");// line 30
		}// line 34
		
		endDoLayout(sourceTemplate);
	}

	@Override protected void title() {
		p(title);;
	}
}