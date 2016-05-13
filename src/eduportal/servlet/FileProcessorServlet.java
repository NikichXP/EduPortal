package eduportal.servlet;

import java.io.*;
import java.util.LinkedList;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;

import com.google.appengine.api.datastore.Blob;

import eduportal.dao.entity.UserSavedFile;

public class FileProcessorServlet extends HttpServlet {
	private static final long serialVersionUID = 4212663183594760678L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<h1>" + "LOL" + "</h1>");
	}

	// Your upload handle would look like
	public void doPost(HttpServletRequest req, HttpServletResponse res) {
		
		
//		// Get the image representation
//		ServletFileUpload upload = new ServletFileUpload();
//		FileItemIterator iter = upload.getItemIterator(req);
//		FileItemStream imageItem = iter.next();
//		InputStream imgStream = imageItem.openStream();
		
		
		// construct our entity objects
//		Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
//		UserSavedFile myImage = new UserSavedFile(imageItem.getName(), imageBlob);
//
//		// persist image
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		pm.makePersistent(myImage);
//		pm.close();

		// respond to query
//		res.setContentType("text/plain");
//		res.getOutputStream().write("OK!".getBytes());
	}

}
