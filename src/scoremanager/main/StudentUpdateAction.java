// 学生更新
package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req,res);

		StudentDao stuDao = new StudentDao();
		ClassNumDao cNumDao = new ClassNumDao();

		// ログイン前の不正アクセス防止
		if(user == null){
			res.sendRedirect("/exam/scoremanager/Login.action");
			return;
		}

		// urlに乗せて渡されているパラメーターを取得
		String studentNo = req.getParameter("no");

		// student初期化
		Student student = new Student();
		student = null;

		// studentテーブルからデータ取得
		student=stuDao.get(studentNo);

		// クラスの一覧を取得
		List<String> classNumSet = cNumDao.filter(user.getSchool());

		if(student == null){
			/*
			 * この処理が働くときはDBにアクセスできなかった時のみなのでエラーページにリダイレクト
			 * */
			res.sendRedirect("/error.jsp");
			return;
		}else{
			req.setAttribute("now_ent_year", student.getEntYear());
			req.setAttribute("now_no", student.getNo());
			req.setAttribute("now_name", student.getName());
			req.setAttribute("now_class_num", student.getClassNum());
			req.setAttribute("now_is_attend", student.isAttend());
			req.setAttribute("class_num_set", classNumSet);
		}

		req.getRequestDispatcher("student_update.jsp").forward(req, res);
	}
}