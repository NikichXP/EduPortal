package eduportal.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.appengine.api.blobstore.*;

import eduportal.dao.entity.UserEntity;
import eduportal.dao.entity.UserSavedFile;
import eduportal.model.AuthContainer;

public class FileProcessorServlet extends HttpServlet {
	private static final long serialVersionUID = 4212663183594760678L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("POST");
		UserEntity user = null;
		for (Cookie c : req.getCookies()) {
			System.out.println(c.getName() + "   " + c.getValue());
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
			}
		}
		if (user == null) {
			res.sendRedirect("/auth.html");
			return;
		}
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("myFile");
		if (blobKeys == null || blobKeys.isEmpty()) {
			res.sendRedirect("/");
		} else {
			res.sendRedirect("/FileProcessorServlet?blob-key=" + blobKeys.get(0).getKeyString());
		}
		UserSavedFile file = new UserSavedFile();
		
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		System.out.println("GET");
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		blobstoreService.serve(blobKey, res);
	}
}
