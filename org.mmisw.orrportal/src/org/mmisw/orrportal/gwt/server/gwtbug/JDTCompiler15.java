package org.mmisw.orrportal.gwt.server.gwtbug;

import org.apache.tools.ant.taskdefs.Javac;

import org.eclipse.jdt.core.JDTCompilerAdapter;

/**
 * See <a href="http://code.google.com/p/google-web-toolkit/issues/detail?id=3557"
 * >this GWT bug</a>
 * 
 * <p>
 * This needs to be compiled/used against GWT 2.0.4 (well, in any case, post 1.5.2)
 */
public class JDTCompiler15 extends JDTCompilerAdapter {
	@Override
	public void setJavac(Javac attributes) {
		if (attributes.getTarget() == null) {
			attributes.setTarget("1.5");
		}
		if (attributes.getSource() == null) {
			attributes.setSource("1.5");
		}
		super.setJavac(attributes);
	}
}
