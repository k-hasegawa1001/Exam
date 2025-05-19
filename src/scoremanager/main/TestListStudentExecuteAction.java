package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.TestListStudent;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import tool.Action;

public class TestListStudentExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    	Teacher user = this.getUserFromSession(req, res);

    	TestListStudentDao TLStuDao = new TestListStudentDao();
    	StudentDao stuDao = new StudentDao();
//    	SubjectDao subjectDao = new SubjectDao();
    	ClassNumDao cDao = new ClassNumDao();
    	SubjectDao sDao = new SubjectDao();
//        School school = ((Teacher) req.getSession().getAttribute("user")).getSchool();

        // JSPでのform表示用
        List<Integer> entYearSet = new ArrayList<>();

		LocalDate todeysDate = LocalDate.now();
        int year = todeysDate.getYear();

		// 10年前から1年後まで年をリストに追加
		for (int i = year - 10; i < year + 1; i++){
			entYearSet.add(i);
		}

        // ▼ クラス番号のリスト取得（学校ごとに）
        List<String> classNumList = cDao.filter(user.getSchool());
        System.out.println(classNumList + "class");
        // ▼ 科目一覧取得（SbjectDao に findAll() がある前提）
        List<Subject> subjectList = sDao.filter(user.getSchool());


        // リクエストパラメータ取得
        String studentNoStr = req.getParameter("f4");
        Student student = stuDao.get(studentNoStr);

//        // 入力バリデーション
//        if (entYearStr == null || entYearStr.equals("0") || classNumStr == null || classNumStr.equals("0") ||
//        		subjectCdStr == null || subjectCdStr.equals("0")) {
//            req.setAttribute("error", "すべての項目を入力してください。");
//            // 表示用
//            req.setAttribute("ent_year_set", entYearSet);
//            req.setAttribute("class_num_set", classNumList);
//            req.setAttribute("subject_set", subjectList);
//
//            req.getRequestDispatcher("test_list.jsp").forward(req, res);
//            return;
//        }

        List<TestListStudent> resultList = null;

        resultList = TLStuDao.filter(student,user.getSchool());

        // メッセージ処理
        if (resultList == null || resultList.isEmpty() || student == null) {
            req.setAttribute("message", "該当する成績情報が見つかりませんでした。");
        } else {
            req.setAttribute("testListStudent", resultList);
            req.setAttribute("studentName", student.getName());

        }

        // ▼ JSPに渡す
        req.setAttribute("studentName",student.getName());
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("class_num_set", classNumList);
        req.setAttribute("subject_set", subjectList);


        // 確認用
//        for(TestListStudent stu:resultList){
//        	System.out.println("科目名 : "+stu.getSubjectName()+"\n得点 : "+stu.getPoint());
//        }
//        System.out.println(resultList2);

        // 表示用
        req.setAttribute("studentNo", studentNoStr);
//        System.out.println(resultList.size());

        // JSPへフォワード
        req.getRequestDispatcher("test_list_student.jsp").forward(req, res);
    }

    // ★ 補助メソッド（subjectListが使えない場合）
    private String getSubjectName(String cd, List<Subject> subjectList) {
        for (Subject s : subjectList) {
            if (s.getCd().equals(cd)) return s.getName();
        }
        return "";
    }
}

