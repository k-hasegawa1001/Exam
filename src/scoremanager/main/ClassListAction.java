package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassListAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        Teacher user = this.getUserFromSession(req, res);

        // 一覧表示を行う為のSQL
        ClassNumDao classNumDao = new ClassNumDao();

        // 全クラス番号リスト取得
        List<String> classNumList = classNumDao.filter(user.getSchool());

        // ページネーション処理
        int pageSize = 10; // 1ページあたりの表示件数
        int totalItems = classNumList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // pageパラメータ取得（なければ1ページ目）
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

        // 表示する範囲の計算
        int startIdx = (currentPage - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, totalItems);

        // 現在ページに表示するClassNumのリスト作成
        List<ClassNum> list = new ArrayList<>();
        for (int i = startIdx; i < endIdx; i++) {
            String classNum = classNumList.get(i);
            ClassNum cNum = classNumDao.get(classNum, user.getSchool());
            list.add(cNum);
        }

        // JSPに渡す値
        req.setAttribute("classList", list);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("paginationBaseUrl", "ClassList.action"); // 基本URL
        req.setAttribute("totalItems", totalItems); // ← 最大件数（全体件数）

        req.getRequestDispatcher("class_list.jsp").forward(req, res);
    }
}