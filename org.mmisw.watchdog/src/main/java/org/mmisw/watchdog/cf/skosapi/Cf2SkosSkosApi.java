package org.mmisw.watchdog.cf.skosapi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.mmisw.watchdog.cf.Cf2SkosBase;
import org.mmisw.watchdog.util.jena.SKOS;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skos.SKOSEntity;
import org.semanticweb.skosapibinding.SKOSManager;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;



/**
 * Cf2Skos implementation based on SKOS API.
 * 
 * @author carueda
 */
public class Cf2SkosSkosApi extends Cf2SkosBase {
	
	protected void _doConvert() throws Exception {
		_createNewOntology();
		_convert();
	}

	protected void _doSave() throws IOException {
		_saveNewOntology();
	}

	
	///////////////////////////////////////////////////////////////////////////////
	// private
	///////////////////////////////////////////////////////////////////////////////
	
	private Model model;

	private Resource standardNameClass;
	
	private Resource currentTopConcept;

	private Property canonical_units;

	
	private void _createNewOntology() throws Exception {
		
		String baseURI = inputUrl.toString();
		
		SKOSManager manager = new SKOSManager();

		SKOSDataset dataset = manager.createSKOSDataset(URI.create(baseURI));
		
        SKOSDataFactory df = manager.getSKOSDataFactory();

        List<SKOSEntity> allEntities = new ArrayList<SKOSEntity>();

        // Create a concept scheme identified by a URI
        SKOSConceptScheme conceptScheme1 = df.getSKOSConceptScheme(URI.create(baseURI + "#conceptScheme1"));

        
		// create ontology
		model = SKOS.getAnSKOSModel();
		
		model.setNsPrefix("", NS);

		// creates top concept scheme
//		conceptScheme = model.createResource(NS + "cf", SKOS.ConceptScheme);
//		conceptScheme.addProperty(DC.creator, "Luis Bermudez MMI");
//		conceptScheme.addProperty(DC.date, (new java.text.SimpleDateFormat(
//				"yyyy-MM-dd'T'hh:mm:ss")).format(new Date(System
//				.currentTimeMillis())));
//		conceptScheme.addProperty(DC.description, "CF Terms");

		
		standardNameClass = model.createProperty(NS, "Standard_Name");
		
		currentTopConcept = model
				.createResource(NS + "parameter", standardNameClass);
//				.createResource(NS + "parameter", skosConcept);
//				.createResource(NS + "parameter", SKOS.Concept);

		Statement stmt;
		
		stmt = model.createStatement(standardNameClass, RDF.type, OWL.Class);
		model.add(stmt);
		
		stmt = model.createStatement(standardNameClass, RDFS.subClassOf, SKOS.Concept);
		model.add(stmt);

		stmt = model.createStatement(standardNameClass, RDFS.label, "Standard Name");
		model.add(stmt);
		
		stmt = model.createStatement(standardNameClass, RDFS.label, "Standard Name");
		model.add(stmt);

		
		canonical_units = model.createProperty(NS + "canonical_units");
		
		stmt = model.createStatement(canonical_units, RDF.type, OWL.DatatypeProperty);
		model.add(stmt);
		stmt = model.createStatement(canonical_units, RDFS.domain, standardNameClass);
		model.add(stmt);
		stmt = model.createStatement(canonical_units, RDFS.range, XSD.xstring);
		model.add(stmt);


	}

	private void _convert() throws IOException {
		try {
			SAXBuilder builder = new SAXBuilder();

			Document document = builder.build(inputUrl);

			Element standard_name_table = document.getRootElement();
			List list = standard_name_table.getChildren("entry");
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Element ele = (Element)iterator.next();
				String id = (ele.getAttribute("id").getValue()).trim();
				
				String canonicalUnits = ele.getChildTextNormalize("canonical_units");
//				String grib = ele.getChildTextNormalize("grib");
//				String amip = ele.getChildTextNormalize("amip");
				
				String description = ele.getChildTextNormalize("description");
				
				String prefLabel = id.replace('_', ' ');
				
				Resource concept = model.createResource(NS + id,
						standardNameClass);
//						skosConcept);
//						SKOS.Concept);
				
				concept.addProperty(SKOS.prefLabel, prefLabel);
				concept.addProperty(RDFS.label, id);
				concept.addProperty(canonical_units, canonicalUnits);
				concept.addProperty(RDFS.comment, description);
				
				currentTopConcept.addProperty(SKOS.narrower, concept);
				
				_log("\tResource created: "+concept);
			}
		} 
		catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	private void _saveNewOntology() {

		RDFWriter writer = model.getWriter("RDF/XML-ABBREV");
		writer.setProperty("showXmlDeclaration", "true");
		writer.setProperty("relativeURIs", "same-document,relative");
		writer.setProperty("xmlbase", NS);
		try {
			FileOutputStream fo = new FileOutputStream(fileOut);
			// model.setNsPrefix("",NS);
			// model.write(fo,"RDF/XML-ABBREV");

			writer.write(model, fo, null);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		_log("New SKOS Ontology saved in: " + fileOut);
		_log("Size of the new Ontology: " + model.size());
	}

}
