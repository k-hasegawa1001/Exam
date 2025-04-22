// 科目登録
package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectCreateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req, res);
		SubjectDao subDao = new SubjectDao();
		Subject subBean = new Subject();

		Map<String,String> errors = new HashMap<>();

		// 入力値を格納
		String cdStr = req.getParameter("cd");
		String nameStr = req.getParameter("name");



		// 文字入力が未入力
		if (cdStr.equals(null)){
			// 学校コード未入力
			errors.put("error_school_cd", "このフィールドを入力してください。");
			req.setAttribute("errors", errors);
			req.setAttribute("error_name", nameStr);
			req.setAttribute("error_cd", cdStr);

			req.getRequestDispatcher("SubjectCreate.action").forward(req, res);
		}

		// 重複エラー検知
		if(subDao.get(cdStr, user.getSchool()) != null){
			// すでに該当する学校コードが存在していた場合のエラー処理
			errors.put("erros_duplication_cd", "学校コードが重複しています。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("SubjectCreate.action").forward(req, res);
		}



		// subjectにデータをセット
		subBean.setCd(cdStr);
		subBean.setName(nameStr);
		subBean.setSchool(user.getSchool());


		// 完了ページ
		if(subDao.save(subBean)){
			req.getRequestDispatcher("student_create_done.jsp").forward(req, res);
		}
	}
}
