package eduportal.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.appengine.api.blobstore.*;

import eduportal.dao.OrderDAO;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.model.AccessSettings;
import eduportal.model.AuthContainer;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class FileProcessorServlet extends HttpServlet {
	private static final long serialVersionUID = 4212663183594760678L;

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		UserEntity user = null;
		for (Object o : req.getParameterMap().keySet()) {
			System.out.println(o.toString());
		}
		String token = req.getParameter("token");
		String userid = req.getParameter("userid");
		String productid = req.getParameter("productid");
		String orderid = req.getParameter("orderid");
		user = AuthContainer.getUser(token);
		if (user == null) {
			for (Cookie c : req.getCookies()) {
				if (c.getName().equals("sesToken")) {
					user = AuthContainer.getUser(c.getValue());
				}
			}
		}
		if (user == null) {
			res.sendRedirect("/auth.html");
			return;
		}
		Order order = null;
		Product product = null;
		UserEntity targetUser = null;
		if (orderid != null) {
			order = OrderDAO.getOrder(orderid);
		}
		if (productid != null) {
			product = OrderDAO.getProduct(productid);
		}
		if (userid != null) {
			targetUser = UserDAO.get(userid);
		}
		if (order == null && product == null && targetUser == null) {
			res.sendRedirect("/workspace.jsp");
			return;
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
			if (order != null) {
				order.addFile(file);
				OrderDAO.saveOrder(order);
			}
			if (product != null) {
				product.addFile(file);
				OrderDAO.saveProduct(product);
				System.out.println("Prod != null");
			} else {
				System.out.println("Prod == null");
			}
			if (targetUser != null) {
				targetUser.addFile(file);
				UserDAO.update(targetUser);
			}
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
