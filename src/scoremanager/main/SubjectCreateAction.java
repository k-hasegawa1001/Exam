// 科目登録
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import tool.Action;

public class SubjectCreateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		@SuppressWarnings("unused")
		Teacher user = this.getUserFromSession(req, res);


		req.getRequestDispatcher("subject_create.jsp").forward(req, res);
	}
}
