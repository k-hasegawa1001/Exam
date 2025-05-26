package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListNameDao;
import tool.Action;

public class TestListNameExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Teacher user = this.getUserFromSession(req, res);

        TestListNameDao testListNameDao = new TestListNameDao();
        SubjectDao subjectDao = new SubjectDao();
        ClassNumDao cDao = new ClassNumDao();
        School school = user.getSchool();

        List<Integer> entYearSet = new ArrayList<>();
        LocalDate todayDate = LocalDate.now();
        int year = todayDate.getYear();
        Map<String, String> errors = new HashMap<>();

        // 10年前から1年後まで年をリストに追加
        for (int i = year - 10; i < year + 1; i++) {
            entYearSet.add(i);
        }

        // クラス番号と科目一覧
        List<String> classNumList = cDao.filter(school);
        List<Subject> subjectList = subjectDao.filter(school);

        // パラメータ取得
        String name = req.getParameter("f5");

        // デバッグ
        System.out.println(name);
        List<Student> searchStudents = null;
        if (name != null && !name.isEmpty()) {
            searchStudents = testListNameDao.findByName(name, school);
            if (searchStudents.isEmpty()) {
                errors.put("name", "該当する学生が見つかりませんでした");
            }
        }

        // 表示用セット
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("class_num_set", classNumList);
        req.setAttribute("subject_set", subjectList);
        req.setAttribute("f5", name); // 検索欄の再表示用
        req.setAttribute("searchStudents", searchStudents);
        req.setAttribute("errors", errors);

        // JSPへフォワード
        req.getRequestDispatcher("test_list_name.jsp").forward(req, res);
    }
}