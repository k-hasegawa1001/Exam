// 学生更新
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		@SuppressWarnings("unused")

		Teacher user = this.getUserFromSession(req, res);
		Student student = new Student();
		StudentDao stuDao = new StudentDao();

		String inputEntYear=null;
		String inputNo=null;
		String inputName=null;
		String inputClassNum=null;
		String inputIsAttend = null;

		// パラメータ取得
		inputEntYear=req.getParameter("ent_year");
		inputNo=req.getParameter("no");
		inputName=req.getParameter("name");
		inputClassNum=req.getParameter("class_num");
		inputIsAttend=req.getParameter("is_attend");

		student.setEntYear(Integer.parseInt(inputEntYear));
		student.setNo(inputNo);
		student.setName(inputName);
		student.setClassNum(inputClassNum);
		if(inputIsAttend != null){
			student.setAttend(true);
		}else{
			student.setAttend(false);
		}
		if(stuDao.save(student)){
			req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
		}
	}
}
