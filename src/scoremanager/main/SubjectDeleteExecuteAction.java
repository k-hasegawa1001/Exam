// 科目削除
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectDeleteExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req, res);

		// インスタンス化
		Subject subject = new Subject();
		SubjectDao subDao = new SubjectDao();

		// 空の値を設定
		String inputCd=null;
		String inputName=null;

		// パラメータ取得
		inputCd=req.getParameter("cd");
		inputName=req.getParameter("name");

		// subjectにセット
		subject.setCd(inputCd);
		subject.setName(inputName);
		subject.setSchool(user.getSchool());


		// 削除成功時にフォワード
		////////////// 参照制約によるエラーメッセージを表示するページを追加作成する必要あり
		if (subDao.delete(subject)) {
			req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);
		}

	}
}
