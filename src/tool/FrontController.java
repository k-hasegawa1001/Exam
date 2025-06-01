package tool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.action")
public class FrontController extends HttpServlet{

	protected void doGet(
			HttpServletRequest req,HttpServletResponse res
		)throws ServletException, IOException{
		try{
			// パスを取得
			String path = req.getServletPath().substring(1);
			String name = path.replace(".a", "A").replace('/', '.');

			//System.out.println("★ servlet path -> " + req.getServletPath().substring(1).split("/")[0]);
			System.out.println("★ servlet path -> " + req.getServletPath());
			System.out.println("★ class path -> " + name);

			// アクションクラスのインスタンスを返却
			Action action = (Action)Class.forName(name).getDeclaredConstructor().newInstance();

			action.execute(req, res);
		}catch(Exception e){
			e.printStackTrace();
			// リダイレクト：エラーページ
			req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
		res.getWriter().append("Servlet at:").append(req.getContextPath());
	}

	protected void doPost(
			HttpServletRequest request,HttpServletResponse response
		)throws ServletException, IOException{
		doGet(request, response);
	}
}
