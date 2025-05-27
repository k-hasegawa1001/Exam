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
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Teacher user = this.getUserFromSession(req, res);

        TestListStudentDao TLStuDao = new TestListStudentDao();
        StudentDao stuDao = new StudentDao();
        ClassNumDao cDao = new ClassNumDao();
        SubjectDao sDao = new SubjectDao();

        // JSPでのform表示用：入学年度リスト作成
        List<Integer> entYearSet = new ArrayList<>();
        LocalDate todaysDate = LocalDate.now();
        int year = todaysDate.getYear();
        for (int i = year - 10; i < year + 1; i++){
            entYearSet.add(i);
        }

        // クラス番号・科目一覧取得
        List<String> classNumList = cDao.filter(user.getSchool());
        List<Subject> subjectList = sDao.filter(user.getSchool());

        // リクエストパラメータ取得
        String studentNoStr = req.getParameter("f4");
        Student student = null;
        if (studentNoStr != null && !studentNoStr.isEmpty()) {
            student = stuDao.get(studentNoStr);
        }

        // ページング準備
        String studentName = "";
        List<TestListStudent> students_page = new ArrayList<>();
        int totalStudents = 0;

        if (student != null) {
            studentName = student.getName();
            // 学生で絞り込んだ総件数
            totalStudents = TLStuDao.countForStudent(user.getSchool(), student.getNo());
        }

        int totalPages = (int) Math.ceil((double) totalStudents / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

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
        int offset = (page - 1) * PAGE_SIZE;

        if (student != null && totalStudents > 0) {
            students_page = TLStuDao.findByPageForStudent(user.getSchool(), student.getNo(), offset, PAGE_SIZE);
        }

        // メッセージ処理
        if (student == null || students_page.isEmpty()) {
            req.setAttribute("message", "該当する成績情報が見つかりませんでした。");
        }

        // JSPに渡す
        req.setAttribute("studentName", studentName);
        req.setAttribute("studentNo", studentNoStr);
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("class_num_set", classNumList);
        req.setAttribute("subject_set", subjectList);
        req.setAttribute("testListStudent", students_page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalStudents", totalStudents);
        req.setAttribute("paginationBaseUrl", "TestListStudentExecute.action");

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