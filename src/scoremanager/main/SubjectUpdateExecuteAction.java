//科目更新
package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req, res);


		// インスタンス化
		Subject subject = new Subject();
		SubjectDao subDao = new SubjectDao();

		// 空の値を設定
		String inputCd=null;
		String inputName=null;

		String oldSubjectCd = req.getParameter("cd");

		// パラメータ取得
		inputCd=req.getParameter("cd"); // 科目コード
		inputName=req.getParameter("name"); // 科目名


		// Map
		Map<String,String> errors = new HashMap<>();



		if (subject.getCd() != null){
			// エラー記述
			errors.put("errors_notFound_cd", "科目が存在していません。");
		    req.setAttribute("errors", errors);
			req.getRequestDispatcher("subject_update.jsp").forward(req, res);
			return;
		}
		if (inputCd == null){
			req.getRequestDispatcher("SubjectUpdateError.action").forward(req, res);
		}

		// subjectにセット
		subject.setCd(inputCd);
		subject.setName(inputName);
		subject.setSchool(user.getSchool());

		System.out.println(oldSubjectCd);

		Subject old = subDao.get(oldSubjectCd, user.getSchool());

		if (!(subDao.save(old,subject))){
		req.getRequestDispatcher("SubjectUpdateError.action").forward(req, res);
		}else {
			req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
		}
	}
}
