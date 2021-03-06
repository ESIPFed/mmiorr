package org.mmisw.ont;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Provides version information about the Ont service.
 * 
 * @author Carlos Rueda
 */
public final class OntVersion {
	
	// Version and build info is retrieved from the version.properties resource in this package.
	// This resource is prepared by build.xml from a master version.properties.
	//
	// If the associated resource cannot be read or the corresponding properties are
	// not defined, warnings are logged out ({@link org.apache.commons.logging.Log#warn(Object)}) 
	// and values of the form "undefXXX" are reported.
	
	/** 
	 * Returns the version.
	 */
	public static String getVersion() {
		return version;
	}
	
	/**
	 * Returns the build.
	 */
	public static String getBuild() {
		return build;
	}
	
	/**
	 * Returns a descriptive string with version and build.
	 */
	public static String getVersionAndBuild() {
		return versionAndBuild;
	}
	
	/**
	 * Returns the full title of the service.
	 */
	public static String getFullTitle() {
		return fullTitle;
	}
	
	/**
	 * Returns the title (without version) of the service.
	 */
	public static String getTitle() {
		return TITLE;
	}
	
	
	/** retrieved from properties file */
	private static String version;
	
	/** retrieved from properties file */
	private static String build;
	

	private static final String TITLE = "MMI Ontology and Term URI Resolver";
	
	/** composed from version and build */
	private static String versionAndBuild = "?";
	
	/** composed from title, version and build */
	private static String fullTitle = "?";
	
	private static final String VERSION_PROP = "ont.version";
	private static final String BUILD_PROP = "ont.build";
	private static final String VERSION_UNDEF_VALUE = "undefVersion";
	private static final String BUILD_UNDEF_VALUE = "undefBuild";
	
	
	
	static {
		Log log = LogFactory.getLog(OntVersion.class);
		InputStream is = OntVersion.class.getResourceAsStream("version.properties");
		if ( is == null ) {
			log.warn("Cannot get resource: version.properties");
		}
		else {
			Properties verProps = new Properties();
			try {
				verProps.load(is);
				version = verProps.getProperty(VERSION_PROP, VERSION_UNDEF_VALUE);
				build = verProps.getProperty(BUILD_PROP, BUILD_UNDEF_VALUE);
				
				versionAndBuild = version+ " (" +build+ ")";
				fullTitle = TITLE + ". Version " +versionAndBuild;
			}
			catch (IOException e) {
				log.warn("Cannot load version.properties", e);
			}
			finally {
				IOUtils.closeQuietly(is);
			}
		}
	}
	
	private OntVersion() {}
}
