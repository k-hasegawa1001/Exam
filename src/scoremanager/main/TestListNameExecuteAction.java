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
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;

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
        List<Student> allSearchStudents = new ArrayList<>();
        List<Student> searchStudents = new ArrayList<>();
        int totalname = 0;

        // ページ番号取得
        int page = 1;
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // 検索＆ページング
        if (name != null && !name.isEmpty()) {
            allSearchStudents = testListNameDao.findByName(name, school);
            totalname = allSearchStudents.size();

            // ページング
            int totalPages = (int) Math.ceil((double) totalname / PAGE_SIZE);
            if (totalPages == 0) totalPages = 1;
            if (page > totalPages) page = totalPages;
            int offset = (page - 1) * PAGE_SIZE;

            for (int i = offset; i < Math.min(offset + PAGE_SIZE, totalname); i++) {
                searchStudents.add(allSearchStudents.get(i));
            }

            req.setAttribute("totalPages", totalPages);
            req.setAttribute("currentPage", page);
            req.setAttribute("studentName", name);
            req.setAttribute("paginationBaseUrl", "TestListNameExecute.action");
            req.setAttribute("searchTotalCount", totalname);
            if (searchStudents.isEmpty()) {
                errors.put("name", "該当する学生が見つかりませんでした");
            }
        } else {
            req.setAttribute("totalPages", 1);
            req.setAttribute("currentPage", 1);
            req.setAttribute("searchTotalCount", 0);
        }

        // 表示用セット
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("class_num_set", classNumList);
        req.setAttribute("subject_set", subjectList);
        req.setAttribute("f5", name);
        req.setAttribute("searchStudents", searchStudents);
        req.setAttribute("errors", errors);

        // JSPへフォワード
        req.getRequestDispatcher("test_list_name.jsp").forward(req, res);
    }
}