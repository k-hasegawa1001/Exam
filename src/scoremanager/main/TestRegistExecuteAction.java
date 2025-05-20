package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // ユーザー情報取得
        Teacher user = this.getUserFromSession(req, res);

        // パラメータ取得
        String entYearStr = req.getParameter("ent_year");
        String classNum = req.getParameter("class_num");
        String subjectCd = req.getParameter("subject");
        String roundStr = req.getParameter("round");

        String[] studentNos = req.getParameterValues("student_no");
        String[] scores = req.getParameterValues("score");

        boolean errorFlg=false;

        int entYear = entYearStr != null && !entYearStr.isEmpty() ? Integer.parseInt(entYearStr) : 0;
        int round = roundStr != null && !roundStr.isEmpty() ? Integer.parseInt(roundStr) : 0;

//        Map<String, String> errors = new HashMap<>();
        List<String> errorStudentList = new ArrayList<>();

        // 登録データ作成
        List<Test> testList = new ArrayList<>();
        for (int i = 0; i < studentNos.length; i++) {
            Test test = new Test();

            // Student情報
            Student student = new Student();
            student.setNo(studentNos[i]);
            student.setEntYear(entYear);
            student.setClassNum(classNum);
            student.setSchool(user.getSchool());
            test.setStudent(student);

            // Subject情報
            Subject subject = new Subject();
            subject.setCd(subjectCd);
            test.setSubject(subject);

            test.setNo(round);
            test.setSchool(user.getSchool());
            test.setClassNum(classNum);
            // バリデーション
            if (Integer.parseInt(scores[i])<0 || Integer.parseInt(scores[i])>100) {
//            	errors.put("error"+studentNos[i], "0～100の範囲で入力してください");
//            	System.out.println("error"+studentNos[i]);
            	errorStudentList.add(studentNos[i]);
            	errorFlg=true;
            }
            try {
                test.setPoint(Integer.parseInt(scores[i]));
            } catch (NumberFormatException e) {
                test.setPoint(0);
            }
            testList.add(test);
        }

        boolean success = false;

        // DB保存
        TestDao testDao = new TestDao();
        if(!errorFlg){
        	success = testDao.save(testList);
        }

        if (success) {
            req.setAttribute("successMessage", "成績を登録しました。");
            // 結果画面へフォワード（必要ならtest_regist_done.jspや完了画面に変更）
            req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
        } else {
        	req.setAttribute("ent_year", entYearStr);
        	req.setAttribute("class_num", classNum);
        	req.setAttribute("subject", subjectCd);
        	req.setAttribute("round", roundStr);

        	System.out.println();
            req.setAttribute("errorMessage", "成績の登録に失敗しました。");
            req.setAttribute("errorStudents", errorStudentList);
            req.getRequestDispatcher("TestRegist.action").forward(req, res);
        }



    }
}