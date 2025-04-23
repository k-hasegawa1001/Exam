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

		// Map
		Map<String,String> errors = new HashMap<>();


		// 空の値を設定
		String inputCd=null;
		String inputName=null;

		// パラメータ取得
		inputCd=req.getParameter("cd"); // 科目コード
		inputName=req.getParameter("name"); // 科目名

		// 科目が存在しない場合
		if(inputCd == null){
			// 該当する学校コードが存在しない場合
			errors.put("erros_notFound_cd", "科目が存在していません。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("SubjectUpdate.action").forward(req, res);
		}

		// subjectにセット
		subject.setCd(inputCd);
		subject.setName(inputName);
		subject.setSchool(user.getSchool());


		if (subDao.save(subject)) {
			req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
		}
	}
}
