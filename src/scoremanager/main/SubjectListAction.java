// 科目一覧
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import tool.Action;

public class SubjectListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		HttpSession session=req.getSession();
		Teacher teacher=(Teacher)session.getAttribute("user");

		// ログイン前の不正アクセス防止
		if(teacher == null){
			res.sendRedirect("/exam/scoremanager/Login.action");
			return;
		}
	}
}
