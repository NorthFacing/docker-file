package japidviews.more.Portlets;
import java.util.*;
import java.io.*;
import cn.bran.japid.tags.Each;
import cn.bran.japid.template.ActionRunner;
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
// NOTE: This file was generated from: japidviews/more/Portlets/index.html
// Change to this file will be lost next time the template file is compiled.
//
public class index extends cn.bran.play.JapidTemplateBase
{
	public static final String sourceTemplate = "japidviews/more/Portlets/index.html";
	{
		putHeader("Content-Type", "text/html; charset=utf-8");
		setContentType("text/html; charset=utf-8");
		setTraceFile(true);
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


	public index() {
		super(null);
	}
	public index(StringBuilder out) {
		super(out);
	}
/* based on https://github.com/branaway/Japid/issues/12
 */
	public static final String[] argNames = new String[] {/* args of the template*/"a", "b",  };
	public static final String[] argTypes = new String[] {/* arg types of the template*/"String", "String",  };
	public static final Object[] argDefaults= new Object[] {null,null, };
	public static java.lang.reflect.Method renderMethod = getRenderMethod(japidviews.more.Portlets.index.class);

	{
		setRenderMethod(renderMethod);
		setArgNames(argNames);
		setArgTypes(argTypes);
		setArgDefaults(argDefaults);
		setSourceTemplate(sourceTemplate);
	}
////// end of named args stuff

	private String a; // line 1
	private String b; // line 1
	public cn.bran.japid.template.RenderResult render(String a,String b) {
		this.a = a;
		this.b = b;
		long t = -1;
		try {super.layout();} catch (RuntimeException e) { super.handleException(e);} // line 1
		return new cn.bran.japid.template.RenderResultPartial(getHeaders(), getOut(), t, actionRunners, sourceTemplate);
	}
	@Override protected void doLayout() {
		beginDoLayout(sourceTemplate);
//------
;// line 1
		p("\n");// line 1
		p("<h1>To demonstrate various ways to composing complex pages with cached <b>invoke</b></h1>\n" + 
"<h3>Each parts render themselves with own cache control</h3>\n" + 
"\n" + 
"<p>The outer most content is cached for 20 seconds, using the CacheFor annotation. <em>");// line 2
		p(new Date());// line 6
		p("</em></p>\n" + 
"\n" + 
"<p>this part is never cached: \n");// line 6
				actionRunners.put(getOut().length(), new cn.bran.play.CacheablePlayActionRunner("", controllers.more.Portlets.class, "panel1", "\"never cached\"") {
			@Override
			public void runPlayAction() throws cn.bran.play.JapidResult {
				controllers.more.Portlets.panel1("\"never cached\""); // line 9
			}
		}); p("\n");// line 9
		p(" \n" + 
"</p>\n" + 
"\n" + 
"<p>this part is cached for 10 seconds. Note the timeout spec with invoke overrides CacheFor annotation. \n");// line 9
				actionRunners.put(getOut().length(), new cn.bran.play.CacheablePlayActionRunner("10s", controllers.more.Portlets.class, "panel2", b) {
			@Override
			public void runPlayAction() throws cn.bran.play.JapidResult {
				controllers.more.Portlets.panel2(b); // line 13
			}
		}); p("\n");// line 13
		p("</p>\n" + 
"\n" + 
"<div>\n" + 
" <a href=\"evict2\">Let's evict the panel2 cache!</a>\n" + 
"</div>\n" + 
"\n" + 
"<div>\n" + 
"    <p>this part is cached for 4 seconds, \n" + 
"    specified with CacheFor annotation in the controller. \n" + 
"    ");// line 13
				actionRunners.put(getOut().length(), new cn.bran.play.CacheablePlayActionRunner("", controllers.more.Portlets.class, "panel3", a + b) {
			@Override
			public void runPlayAction() throws cn.bran.play.JapidResult {
				controllers.more.Portlets.panel3(a + b); // line 23
			}
		}); p("\n");// line 23
		p("    </p>\n" + 
"</div>\n" + 
"<div>\n" + 
" <a href=\"evict3\">Let's evict the panel3 cache!</a>\n" + 
"</div>\n");// line 23
		System.out.println("japidviews/more/Portlets/index.html(line 29): " + "-- is this cool?");
		
		endDoLayout(sourceTemplate);
	}

}