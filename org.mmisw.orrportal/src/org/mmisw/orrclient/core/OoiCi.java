package org.mmisw.orrclient.core;

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mmisw.ont.client.util.AquaUtil;
import org.mmisw.orrclient.core.util.OntServiceUtil;
import org.mmisw.orrclient.core.util.Util2;
import org.mmisw.orrclient.gwt.client.rpc.BaseOntologyInfo;
import org.mmisw.orrclient.gwt.client.rpc.CreateOntologyInfo;
import org.mmisw.orrclient.gwt.client.rpc.LoginResult;
import org.mmisw.orrclient.gwt.client.rpc.RegisterOntologyResult;
import org.mmisw.orrclient.gwt.client.rpc.RegisteredOntologyInfo;
import org.mmisw.orrclient.gwt.client.rpc.TempOntologyInfo;

/**
 * Some functionality developed in the context of the OOI CI semantic prototype.
 * @author Carlos Rueda
 */
public class OoiCi {
	
	private static final Log log = LogFactory.getLog(OoiCi.class);
	
	
	/**
	 * Adapted from Server.registerOntologyFullyHosted (note that the mode for direct registration is "re-hosted")
	 * 
	 * @param loginResult               user info
	 * @param registeredOntologyInfo    current registration, if any, to create new version
	 * @param createOntologyInfo        info for the new registration
	 * @param graphId                   desired graph to be updated
	 * @return
	 */
	static RegisterOntologyResult registerOntologyDirectly(
			LoginResult loginResult, 
			RegisteredOntologyInfo registeredOntologyInfo,
			CreateOntologyInfo createOntologyInfo, 
			String graphId
	) {
		
		RegisterOntologyResult registerOntologyResult = new RegisterOntologyResult();
		
		BaseOntologyInfo baseOntologyInfo = createOntologyInfo.getBaseOntologyInfo();
		if ( ! (baseOntologyInfo instanceof TempOntologyInfo ) ) {
			String error = "Unexpected: baseOntologyInfo is not TempOntologyInfo. It is " +baseOntologyInfo.getClass().getName();
			log.info(error);
			registerOntologyResult.setError(error);
			return registerOntologyResult;
		}
		
		TempOntologyInfo tempOntologyInfo = (TempOntologyInfo) baseOntologyInfo;
		
		String full_path = tempOntologyInfo.getFullPath();
		
		log.info("registerOntologyDirectly: Reading in temporary file: " +full_path);
		
		// get the RDF contents:
		File file = new File(full_path);
		String rdf;
		try {
			rdf = Util2.readRdfWithCheckingUtf8(file);
		}
		catch (Throwable e) {
			String error = "Unexpected: error while reading from: " +full_path+ " : " +e.getMessage();
			log.info(error);
			registerOntologyResult.setError(error);
			return registerOntologyResult;
		}
		
		// ok, we have our ontology:
		
		
		//////////////////////////////////////////////////////////////////////////
		// finally, do actual registration to MMI registry

		// Get final URI of resulting model
		final String uri = createOntologyInfo.getUri();
		assert uri != null;
		assert loginResult.getUserId() != null;
		assert loginResult.getSessionId() != null;
		
		log.info(": registering URI: " +uri+ " ...");

		String ontologyId = createOntologyInfo.getPriorOntologyInfo().getOntologyId();
		String ontologyUserId = createOntologyInfo.getPriorOntologyInfo().getOntologyUserId();
		
		if ( ontologyId != null ) {
			log.info("Will create a new version for ontologyId = " +ontologyId+ ", userId=" +ontologyUserId);
		}
		
		
		Map<String, String> newValues = createOntologyInfo .getMetadataValues();
		

		try {
			// this is to get the filename for the registration
			String fileName = AquaUtil.getAquaportalFilename(uri); 
			
			// OK, now do the actual registration:
			OntologyUploader createOnt = new OntologyUploader(uri, fileName, rdf, 
					loginResult,
					ontologyId, ontologyUserId,
					newValues
			);
			String res = createOnt.create();
			
			if ( res.startsWith("OK") ) {
				registerOntologyResult.setUri(uri);
				registerOntologyResult.setInfo(res);
				
				// request that the ontology be loaded in the desired graph:
				OntServiceUtil.loadOntologyInGraph(uri, graphId);
			}
			else {
				registerOntologyResult.setError(res);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			registerOntologyResult.setError(ex.getClass().getName()+ ": " +ex.getMessage());
		}
		
		log.info("registerOntologyResult = " +registerOntologyResult);

		return registerOntologyResult;
	}

}
