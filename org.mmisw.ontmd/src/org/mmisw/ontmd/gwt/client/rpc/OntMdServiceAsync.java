package org.mmisw.ontmd.gwt.client.rpc;

import java.util.List;
import java.util.Map;

import org.mmisw.iserver.gwt.client.rpc.AppInfo;
import org.mmisw.iserver.gwt.client.rpc.BasicOntologyInfo;
import org.mmisw.iserver.gwt.client.rpc.CreateOntologyInfo;
import org.mmisw.iserver.gwt.client.rpc.CreateOntologyResult;
import org.mmisw.iserver.gwt.client.rpc.EntityInfo;
import org.mmisw.iserver.gwt.client.rpc.LoginResult;
import org.mmisw.iserver.gwt.client.rpc.MetadataBaseInfo;
import org.mmisw.iserver.gwt.client.rpc.OntologyInfo;
import org.mmisw.iserver.gwt.client.rpc.TempOntologyInfo;
import org.mmisw.iserver.gwt.client.rpc.UploadOntologyResult;
import org.mmisw.iserver.gwt.client.vocabulary.AttrDef;
import org.mmisw.ontmd.gwt.client.voc2rdf.rpc.ConversionResult;
import org.mmisw.ontmd.gwt.client.voc2rdf.rpc.Voc2RdfBaseInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async interface for Voc2RdfService
 * 
 * @author Carlos Rueda
 * @version $Id$
 */
public interface OntMdServiceAsync {

	void getAppInfo(AsyncCallback<AppInfo> callback);
	
	void refreshOptions(AttrDef attrDef, AsyncCallback<AttrDef> callback);
	
	void getBaseInfo(Map<String, String> params, AsyncCallback<MetadataBaseInfo> callback);
	
	void login(String userName, String userPassword, AsyncCallback<LoginResult> callback);
	
	void getOntologyInfoFromRegistry(String ontologyUri, AsyncCallback<OntologyInfoPre> callback);
	
	void getOntologyInfoFromPreLoaded(String uploadResults, AsyncCallback<OntologyInfoPre> callback);
	
	void getOntologyInfoFromFileOnServer(String fullPath, AsyncCallback<OntologyInfoPre> callback);
	
	void review(OntologyInfoPre ontologyInfoPre, LoginResult loginResult_Old, AsyncCallback<ReviewResult_Old> callback);
	
	void upload(ReviewResult_Old reviewResult_Old, LoginResult loginResult_Old, AsyncCallback<UploadResult> callback);
	
	
	///////////////////////////////////////////////////////////////////////
	// Voc2RDF
	
	void getVoc2RdfAppInfo(AsyncCallback<AppInfo> callback);
	
	void getVoc2RdfBaseInfo(AsyncCallback<Voc2RdfBaseInfo> callback);
	
	void convert2Rdf(Map<String,String> values, AsyncCallback<ConversionResult> callback);

	
	///////////////////////////////////////////////////////////////////////
	// data

	@Deprecated
	void getData(OntologyInfoPre ontologyInfoPre, AsyncCallback<DataResult> callback);

	void getEntities(String ontologyUri, AsyncCallback<List<EntityInfo>> callback);
	

	///////////////////////////////////////////////////////////////////////
	// Portal
	
	void getPortalAppInfo(AsyncCallback<AppInfo> callback);
	
	void getPortalBaseInfo(AsyncCallback<PortalBaseInfo> callback);
	
	void getMetadataBaseInfo(boolean includeVersion, AsyncCallback<MetadataBaseInfo> callback);

	void getAllOntologies(boolean includePriorVersions, AsyncCallback <List<OntologyInfo>> callback);

	void getOntologyInfo(String ontologyUri, AsyncCallback<OntologyInfo> callback);
	
	void getOntologyContents(OntologyInfo ontologyInfo, AsyncCallback<OntologyInfo> callback);
	
	
	void createOntology(BasicOntologyInfo basicOntologyInfo, CreateOntologyInfo createOntologyInfo, 
			AsyncCallback<CreateOntologyResult> callback);
	
	
	void uploadOntology(CreateOntologyResult createOntologyResult, LoginResult loginResult,
			AsyncCallback<UploadOntologyResult> callback) ;
	
	
	void getTempOntologyInfo(String uploadResults, AsyncCallback<TempOntologyInfo> callback);
}
