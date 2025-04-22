// 科目一覧
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
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
///////////////////////////////////////////////////////////////////////////////// コードレビュー user
		Teacher user=this.getUserFromSession(req, res);

        // 処理層

        SubjectDao suDao = new SubjectDao();

        // Subjectオブジェクトのリストを取得
        List<Subject> subjectList = suDao.filter(user.getSchool());

        ///確認用
        for(Subject sub:subjectList){
        	System.out.println(sub.getCd());
        }

        // 科目をリストにセット
        ///////////////////////////////////////////////////////////////////////////////// コードレビュー subjectlist
        req.setAttribute("subjectList", subjectList);

        req.getRequestDispatcher("subject_list.jsp").forward(req, res);
    }
}