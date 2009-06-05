package org.mmisw.ontmd.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mmisw.iserver.gwt.client.rpc.DataCreationInfo;
import org.mmisw.iserver.gwt.client.rpc.IndividualInfo;
import org.mmisw.iserver.gwt.client.rpc.MappingOntologyData;
import org.mmisw.iserver.gwt.client.rpc.OntologyData;
import org.mmisw.iserver.gwt.client.rpc.OntologyInfo;
import org.mmisw.iserver.gwt.client.rpc.OtherOntologyData;
import org.mmisw.iserver.gwt.client.rpc.PropValue;
import org.mmisw.iserver.gwt.client.rpc.VocabularyOntologyData;
import org.mmisw.iserver.gwt.client.rpc.VocabularyOntologyData.ClassData;
import org.mmisw.ontmd.gwt.client.portal.BaseOntologyContentsPanel;
import org.mmisw.ontmd.gwt.client.portal.IVocabPanel;
import org.mmisw.ontmd.gwt.client.portal.OtherOntologyContentsPanel;
import org.mmisw.ontmd.gwt.client.portal.TempOntologyInfoListener;
import org.mmisw.ontmd.gwt.client.util.IRow;
import org.mmisw.ontmd.gwt.client.voc2rdf.VocabClassPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Main panel for viewing/editing data.
 * 
 * @author Carlos Rueda
 */
public class DataPanel extends VerticalPanel {

//	private static final char SEPARATOR = '|';
//	private static final char QUOTECHAR = '"';
	

	// created during refactoring process -- may be removed later
	private class MyVocabPanel implements IVocabPanel {

		public void enable(boolean enabled) {
			// TODO Auto-generated method stub
			
		}

		public void statusPanelsetHtml(String str) {
			// TODO Auto-generated method stub
			
		}

		public void statusPanelsetWaiting(boolean waiting) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private MyVocabPanel myVocabPanel = new  MyVocabPanel();
	
	
	private TempOntologyInfoListener tempOntologyInfoListener;
	
	private boolean readOnly = true;
	
	private Set<BaseOntologyContentsPanel> baseOntologyContentsPanels = new HashSet<BaseOntologyContentsPanel>();
	
	/**
	 * Creates the data panel
	 */
	public DataPanel(boolean readOnly, TempOntologyInfoListener tempOntologyInfoListener) {
		super();
		this.tempOntologyInfoListener = tempOntologyInfoListener;
		this.readOnly = readOnly;
		setWidth("100%");
	}
	
	public void enable(boolean enabled) {
		// TODO
	}
	
	

	/**
	 * Updates this panel with the data associated to the given ontology 
	 * @param ontologyInfoPre
	 */
	public void updateWith(OntologyInfo ontologyInfo, boolean readOnly) {
		this.readOnly = readOnly;
		this.clear();
		
		OntologyData ontologyData = ontologyInfo.getOntologyData();
		
		String type;
		Widget widget;
		
		if ( ontologyData instanceof VocabularyOntologyData ) {
			type = "Vocabulary contents:";
			widget = _createVocabularyWidget((VocabularyOntologyData) ontologyData);
		}
		else if ( ontologyData instanceof MappingOntologyData ) {
			type = "Mapping contents:";
			widget = _createMappingWidget((MappingOntologyData) ontologyData);
		}
		else {
			type = "Synopsis of ontology contents:";
			widget = _createOtherWidget((OtherOntologyData) ontologyData);
		}
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(new Label(type));
		vp.add(widget);
		
		add(vp);
	}
	
	
	public void setReadOnly(boolean readOnly) {
		if ( this.readOnly == readOnly ) {
			return;
		}
		this.readOnly = readOnly;
		
		for ( BaseOntologyContentsPanel baseOntologyContentsPanel : baseOntologyContentsPanels ) {
			baseOntologyContentsPanel.setReadOnly(readOnly);
		}
	}
	

	public String checkData() {
		for ( BaseOntologyContentsPanel baseOntologyContentsPanel : baseOntologyContentsPanels ) {
			String error = baseOntologyContentsPanel.checkData();
			if ( error != null ) {
				return error;
			}
		}
		
		return null;
	}


	private Widget _createVocabularyWidget(VocabularyOntologyData ontologyData) {

		Main.log("Creating VocabularyWidget");

		List<ClassData> classes = ontologyData.getClasses();
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(4);
		
		if ( classes == null || classes.size() == 0 ) {
			// empty data - we must be creating a new vocabulary
			// insert a default contents to initialize the table
			ClassData classData = new ClassData();
			
//			classData.setClassUri("");
			
//			List<String> classHeader = new ArrayList<String>();
//			classHeader.add("name");
//			classHeader.add("definition");
//			
//			List<IRow> rows = new ArrayList<IRow>();
//			rows.add(new IRow() {
//				public String getColValue(String sortColumn) {
//					return "";
//				}
//			});

			VocabClassPanel classPanel = new VocabClassPanel(classData, myVocabPanel, readOnly);
			baseOntologyContentsPanels.add(classPanel);

//			classPanel.importContents(classHeader, rows);
			
			vp.add(classPanel.getWidget());
			
			return vp;
		}
		
		for ( ClassData classData : classes ) {
			List<String> classHeader = classData.getDatatypeProperties();

			
//			String[] colNames = classHeader.toArray(new String[classHeader.size()]);
			
//			StringBuilder termContents = new StringBuilder();
//			
//			// header line in contents:
//			String _sep = "";
//			for (int i = 0; i < colNames.length; i++) {
//				termContents.append(_sep + QUOTECHAR +colNames[i]+ QUOTECHAR);
//				_sep = String.valueOf(SEPARATOR);
//			}
//			termContents.append("\n");
			
			VocabClassPanel classPanel = new VocabClassPanel(classData, myVocabPanel, readOnly);
			baseOntologyContentsPanels.add(classPanel);
			
//			ViewTable viewTable = new ViewTable(colNames);
//			tp.add(viewTable.getWidget());
			

			List<IndividualInfo> individuals = classData.getIndividuals();
			Main.log("num individuals: " +individuals.size());
			
			List<IRow> rows = new ArrayList<IRow>();
			
			for ( IndividualInfo entity : individuals ) {
				
				final Map<String, String> vals = new HashMap<String, String>();
				List<PropValue> props = entity.getProps();
				for ( PropValue pv : props ) {
					vals.put(pv.getPropName(), pv.getValueName());
				}

				vals.put("Name", entity.getLocalName());
				
//				_sep = "";
//				for (int i = 0; i < colNames.length; i++) {
//					String val = vals.get(colNames[i]);
//					if ( val == null ) {
//						val = "";
//					}
//					termContents.append(_sep + QUOTECHAR +val+ QUOTECHAR);
//					_sep = String.valueOf(SEPARATOR);
//				}
//				
//				termContents.append("\n");
				
				
				rows.add(new IRow() {
					public String getColValue(String sortColumn) {
						return vals.get(sortColumn);
					}
				});

			}
			
			classPanel.importContents(classHeader, rows);
//			classPanel.importContents(SEPARATOR, termContents.toString());
			
			vp.add(classPanel.getWidget());
			
		}
		
		return vp;
	}

	
	private Widget _createOtherWidget(OtherOntologyData ontologyData) {
		
		Main.log("Creating OtherWidget");
		
		OtherOntologyContentsPanel otherOntologyContentsPanel = new OtherOntologyContentsPanel(
				ontologyData, tempOntologyInfoListener, readOnly);

		baseOntologyContentsPanels.add(otherOntologyContentsPanel);
		
		return otherOntologyContentsPanel.getWidget();
	}
	
	private Widget _createMappingWidget(MappingOntologyData ontologyData) {
		Main.log("Creating MappingWidget");

		return new HTML("<i>not implemented yet</i>");
	}

	public void cancel() {
		for ( BaseOntologyContentsPanel baseOntologyContentsPanel : baseOntologyContentsPanels ) {
			baseOntologyContentsPanel.cancel();
		}
	}

	public DataCreationInfo getCreateOntologyInfo() {
		
		if ( baseOntologyContentsPanels.size() == 0 ) {
			return null;
		}
		BaseOntologyContentsPanel baseOntologyContentsPanel = baseOntologyContentsPanels.iterator().next();

		if ( baseOntologyContentsPanel instanceof VocabClassPanel ) {
			VocabClassPanel vocabClassPanel = (VocabClassPanel) baseOntologyContentsPanel;
			
			return vocabClassPanel.getCreateOntologyInfo();
		}
		else if ( baseOntologyContentsPanel instanceof OtherOntologyContentsPanel ) {
			OtherOntologyContentsPanel otherOntologyContentsPanel = (OtherOntologyContentsPanel) baseOntologyContentsPanel;
			
			return otherOntologyContentsPanel.getCreateOntologyInfo();
		}
		
		return null;
	}

}
