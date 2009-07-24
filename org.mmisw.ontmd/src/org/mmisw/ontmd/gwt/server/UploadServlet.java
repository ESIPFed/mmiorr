package org.mmisw.ontmd.gwt.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This servlet does the "pre-upload" operation, that is, it stores the uploaded file from
 * the client in the {@link Config.Prop#ONTMD_PRE_UPLOADS_DIR} directory.
 * 
 * @author Carlos Rueda
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/** Maximum size of an uploaded file */
	// Was initially set to 2MB, too short; it's now unlimited.
	// See issue #153 "Large ontology file fails to load"
	private static final long MAX_FILE_SIZE = -1L;
	
	
	private final Log log = LogFactory.getLog(UploadServlet.class);
	
	
	private static File preUploadsDir;

	
	public void init() throws ServletException {
		super.init();
		log.info("initializing upload service ...");
		try {
			Config.getInstance().init(getServletConfig(), null, true);
			preUploadsDir = new File(Config.Prop.ONTMD_PRE_UPLOADS_DIR.getValue());
			log.info("preUploadsDir = " +preUploadsDir);
		}
		catch (Exception ex) {
			log.error("Cannot initialize: " +ex.getMessage(), ex);
			throw new ServletException("Cannot initialize", ex);
		}
	}
	
	public void destroy() {
		super.destroy();
		log.info("destroy called.\n\n");
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Check that we have a file upload request
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if ( ! isMultipart ) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "not a multipart request");
			return;
		}

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// size threshold beyond which files are written directly to disk.
		factory.setSizeThreshold(10*1024);
//		factory.setRepository(yourTempDirectory);

		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		upload.setFileSizeMax(MAX_FILE_SIZE);
		
		
		// Parse the request
		List<?> /* FileItem */ items;
		try {
			items = upload.parseRequest(request);
		}
		catch (FileUploadException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
			return;
		}
		
		
		// Process the uploaded items
		Iterator<?> iter = items.iterator();
		while (iter.hasNext()) {
		    FileItem item = (FileItem) iter.next();

		    if (item.isFormField()) {
		        processFormField(item);
		    } 
		    else {
		        processUploadedFile(request, response, item);
		    }
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void processUploadedFile(HttpServletRequest request, HttpServletResponse response,
			FileItem item) 
	throws IOException {
		
		String ct = item.getContentType();
		if ( log.isDebugEnabled() ) {
			log.debug("processUploadedFile: item=" +item);
			log.debug("getContentType=" +ct);
		}
		
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		try {
			String sessionId = request.getSession().getId();
			File file = File.createTempFile("ontmd_" +sessionId+"_", ".tmp", preUploadsDir );
			String filename = file.getAbsolutePath();
			InputStream is = item.getInputStream();
			PrintWriter os = new PrintWriter(file);
			IOUtils.copy(is, os);
			
			os.flush();
			os.close();
			
			out.println("<success><filename>" +filename+ "</filename></success>");
			if ( log.isDebugEnabled() ) {
				log.debug("temporary file: " +filename);
			}
			
		}
		catch (Exception ex) {
			out.println("<error>" +ex.getMessage()+ "</error>");
			log.error("Error while creating temporary file: " +ex.getMessage(), ex);
		}
		
	}


	private void processFormField(FileItem item) {
		// ignored
	}

}