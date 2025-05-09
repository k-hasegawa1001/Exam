package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import dao.TestListSubjectDao;
import tool.Action;

public class TestListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req, res);

		ClassNumDao cNumDao=new ClassNumDao();
		SubjectDao subDao = new SubjectDao();
		Map<String,String> errors=new HashMap<>();

		String entYearStr = "0"; // f1
		String classNumStr = "0"; // f2
		String subjectCdStr = "0"; // f3

		String student_no = ""; // f4

		LocalDate todayDate=LocalDate.now();
		int year =todayDate.getYear();

		List<String> classNumSet = cNumDao.filter(user.getSchool());
		List<Subject> subjectList = subDao.filter(user.getSchool());
//		List<String> subjectCdSet = new ArrayList<>();
//		for(Subject subject : subjectList){
//			subjectCdSet.add(subject.getCd());
//		}

		TestListStudentDao TLStuDao = new TestListStudentDao();
		TestListSubjectDao TLSubDao = new TestListSubjectDao();

		List<TestListSubject> TLSubSet = new ArrayList<>();
//		TLSubSet = null;

		int entYear = 0;

		entYearStr = req.getParameter("f1");
		classNumStr = req.getParameter("f2");
		subjectCdStr = req.getParameter("f3");

//		System.out.println(entYearStr);

		student_no = req.getParameter("f4");

//		System.out.println(student_no);

		Subject subject = subDao.get(subjectCdStr, user.getSchool());

		if(student_no == null && entYearStr!=null){
			// TLSubDao
//			res.sendRedirect("TestListSubjectExecute.action");
		}else{
			// TLStuDao
		}

		List<Integer> entYearSet=new ArrayList<>();
		// 10年前から1年後まで年をリストに追加
		for(int i=year-10;i<year+1;i++){
			entYearSet.add(i);
		}

		req.setAttribute("f1", entYear);
		req.setAttribute("f2", classNumStr);
		req.setAttribute("f3", subject);

		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", classNumSet);
		req.setAttribute("subject_set", subjectList);

		req.setAttribute("test_list_subjects", TLSubSet);

		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}
