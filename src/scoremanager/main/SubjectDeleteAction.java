// 科目削除
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectDeleteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req, res);

		Subject subject = new Subject();
		SubjectDao subDao = new SubjectDao();


		// 画面遷移前から学校コードの取得
		String subjectCd = req.getParameter("subCd");
		subject = subDao.get(subjectCd, user.getSchool());


		// セット
		req.setAttribute("subject_cd", subject.getCd());
		req.setAttribute("subject_name", subject.getName());

		req.getRequestDispatcher("subject_delete.jsp").forward(req, res);
	}
}
