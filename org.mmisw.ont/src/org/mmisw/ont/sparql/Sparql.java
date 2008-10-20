package org.mmisw.ont.sparql;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import org.mmisw.ont.util.Unfinished;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Dispatcher of SPARQL queries.
 * 
 * @author Carlos Rueda
 */
@Unfinished(priority=Unfinished.Priority.MEDIUM)
public class Sparql {
	
	public static class QueryResult {
		private String result;
		private String contentType;

		void setResult(String result) {
			this.result = result;
		}

		void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public String getResult() {
			return result;
		}

		public String getContentType() {
			return contentType;
		}
	}

	public static QueryResult executeQuery(Model model, String sparqlQuery) {
		QueryResult queryResult = new QueryResult();
		
		Query query = QueryFactory.create(sparqlQuery);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		
		try {
			if ( query.isConstructType() ) {
				Model model_ = qe.execConstruct();
				StringWriter writer = new StringWriter();
				model_.getWriter().write(model_, writer, null);
				queryResult.setResult(writer.getBuffer().toString());
				queryResult.setContentType("Application/rdf+xml");
			}
			else if ( query.isSelectType() ) {
				ResultSet results = qe.execSelect();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ResultSetFormatter.out(os, results, query);
	
				queryResult.setResult(os.toString());
				queryResult.setContentType("text/plain");
			}
			
			// TODO handle other types of queries.
			else {
				queryResult.setResult("Sorry, query type " +query.getQueryType()+ " not handled yet");
				queryResult.setContentType("text/plain");
			}
		}
		finally {
			qe.close();
		}
		
		return queryResult;
	}

	private Sparql() {}
}