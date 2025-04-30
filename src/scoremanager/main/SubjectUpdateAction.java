// 科目更新
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{

		Teacher user = this.getUserFromSession(req, res);

		// DaoとBeanのインスタンス化
		SubjectDao subDao = new SubjectDao();
		Subject subject = new Subject();


		// 画面遷移前から学校コードの取得
		String subjectCd = req.getParameter("subCd");
		System.out.println(subjectCd);

		// subjectテーブルからデータの取得
		subject = subDao.get(subjectCd, user.getSchool());
		System.out.println(subject);

		// セット
		req.setAttribute("now_cd", subject.getCd());
		req.setAttribute("now_name", subject.getName());
		// フォワード
		req.getRequestDispatcher("subject_update.jsp").forward(req, res);
	}
}
