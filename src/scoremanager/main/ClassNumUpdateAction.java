package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassNumUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req,res);

		ClassNumDao classDao = new ClassNumDao();
		ClassNum classnum = new ClassNum();

		String classNum = req.getParameter("Cd");
		System.out.println(classNum);

		classnum = classDao.get(classNum, user.getSchool());
		System.out.println(classnum);


		req.setAttribute("now_classcd", classnum.getClassNum());

		req.getRequestDispatcher("class_update.jsp").forward(req, res);
	}
}
