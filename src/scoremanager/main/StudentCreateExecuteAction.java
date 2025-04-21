// 学生登録
package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = getUserFromSession(req, res);
		Student student = new Student();
		StudentDao stuDao = new StudentDao();

		Map<String,String> errors = new HashMap<>();

		// 入力値を格納する変数を定義
		String entYearStr = null;
		String noStr = null;
		String nameStr = null;
		String classNumStr = null;

		// 入力値を格納
		entYearStr = req.getParameter("ent_year");
		noStr = req.getParameter("no");
		nameStr = req.getParameter("name");
		classNumStr = req.getParameter("class_num");

		if(entYearStr.equals("0")){
			// 入学年度未入力
			errors.put("error_entYear", "入学年度を選択してください。");
			req.setAttribute("errors", errors);
			req.setAttribute("error_no", noStr);
			req.setAttribute("error_name", nameStr);
			req.setAttribute("error_class_num", classNumStr);
			req.getRequestDispatcher("StudentCreate.action").forward(req, res);
		}

		if(stuDao.get(noStr) != null){
			// すでに当該学生番号の生徒が存在していた場合のエラー処理
			errors.put("erros_duplication_no", "学生番号が重複しています。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("StudentCreate.action").forward(req, res);
		}

		// studentにデータをセット
		student.setNo(noStr);
		student.setName(nameStr);
		student.setEntYear(Integer.parseInt(entYearStr));
		student.setClassNum(classNumStr);
		student.setAttend(true);
		student.setSchool(user.getSchool());

		if(stuDao.save(student)){
			req.getRequestDispatcher("student_create_done.jsp").forward(req, res);
		}
	}
}
