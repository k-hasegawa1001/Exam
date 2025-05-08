package scoremanager.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // ユーザー情報をセッションから取得（school_cdの取得に必要）
        Teacher teacher = this.getUserFromSession(req, res);
        School school = teacher.getSchool();

        // リクエストパラメータ取得（共通情報）
        String subjectCd = req.getParameter("subject");
        String classNum = req.getParameter("class_num");
        String countStr = req.getParameter("round");

        int count = 0;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "回数の値が不正です");
            req.getRequestDispatcher("test_regist.jsp").forward(req, res);
            return;
        }

        // テスト一覧を作成
        Map<String, String[]> parameterMap = req.getParameterMap();
        List<Test> testList = new ArrayList<>();
        TestDao testDao = new TestDao();
        StudentDao studentDao = new StudentDao();

        for (String key : parameterMap.keySet()) {
            if (key.startsWith("scores[")) {
                // キーから student_no を抽出
                String studentNo = key.substring(7, key.length() - 1);
                String value = parameterMap.get(key)[0];

                // 空文字はスキップ（未入力）
                if (value == null || value.isEmpty()) continue;

                int point;
                try {
                    point = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "点数には数値を入力してください");
                    req.getRequestDispatcher("test_regist.jsp").forward(req, res);
                    return;
                }

                // 点数の範囲チェック
                if (point < 0 || point > 100) {
                    req.setAttribute("error", "点数は0～100の範囲で入力してください");
                    req.getRequestDispatcher("test_regist.jsp").forward(req, res);
                    return;
                }

                // 各種オブジェクトを構築
                Student student = new Student();
                student.setNo(studentNo);

                Subject subject = new Subject();
                subject.setCd(subjectCd);

                Test test = new Test();
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setClassNum(classNum);
                test.setNo(count);
                test.setPoint(point);

                testList.add(test);
            }
        }

        // 成績保存処理
        if (testDao.save(testList)) {
            res.sendRedirect("test_regist_done.jsp");
        } else {
            req.setAttribute("error", "成績の保存に失敗しました");
            req.getRequestDispatcher("test_regist.jsp").forward(req, res);
        }
    }
}
