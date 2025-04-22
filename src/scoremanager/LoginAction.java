package scoremanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

public class LoginAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res){
		try{
			req.getRequestDispatcher("login.jsp").forward(req, res);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
// commit test
// commit pull
// test