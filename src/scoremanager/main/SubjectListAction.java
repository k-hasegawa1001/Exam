package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Teacher user = this.getUserFromSession(req, res);
        SubjectDao subDao = new SubjectDao();

        int pageSize = 10;
        int totalItems = subDao.count(user.getSchool());
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        String pageParam = req.getParameter("page");
        int currentPage = 1;
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
                if (currentPage > totalPages) currentPage = totalPages;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        List<Subject> subjectList = subDao.filterPaginated(user.getSchool(), currentPage, pageSize);

        req.setAttribute("subjectList", subjectList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("paginationBaseUrl", "SubjectList.action");
        req.setAttribute("totalItems", totalItems);

        req.getRequestDispatcher("subject_list.jsp").forward(req, res);
    }
}