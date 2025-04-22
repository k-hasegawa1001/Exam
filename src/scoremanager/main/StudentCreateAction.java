// 学生登録
package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class StudentCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		@SuppressWarnings("unused")
		Teacher user = this.getUserFromSession(req, res);


		ClassNumDao cNumDao = new ClassNumDao();

		// 入学年度を今年を基準に10年前から10年後までの年度リストを作成
		// 現在の年を作成
		LocalDate todayDate=LocalDate.now();
		int year =todayDate.getYear();
		List<Integer> entYearSet=new ArrayList<>();
		// 10年前から10年後まで年をリストに追加
		for(int i=year-10;i<year+10;i++){
			entYearSet.add(i);
		}
		req.setAttribute("ent_year_set", entYearSet);

		List<String> class_num_set = cNumDao.filter(user.getSchool());
		req.setAttribute("class_num_set", class_num_set);

		req.getRequestDispatcher("student_create.jsp").forward(req, res);
	}
}
