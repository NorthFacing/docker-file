package japidviews.cn.bran.japid.template.FooControllerBare;
import java.util.*;
import java.io.*;
import cn.bran.japid.tags.Each;
import japidviews._layouts.*;
import japidviews._tags.*;
//
// NOTE: This file was generated from: japidviews/cn/bran/japid/template/FooControllerBare/a1.html
// Change to this file will be lost next time the template file is compiled.
//
public class a1 extends cn.bran.japid.template.JapidTemplateBaseWithoutPlay
{
	public static final String sourceTemplate = "japidviews/cn/bran/japid/template/FooControllerBare/a1.html";
	public a1() {
		super(null);
	}
	public a1(StringBuilder out) {
		super(out);
	}
/* based on https://github.com/branaway/Japid/issues/12
 */
	public static final String[] argNames = new String[] {/* args of the template*/"a",  };
	public static final String[] argTypes = new String[] {/* arg types of the template*/"String",  };
	public static final Object[] argDefaults= new Object[] {null, };
	public static java.lang.reflect.Method renderMethod = getRenderMethod(japidviews.cn.bran.japid.template.FooControllerBare.a1.class);

	{
		setRenderMethod(renderMethod);
		setArgNames(argNames);
		setArgTypes(argTypes);
		setArgDefaults(argDefaults);
		setSourceTemplate(sourceTemplate);
	}
////// end of named args stuff

	private String a; // line 1
	public String render(String a) {
		this.a = a;
		long __t = -1;
		try {super.layout();} catch (RuntimeException e) { super.handleException(e);} // line 1
		 if (__t != -1) System.out.println("[a1] rendering time: " + __t);
		return getOut().toString();
	}

	public static String apply(String a) {
		return new a1().render(a);
	}

	@Override protected void doLayout() {
		beginDoLayout(sourceTemplate);
//------
;// line 1
		p(">");// line 1
		p(a);// line 2
		;// line 2
		
		endDoLayout(sourceTemplate);
	}

}