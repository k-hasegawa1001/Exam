package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentListAction extends Action {
    private static final int PAGE_SIZE = 10;

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Teacher user = this.getUserFromSession(req, res);

        String entYearStr = ""; // 入力された入学年度
        String classNum = ""; // 入力されたクラス番号
        String isAttendStr = ""; // 入力された在学フラグ
        int entYear = 0;
        boolean isAttend = false;
        List<Student> students = null;
        LocalDate todayDate = LocalDate.now();
        int year = todayDate.getYear();
        StudentDao sDao = new StudentDao();
        ClassNumDao cNumDao = new ClassNumDao();
        Map<String, String> errors = new HashMap<>();

        entYearStr = req.getParameter("f1");
        classNum = req.getParameter("f2");
        isAttendStr = req.getParameter("f3");

        // 入学年度パラメータ取得と整形
        if (entYearStr != null && !entYearStr.equals("") && !entYearStr.equals("0")) {
            try {
                entYear = Integer.parseInt(entYearStr);
            } catch (NumberFormatException e) {
                entYear = 0;
            }
        }

        // 入学年度セット（10年前から翌年まで）
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i < year + 2; i++) {
            entYearSet.add(i);
        }

        // クラス番号セット
        List<String> classNumSet = cNumDao.filter(user.getSchool());

        // 在学フラグ判定
        if (isAttendStr != null && !isAttendStr.equals("")) {
            isAttend = true;
            req.setAttribute("f3", isAttend);
        }

        // 絞込条件による学生リスト取得
        if (entYear != 0 && classNum != null && !classNum.equals("0")) {
            // 入学年度とクラス番号を指定
            students = sDao.filter(user.getSchool(), entYear, classNum, isAttend);
        } else if (entYear != 0 && (classNum == null || classNum.equals("0"))) {
            // 入学年度のみ指定
            students = sDao.filter(user.getSchool(), entYear, isAttend);
        } else if ((entYear == 0 && (classNum == null || classNum.equals("0"))) || (entYear == 0 && classNum == null)) {
            // 指定なし
            students = sDao.filter(user.getSchool(), isAttend);
        } else {
            errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
            req.setAttribute("errors", errors);
            students = sDao.filter(user.getSchool(), isAttend);
        }

        // ページネーション
        int totalStudents = students.size();
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
        int toIndex = Math.min(offset + PAGE_SIZE, totalStudents);
        List<Student> studentPage = new ArrayList<>();
        if (!students.isEmpty() && offset < totalStudents) {
            studentPage = students.subList(offset, toIndex);
        }

        // リクエスト属性セット
        req.setAttribute("f1", entYearStr != null ? entYearStr : "0");
        req.setAttribute("f2", classNum != null ? classNum : "0");
        req.setAttribute("students", studentPage);
        req.setAttribute("class_num_set", classNumSet);
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("paginationBaseUrl", "StudentList.action");
        req.setAttribute("errors", errors);

        // jspへフォワード
        req.getRequestDispatcher("student_list.jsp").forward(req, res);
    }
}