package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;
   // private static final int MAX_PAGES = 50; // ← 最大ページ数を設定

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Teacher user = this.getUserFromSession(req, res);

        TestListSubjectDao TLSubDao = new TestListSubjectDao();
        SubjectDao subjectDao = new SubjectDao();
        ClassNumDao cDao = new ClassNumDao();
        SubjectDao sDao = new SubjectDao();
        School school = user.getSchool();

        List<Integer> entYearSet = new ArrayList<>();
        LocalDate todaysDate = LocalDate.now();
        int year = todaysDate.getYear();
        for (int i = year - 10; i < year + 1; i++) entYearSet.add(i);

        List<String> classNumList = cDao.filter(school);
        List<Subject> subjectList = sDao.filter(school);

        String entYearStr = req.getParameter("f1");
        String classNumStr = req.getParameter("f2");
        String subjectCdStr = req.getParameter("f3");
        Map<String, String> errors = new HashMap<>();

        Subject subject = null;
        if (subjectCdStr != null && !subjectCdStr.isEmpty() && !"0".equals(subjectCdStr)) {
            subject = subjectDao.get(subjectCdStr, school);
        }

        // 入力バリデーション
        if (entYearStr == null || entYearStr.equals("0") ||
            classNumStr == null || classNumStr.equals("0") ||
            subjectCdStr == null || subjectCdStr.equals("0")) {

            errors.put("error_subject", "入学年度とクラスと科目を選択してください");
            req.setAttribute("errors", errors);

            req.setAttribute("ent_year_set", entYearSet);
            req.setAttribute("class_num_set", classNumList);
            req.setAttribute("subject_set", subjectList);
            req.setAttribute("f1", entYearStr);
            req.setAttribute("f2", classNumStr);
            req.setAttribute("f3", subjectCdStr);

            req.setAttribute("testListSubject", new ArrayList<TestListSubject>());
            req.setAttribute("totalPages", 1);
            req.setAttribute("currentPage", 1);
            req.setAttribute("subjectName", "");
            req.setAttribute("paginationBaseUrl", "TestListSubjectExecute.action");
            req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
            return;
        }

        int entYear = Integer.parseInt(entYearStr);
        String classNum = classNumStr;

        // 検索結果総件数
        int totalSubject = TLSubDao.countForSubject(school, entYear, classNum, subjectCdStr);
        int totalPages = (int) Math.ceil((double) totalSubject / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        //if (totalPages > MAX_PAGES) totalPages = MAX_PAGES;

        int page = 1;
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
                if (page < 1) page = 1;
                if (page > totalPages) page = totalPages;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

     // ページネーション部分を書き換え
        int offset = (page - 1) * PAGE_SIZE;
        List<String> studentNos = TLSubDao.findStudentNosByPage(school, entYear, classNum, subjectCdStr, offset, PAGE_SIZE);
        List<TestListSubject> subject_page = TLSubDao.findSubjectsByStudentNos(school, entYear, classNum, subjectCdStr, studentNos);
        if (subject_page.isEmpty()) {
            req.setAttribute("message", "該当する成績情報が見つかりませんでした。");
        }

        // JSPに渡す
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("class_num_set", classNumList);
        req.setAttribute("subject_set", subjectList);
        req.setAttribute("testListSubject", subject_page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalSubject); // ← 総件数
        req.setAttribute("currentPage", page);
        req.setAttribute("subjectName", subject != null ? subject.getName() : "");
        req.setAttribute("paginationBaseUrl", "TestListSubjectExecute.action");
        req.setAttribute("f1", entYearStr);
        req.setAttribute("f2", classNumStr);
        req.setAttribute("f3", subjectCdStr);

        req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
    }
}