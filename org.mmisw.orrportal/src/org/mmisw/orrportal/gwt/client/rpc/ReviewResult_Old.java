package org.mmisw.orrportal.gwt.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Info about the result of a review operation.
 * 
 * @author Carlos Rueda
 */
public class ReviewResult_Old extends BaseResult implements IsSerializable {

	private String uri = null;
	private String fullPath;
	private String rdf = null;
	
	private OntologyInfoPre ontologyInfoPre;


	public ReviewResult_Old() {
	}

	
	public OntologyInfoPre getOntologyInfo() {
		return ontologyInfoPre;
	}

	public void setOntologyInfo(OntologyInfoPre ontologyInfoPre) {
		this.ontologyInfoPre = ontologyInfoPre;
	}

	public String getRdf() {
		return rdf;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

	public String toString() {
		return "ReviewResult{uri=" +uri+ ", rdf=" +rdf+" , error=" +error+ "}";
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

}
