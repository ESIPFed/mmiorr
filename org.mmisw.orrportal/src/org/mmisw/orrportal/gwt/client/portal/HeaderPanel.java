package org.mmisw.orrportal.gwt.client.portal;

import java.util.ArrayList;
import java.util.List;

import org.mmisw.orrclient.gwt.client.rpc.LoginResult;
import org.mmisw.orrportal.gwt.client.Orr;
import org.mmisw.orrportal.gwt.client.portal.PortalMainPanel.InterfaceType;
import org.mmisw.orrportal.gwt.client.util.OrrUtil;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Portal header panel.
 * 
 * @author Carlos Rueda
 */
public class HeaderPanel extends FlexTable {

	private static final String HELP_LINK = "http://marinemetadata.org/orr";
	private static final String TOU_LINK = "http://marinemetadata.org/orr/tou";

	private HorizontalPanel linksPanel1 = new HorizontalPanel();
	private HorizontalPanel linksPanel2 = new HorizontalPanel();
	
	private Widget adminLink = new Hyperlink("<b>Admin</b>", true, PortalConsts.T_ADMIN);
	
	private Widget browseLink = new Hyperlink("Browse", PortalConsts.T_BROWSE);
	private Widget searchLink = new Hyperlink("Search terms", PortalConsts.T_SEARCH_TERMS);
	
	private Hyperlink accountLink = new Hyperlink("Create account", PortalConsts.T_USER_ACCOUNT);
	
//	private Widget signInLink = new Hyperlink("Sign in", PortalConsts.T_SIGN_IN);
	private Widget signInLink = new HButton("Sign in",
			new ClickListener() {
				public void onClick(Widget sender) {
					PortalControl.getInstance().userToSignIn();
				}
	});
	
//	private Widget signOutLink = new Hyperlink("Sign out", PortalConsts.T_SIGN_OUT);
	private HButton signOutLink = new HButton("Sign out",
			new ClickListener() {
				public void onClick(Widget sender) {
					PortalControl.getInstance().userSignedOut();
				}
	});
	
	private HTML helpButton = new HTML(
			"<a target=\"_blank\" href=\"" +HELP_LINK+ "\">Help</a>");

	private HTML touButton = new HTML(
			"<a target=\"_blank\" href=\"" +TOU_LINK+ "\">Terms of Use</a>");

	
	HeaderPanel() {
		super();
		
		adminLink.setTitle("Administrative interface");
		searchLink.setTitle("Allows to search terms in all registered ontologies");
		
		linksPanel1.setSpacing(4);
		linksPanel2.setSpacing(4);
		
		FlexTable flexPanel = this;
		flexPanel.setWidth("100%");
//		flexPanel.setBorderWidth(1);
		int row = 0;
		
		flexPanel.getFlexCellFormatter().setRowSpan(row, 0, 2);
		flexPanel.setWidget(row, 0, Orr.images.mmior().createImage());
		flexPanel.getFlexCellFormatter().setWidth(row, 0, "10%");
		flexPanel.getFlexCellFormatter().setAlignment(row, 0, 
				HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE
		);

		flexPanel.setWidget(row, 1, linksPanel1);
		flexPanel.getFlexCellFormatter().setAlignment(row, 1, 
				HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP
		);

		row++;
		flexPanel.setWidget(row, 0, linksPanel2);
		flexPanel.getFlexCellFormatter().setAlignment(row, 0, 
				HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_BOTTOM
		);
	}
	
	/**
	 * Updates the various links and buttons according to the interface type and
	 * whether there is a users logged in.
	 */
	void updateLinks(InterfaceType type) {
		LoginResult loginResult = PortalControl.getInstance().getLoginResult();
	
		List<Widget> widgets = new ArrayList<Widget>();
		List<Widget> widgets2 = new ArrayList<Widget>();
		
		if ( loginResult != null ) {
			accountLink.setText(loginResult.getUserName());
		}
		else if ( type != InterfaceType.USER_ACCOUNT ) {
			accountLink.setText("Create account");
		}

		boolean editing = false;
		
		switch ( type ) {
		case BROWSE:
			widgets2.add(searchLink);
			break;
		case ONTOLOGY_VIEW:
		case USER_ACCOUNT:
		case ENTITY_VIEW:
		case ENTITY_NOT_FOUND:
		case ADMIN:
			widgets2.add(browseLink);
			widgets2.add(searchLink);
			break;
		case SEARCH: 
			widgets2.add(browseLink);
			break;
		case ONTOLOGY_EDIT_NEW_VERSION:
		case ONTOLOGY_EDIT_NEW:
			editing = true;
			break;
		}
		
		widgets.add(helpButton);
		widgets.add(touButton);
		
		if ( loginResult == null ) {
			if ( type != InterfaceType.USER_ACCOUNT ) {
				accountLink.setText("Create account");
				widgets.add(accountLink);
				widgets.add(signInLink);
			}
		}
		else {
			if ( editing ) {
				// note, just a label, not a clickable thing
				widgets.add(new Label(loginResult.getUserName()));
			}
			else {
				widgets.add(accountLink);
				widgets.add(signOutLink);
			}
		}
		
		
		if ( type != InterfaceType.ADMIN && OrrUtil.isOrrAdmin(loginResult) ) {
			widgets2.add(adminLink);
		}

		// now assign to linksPanels
		linksPanel1.clear();
		linksPanel2.clear();
		String sep = null;
		for ( Widget widget : widgets ) {
			if ( sep != null ) {
				linksPanel1.add(new Label(sep));
			}
			linksPanel1.add(widget);	
			sep = "|";
		}

		sep = null;
		for ( Widget widget : widgets2 ) {
			if ( sep != null ) {
				linksPanel2.add(new Label(sep));
			}
			linksPanel2.add(widget);	
			sep = "|";
		}

	}

	
	private static class HButton extends PushButton {

		HButton(String text, ClickListener listener) {
			super();
			setHTML(text.replaceAll("\\s", "&nbsp;"));
			addClickListener(listener);
			DOM.setElementAttribute(getElement(), "id", "my-button-id");
		}
	}
}
