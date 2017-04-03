package com.eduportal.api;

import com.eduportal.dao.OrderDAO;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.Order;
import com.eduportal.entity.Product;
import com.eduportal.entity.SavedFile;
import com.eduportal.entity.UserEntity;
import com.eduportal.interceptor.Auth;
import com.eduportal.interceptor.LogAction;
import com.eduportal.model.AuthContainer;
import com.eduportal.repo.OrderRepository;
import com.eduportal.repo.ProductRepository;
import com.eduportal.repo.SavedFileRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/file")
public class FileAPI {

	@Autowired
	private SavedFileRepository fileRepo;// = AppLoader.get(SavedFileRepository.class);
	@Autowired
	private ProductRepository prodRepo;// = AppLoader.get(ProductRepository.class);
	@Autowired
	private OrderRepository orderRepo;// = AppLoader.get(OrderRepository.class);

	@RequestMapping("/get")
	public void getFile(HttpServletResponse response, HttpServletRequest request,
	                    @RequestParam("file") String filePath) throws Exception {

		val file = new File(System.getProperty("user.dir") + "/src/main/resources/files/" + filePath);

		if (!file.exists()) {
			response.getWriter().write("File not found");
			return;
		}

		ServletContext sc = request.getSession().getServletContext();
		response.reset();
		response.setContentType(sc.getMimeType(file.getName()));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		org.springframework.util.FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	@RequestMapping("/get/{userId}/{fileId}/{ext}") //alternate mapping
	public void getFile2(HttpServletResponse response, HttpServletRequest request, @PathVariable("userId") String userId,
	                     @PathVariable("fileId") String fileId, @PathVariable("ext") String ext) throws Exception {
		getFile(response, request, userId + "/" + fileId + "." + ext);
	}

	@Auth({Auth.Types.SELF, Auth.Types.MANAGED})
	@Auth.Param("userid")
	@GetMapping("/byUser")
	public ResponseEntity byUser(@RequestParam("userid") String userid) {
		return ResponseEntity.ok(UserDAO.getFiles(userid));
	}

	@LogAction("upload")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity upload(HttpServletRequest request, HttpServletResponse response,
	                             @RequestParam(value = "userid", required = false) String userid,
	                             @RequestParam(value = "orderid", required = false) String orderid,
	                             @RequestParam(value = "productid", required = false) String productid) throws Exception {
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
			response.sendRedirect("/workspace.jsp");
			return ResponseEntity.status(300).build();
		}
		Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
		String fileExt = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
		fileExt = fileExt.substring(fileExt.lastIndexOf("."));
		String fileName = UUID.randomUUID().toString() + fileExt;
		InputStream inputStream = filePart.getInputStream();

		UserEntity u = AuthContainer.getUser(request.getParameter("token"));
		if (u == null) {
			return ResponseEntity.status(403).body("Need authorization");
		}
		String userId = u.getId();
		String path = System.getProperty("user.dir") + "/src/main/resources/files/" + userId + "/";
		File dir = new File(path);
		dir.mkdirs();
		File f = new File(path + fileName);
		System.out.println("Uploading: " + f.getAbsolutePath());
		try {
			f.createNewFile();
		} catch (Exception e) {
			System.out.println("Error creating " + f.getAbsolutePath());
			f.getParentFile().mkdirs();
			f.createNewFile();
		}

		FileOutputStream outputStream = new FileOutputStream(f);

		int read = 0;
		byte[] bytes = new byte[4096];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}

		outputStream.close();

		SavedFile file = new SavedFile();
		file.setId(UUID.randomUUID().toString());
		fileRepo.save(file);

		if (order != null) {
			order.addFile(file);
			OrderDAO.saveOrder(order);
		}
		if (product != null) {
			product.addFile(file);
			OrderDAO.saveProduct(product);
		}
		if (targetUser != null) {
			targetUser.addFile(file);
			UserDAO.update(targetUser);
		}

		return ResponseEntity.ok().body(userId + "/" + fileName);
	}

	//TODO Test methods below

	@RequestMapping("/dir")
	public ResponseEntity dir(@RequestParam(value = "dir", required = false) String dir) {
		if (dir == null) {
			return ResponseEntity.ok().body(File.listRoots());
		} else {
			return ResponseEntity.ok().body(new File(dir).list());
		}
	}

	@RequestMapping("/local")
	public ResponseEntity local() {
		return ResponseEntity.ok().body(System.getProperty("user.dir"));
	}

}
