package org.mmisw.orrportal.gwt.client.portal;

import org.mmisw.orrportal.gwt.client.Orr;
import org.mmisw.orrportal.gwt.client.util.TLabel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel for searching ontologies.
 * 
 * @author Carlos Rueda
 */
public class SearchOntologiesPanel extends HorizontalPanel {
	
	private static final String tooltip = 
		"Finds registered ontologies according to a given string, which is " +
		"searched in basic properties of the ontology."
	;
	
	// fix #188: Clarify search options on portal front page
	private TLabel searchLabel = new TLabel("Search&nbsp;ontologies", tooltip);
//	private final HTML searchLabel = new HTML("Search&nbsp;ontologies:&nbsp;");
	
	// static to "remember" values
	private static final TextBox textBox = new TextBox();

	private final PushButton searchButton = new PushButton(Orr.images.search().createImage(), new ClickListener() {
		public void onClick(Widget sender) {
			_dispatchSearch();
		}
	});
	

	/**
	 * Creates a field with a choose feature.
	 */
	public SearchOntologiesPanel() {
		
		this.setVerticalAlignment(ALIGN_MIDDLE);
		
		textBox.setWidth("200px");

		textBox.addKeyboardListener(new KeyboardListenerAdapter() {
			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				if ( searchButton.isEnabled() && keyCode == KEY_ENTER ) {
					_dispatchSearch();
				}
			}
		});
		
		searchLabel.setTitle(tooltip);
		textBox.setTitle(tooltip);
		searchButton.setTitle(tooltip);
		
		this.add(searchLabel);
		this.add(textBox);
		this.add(searchButton);
		
		new Timer() {
			@Override
			public void run() {
				textBox.setFocus(true);
			}
		}.schedule(300);
	}

	private void _dispatchSearch() {
		String str = textBox.getText().trim();
		if (str.length() > 0 ) {
//			str = URL.encode(str).replace("/", "%2F");
			History.newItem(PortalConsts.T_SEARCH_ONTS + "/" + str);
		}
		else {
			History.newItem(PortalConsts.T_BROWSE);
		}
	}
	
	void dispatchSearchOntologies(String str) {
		textBox.setText(str);
		_doSearch();
	}
	
	private void _doSearch() {
		String searchString = getSearchString();
		enable(false);
		PortalControl.getInstance().searchOntologies(searchString, new Command() {
			public void execute() {
				enable(true);
				textBox.selectAll();
			}
		});
	}
	
	private void enable(boolean enabled) {
		textBox.setReadOnly(!enabled);  
		searchButton.setEnabled(enabled);
	}

	private String getSearchString() {
		return textBox.getText().trim();
	}

}
