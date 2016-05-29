package eduportal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import eduportal.dao.UserDAO;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AuthContainer;
import eduportal.util.UserUtils;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 6224885254427565287L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String exist = req.getParameter("exist");
		String newPass = req.getParameter("new");
		UserEntity user = null;
		for (Cookie c : req.getCookies()) {
			if (c.getName().equals("sesToken")) {
				user = AuthContainer.getUser(c.getValue());
			}
		}
		if (user == null) {
			resp.sendRedirect("/auth.html");
		}
		exist = UserUtils.encodePass(exist);
		if (user.getPass().equals(exist) == false) {
			System.out.println("Wrong pass");
			return;
		}
		if (newPass.equals(req.getParameter("valid")) == false) {
			return;
		}
		user.setPass(newPass);
		UserDAO.update(user);
	}
}
