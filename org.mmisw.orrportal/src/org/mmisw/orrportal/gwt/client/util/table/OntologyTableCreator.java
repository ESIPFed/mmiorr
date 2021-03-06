package org.mmisw.orrportal.gwt.client.util.table;

import org.mmisw.orrportal.gwt.client.util.table.ontab.OntologyTable;
//import org.mmisw.orrportal.gwt.client.util.table.ontab.OntologyTableGwt24;
import org.mmisw.orrportal.gwt.client.util.table.ontab.OntologyTableGwtIncubator;
import org.mmisw.orrportal.gwt.client.util.table.ontab.OntologyTableSc;


public class OntologyTableCreator {
	
	private enum Impl { ORIG, GWT24, GWT_INCUBATOR, SMARTGWT };
	
//	private static final Impl impl = Impl.GWT24;  // under preliminary testing
	private static final Impl impl = Impl.ORIG;


	public static IOntologyTable create(IQuickInfo quickInfo, boolean isVersionsTable) {
		
		switch ( impl ) {
			case GWT_INCUBATOR:
				return new OntologyTableGwtIncubator(quickInfo, isVersionsTable);
				
			case SMARTGWT:
				return new OntologyTableSc(quickInfo, isVersionsTable);
				
			case GWT24:
//				return new OntologyTableGwt24(quickInfo, isVersionsTable);
			case ORIG:
			default:
				return new OntologyTable(quickInfo, isVersionsTable);
		}
	}
	

	private OntologyTableCreator() {}
}
