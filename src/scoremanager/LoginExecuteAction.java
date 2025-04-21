package scoremanager;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

public class LoginExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res){
		HttpSession session = req.getSession();

		String userId = null;
		String userPassword = null;


		Teacher teacher = new Teacher();
		teacher = null;
		TeacherDao teacherDao = new TeacherDao();

		Map<String,String> errors = new HashMap<>();

		userId = (String)req.getParameter("id");
		userPassword = (String)req.getParameter("password");


		try{
			teacher = teacherDao.login(userId, userPassword);
			if(teacher != null){
				// ログイン成功
				teacher.setAuthenticated(true);
				session.setAttribute("user", teacher);
				res.sendRedirect("main/Menu.action");
			}else{
				// ログイン失敗
				errors.put("error_login", "ログインに失敗しました。IDまたはパスワードが正しくありません。");
				req.setAttribute("errors", errors);
				req.setAttribute("id", userId);
				req.setAttribute("password", userPassword);
				req.getRequestDispatcher("Login.action").forward(req, res);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
