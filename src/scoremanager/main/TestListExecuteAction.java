package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Test;
import dao.SchoolDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestListExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // パラメータを取得
        int entYear = Integer.parseInt(request.getParameter("ent_year"));
        String classNum = request.getParameter("class_num");
        String subjectCd = request.getParameter("subject_cd");
        int num = Integer.parseInt(request.getParameter("no"));
        String schoolCd = request.getParameter("school_cd");

        // DAOで必要な情報取得
        SchoolDao schoolDao = new SchoolDao();
        SubjectDao subjectDao = new SubjectDao();
        TestDao testDao = new TestDao();

        School school = schoolDao.get(schoolCd);
        Subject subject = subjectDao.get(subjectCd, school);

        // 成績情報を取得
        List<Test> list = testDao.filter(entYear, classNum, subject, num, school);

        // リクエストスコープに設定
        request.setAttribute("list", list);
        request.setAttribute("ent_year", entYear);
        request.setAttribute("class_num", classNum);
        request.setAttribute("subject", subject);
        request.setAttribute("no", num);
        request.setAttribute("school", school);

        // 結果表示ページへフォワード
        request.getRequestDispatcher("test_list_result.jsp").forward(request, response);
    }
}
