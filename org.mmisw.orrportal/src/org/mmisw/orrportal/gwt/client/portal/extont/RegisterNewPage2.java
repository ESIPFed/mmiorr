package org.mmisw.orrportal.gwt.client.portal.extont;

import org.mmisw.orrclient.gwt.client.rpc.HostingType;
import org.mmisw.orrportal.gwt.client.portal.PortalConsts;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * In this page the user indicate the type of hosting.
 * 
 * @author Carlos Rueda
 */
class RegisterNewPage2 extends BasePage {
	
	private VerticalPanel contents = new VerticalPanel();
	
	private class RButton extends RadioButton {
		RButton(final HostingType hostingType) {
			super("ht", hostingType.getLabel());
			addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					getWizard().hostingTypeSelected(hostingType);
					nextButton.setEnabled(true);
				}
			});
		}
	}
	private RadioButton[] rbs;
	

	/**
	 * Creates the ontology panel where the initial ontology can be loaded
	 * and its original contents displayed.
	 * 
	 * @param allowLoadOptions
	 */
	RegisterNewPage2(RegisterNewWizard wizard) {
		super(wizard, true, true);
		contents.setSize("650px", "200px");
		addContents(contents);
		
		rbs = new RadioButton[HostingType.values().length];
		int i = 0;
		for ( HostingType hostingType: HostingType.values() ) {
			
			if ( hostingType == HostingType.INDEXED ) {
				// Issue 212: remove "only index" option for registering external ontology
				continue;
			}
			
			rbs[i++] = new RButton(hostingType);
		}
		nextButton.setEnabled(false);
		recreate();
	}
	
	
	private void recreate() {
		contents.clear();
		
		FlexTable panel = new FlexTable();
		panel.setWidth("100%");
		int row = 0;
		
		String info = "<br/>Specify the desired type of registration"; 
		
		panel.setWidget(row, 0, new HTML(info));
		panel.getFlexCellFormatter().setAlignment(row, 0, 
				HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE
		);
		row++;

		contents.add(panel);
		contents.add(createWidget());
	}

	
	private Widget createWidget() {
		FlexTable panel = new FlexTable();
		int row = 0;
		for ( RadioButton rb : rbs ) {
			panel.getFlexCellFormatter().setColSpan(row, 0, 2);
			panel.setWidget(row, 0, rb);
			panel.getFlexCellFormatter().setAlignment(row, 1, 
					HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE
			);
			row++;
		}
		
		String info2 = 
			"<br/>" +
			"<br/>" +
			"See this <a target=\"_blank\" href=\"" +PortalConsts.REG_TYPE_HELP_PAGE+ "\"" +
					">manual page</a> for details."
		;
		panel.setWidget(row, 0, new HTML(info2));
		panel.getFlexCellFormatter().setAlignment(row, 0, 
				HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE
		);
		
		
		return panel;
	}

	@Override
	public RegisterNewWizard getWizard() {
		return (RegisterNewWizard) wizard;
	}

}
