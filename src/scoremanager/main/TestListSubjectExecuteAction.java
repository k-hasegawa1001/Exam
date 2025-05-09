package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    	Teacher user = this.getUserFromSession(req, res);

    	TestListSubjectDao TLSubDao = new TestListSubjectDao(); // ← 修正済み
    	SubjectDao subjectDao = new SubjectDao();
    	ClassNumDao cDao = new ClassNumDao();
    	SubjectDao sDao = new SubjectDao();
        School school = ((Teacher) req.getSession().getAttribute("user")).getSchool();

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
        String entYearStr = req.getParameter("f1");
        String classNumStr = req.getParameter("f2");
        String subjectCdStr = req.getParameter("f3");

        // 入力バリデーション
        if (entYearStr == null || entYearStr.equals("0") || classNumStr == null || classNumStr.equals("0") ||
        		subjectCdStr == null || subjectCdStr.equals("0")) {
            req.setAttribute("error", "すべての項目を入力してください。");
            // 表示用
            req.setAttribute("ent_year_set", entYearSet);
            req.setAttribute("class_num_set", classNumList);
            req.setAttribute("subject_set", subjectList);

            req.setAttribute("f1", entYearStr);
            req.setAttribute("f2", classNumStr);
            req.setAttribute("f3", sDao.get(subjectCdStr, user.getSchool()));

            req.getRequestDispatcher("test_list.jsp").forward(req, res);
            return;
        }

        int entYear = Integer.parseInt(entYearStr);
        String classNum = classNumStr;

        Subject subject = subjectDao.get(subjectCdStr, school);

        if (subject != null) {
            req.setAttribute("subjectName", subject.getName());
        } else {
            req.setAttribute("subjectName", "不明な科目");
        }

        // ログ
        System.out.println(subject);
        System.out.println("subjectCd: " + subject.getCd());

//        List<TestListStudent> resultList = TLStuDao.filter(student);
        List<TestListSubject> resultList = TLSubDao.filter(entYear, classNum, subject, school);

        // 学生名をマージ
//        if (resultList2 != null) {
//            for (TestListSubject subjectData : resultList2) {
//                for (TestListStudent studentData : resultList) {
//                    if (studentData.getStudent() != null &&
//                        subjectData.getStudentNo().equals(studentData.getStudent().getNo())) {
//                        subjectData.setStudentName(studentData.getStudent().getName());
//                        break;
//                    }
//                }
//            }
//        }


        // メッセージ処理
        if (resultList == null || resultList.isEmpty()) {
            req.setAttribute("message", "該当する成績情報が見つかりませんでした。");
        } else {
//            req.setAttribute("testList", resultList);
            req.setAttribute("testListSubject", resultList);
        }

        // ▼ JSPに渡す
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("class_num_set", classNumList);
        req.setAttribute("subject_set", subjectList);

        for(TestListSubject sub:resultList){
        	System.out.println("学生番号 : "+sub.getStudentNo()+"\n得点 : "+sub.getPoints());
        }
//        System.out.println(resultList2);

        // 表示用
        req.setAttribute("subjectName", subject.getName());
        req.setAttribute("f1", entYearStr);
        req.setAttribute("f2", classNumStr);
        req.setAttribute("f3", subjectCdStr);

//        System.out.println(resultList.size());

        // JSPへフォワード
        req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
    }
}

