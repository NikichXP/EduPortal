package eduportal.servlet;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1242112415223123L;
	private static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		UserEntity user = null;
		for (Object o : req.getParameterMap().keySet()) {
			System.out.println(o.toString());
		}
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("myFile");
		System.out.println(blobKeys);
		if (blobKeys == null || blobKeys.isEmpty()) {
			res.sendRedirect("/auth");
		} else {
			SavedFile file = new SavedFile();
			file.setId(blobKeys.get(0).getKeyString());
			ofy().save().entity(file);
			
			if (user instanceof Employee) {
				res.sendRedirect("/admin/moderator.jsp");
			} else {
				res.sendRedirect("/workspace.html");
			}
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		blobstoreService.serve(blobKey, res); 
	}
}
