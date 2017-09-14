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
// NOTE: This file was generated from: japidviews/more/Perf/perfmain.html
// Change to this file will be lost next time the template file is compiled.
//
@cn.bran.play.NoEnhance
public abstract class perfmain extends cn.bran.play.JapidTemplateBase
{
	public static final String sourceTemplate = "japidviews/more/Perf/perfmain.html";
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


	public perfmain() {
		super(null);
	}
	public perfmain(StringBuilder out) {
		super(out);
	}
	private DataModel.User loggedInUser; // line 1
	 public void layout(DataModel.User loggedInUser) {
		this.loggedInUser = loggedInUser;
		beginDoLayout(sourceTemplate);		;// line 1
		p("\n" + 
"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" + 
"        \"http://www.w3.org/TR/html4/loose.dtd\">\n" + 
"<html>\n" + 
"<head>\n" + 
"    <title>");// line 1
		title();p("</title>\n" + 
"</head>\n" + 
"<body>\n" + 
"\n");// line 7
		if (loggedInUser != null) {// line 11
		p("	<div>\n" + 
"	    Hello ");// line 11
		p(loggedInUser.getUserName());// line 13
		p(", You have ");// line 13
		p(loggedInUser.getFriends().size());// line 13
		p(" friends\n" + 
"	</div>\n");// line 13
		}// line 15
		p("\n" + 
"<h1>Entries</h1>\n" + 
"    ");// line 15
		doLayout();// line 18
		p("</body>\n" + 
"</html>\n");// line 18
				endDoLayout(sourceTemplate);	}
	 protected void title() {};

	protected abstract void doLayout();
}