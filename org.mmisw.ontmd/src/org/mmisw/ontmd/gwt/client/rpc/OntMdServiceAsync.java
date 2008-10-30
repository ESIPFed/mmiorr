package org.mmisw.ontmd.gwt.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async interface for Voc2RdfService
 * 
 * @author Carlos Rueda
 * @version $Id$
 */
public interface OntMdServiceAsync {

	void getBaseInfo(AsyncCallback<BaseInfo> callback);
	
	void login(String userName, String userPassword, AsyncCallback<LoginResult> callback);
	
	void getOntologyInfo(String uploadResults, AsyncCallback<OntologyInfo> callback);
	
	void review(OntologyInfo ontologyInfo, LoginResult loginResult, AsyncCallback<ReviewResult> callback);
	
	void upload(ReviewResult reviewResult, LoginResult loginResult, AsyncCallback<UploadResult> callback);
	
}