// 科目エラー
package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import tool.Action;

public class SubjectUpdateErrorAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		Teacher user = this.getUserFromSession(req, res);

		// Map
		Map<String,String> errors = new HashMap<>();
		errors.put("error_notFound_cd", "科目コードが存在していない状態で追加しようとしています。");
		req.setAttribute("error_notFound_cd", errors);

		req.getRequestDispatcher("subject_update_error.jsp").forward(req, res);
	}
}
