package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // ユーザー情報をセッションから取得
        Teacher user = this.getUserFromSession(req, res);


        // パラメータ取得
        String entYearStr = req.getParameter("ent_year");
        String classNum = req.getParameter("class_num");
        String subjectCd = req.getParameter("subject");
        String countStr = req.getParameter("round");

        // 入力値変換
        int entYear = entYearStr != null && !entYearStr.isEmpty() ? Integer.parseInt(entYearStr) : 0;
        int count = countStr != null && !countStr.isEmpty() ? Integer.parseInt(countStr) : 0;


        // 年度リスト作成
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year + 1; i++) {
            entYearSet.add(i);
        }

        // 回数リスト作成（1～2回）
        List<Integer> countSet = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            countSet.add(i);
        }

        // クラス・科目リスト取得
        ClassNumDao cNumDao = new ClassNumDao();
        SubjectDao sDao = new SubjectDao();

        List<String> classList = cNumDao.filter(user.getSchool());
        List<Subject> subjectList = sDao.filter(user.getSchool());

        // TestDao と StudentDao を使ってデータ取得
        TestDao testDao = new TestDao();
        StudentDao studentDao = new StudentDao();

        // 科目名をセットするための変数
        String subjectName = "";

        // 検索条件がすべて揃っているか確認
        boolean isSearched = entYear > 0 && classNum != null && !classNum.isEmpty()
                && subjectCd != null && !subjectCd.isEmpty() && count > 0;

        List<Test> tests = new ArrayList<>(); // 初期化
        List<Student> students = new ArrayList<>(); // 学生リスト

        if (isSearched) {
            try {
                // 学生データを取得
            	students = studentDao.filter(user.getSchool(), entYear, true);
                if (students.isEmpty()) {
                    System.out.println("該当する学生データが見つかりません。");
                }

                // テストデータを取得
                tests = testDao.filter(entYear, classNum, subjectCd, count, user.getSchool());
                if (tests.isEmpty()) {
                    System.out.println("該当するテストデータが見つかりません。");
                }

                // 科目コードから科目名を探す
                for (Subject s : subjectList) {
                    if (s.getCd().equals(subjectCd)) {
                        subjectName = s.getName();
                        break;
                    }
                }

                // 学生データとテストデータを統合
                List<Test> combinedTests = new ArrayList<>();
                for (Student student : students) {
                    boolean testExists = false;
                    for (Test test : tests) {
                        if (test.getStudent().getNo().equals(student.getNo())) {
                            combinedTests.add(test);
                            testExists = true;
                            break;
                        }
                    }
                    if (!testExists) {
                        Test newTest = new Test();
                        newTest.setStudent(student);
                        newTest.setSubject(new Subject());
                        newTest.setPoint(0); // 初期値
                        combinedTests.add(newTest);
                    }
                }
                tests = combinedTests;

                req.setAttribute("tests", tests);

            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("errorMessage", "データの取得中にエラーが発生しました。");
            }
        } else {
            // デバッグ: 検索条件不足
            System.out.println("検索条件が不足しています。検索は実行されませんでした。");
        }

        // リクエストスコープに値をセット
        req.setAttribute("ent_year", entYearStr);
        req.setAttribute("class_num", classNum);
        req.setAttribute("subject", subjectCd);
        req.setAttribute("subject_name", subjectName);
        req.setAttribute("round", countStr);

        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("round_set", countSet);
        req.setAttribute("class_num_set", classList);
        req.setAttribute("subject_set", subjectList);

        req.setAttribute("isSearched", isSearched);

        // JSPへフォワード
        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}