// 科目更新
package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		@SuppressWarnings("unused")
		Teacher user = this.getUserFromSession(req, res);

		// DaoとBeanのインスタンス化
		SubjectDao subDao = new SubjectDao();
		Subject subject = new Subject();

		// Map
		Map<String,String> errors = new HashMap<>();

		// subject初期化
		subject = null;

		// 画面遷移前から学校コードの取得
		String schoolCd = req.getParameter("cd");

		// subjectテーブルからデータの取得
		subject = subDao.get(schoolCd, user.getSchool());

		// 科目が存在しない場合
		if(subject == null){
			// 該当する学校コードが存在しない場合
			errors.put("erros_notFound_cd", "科目が存在していません。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("SubjectUpdate.action").forward(req, res);
		}

		// セット
		req.setAttribute("now_cd", subject.getCd());
		req.setAttribute("now_name", subject.getName());

		// フォワード
		req.getRequestDispatcher("subject_update.jsp").forward(req, res);
	}
}
