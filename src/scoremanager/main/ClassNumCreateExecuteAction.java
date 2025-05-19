package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassNumCreateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{

		Teacher user = this.getUserFromSession(req, res);
		ClassNumDao classDao = new ClassNumDao();
		ClassNum classNum = new ClassNum();

		Map<String,String> errors = new HashMap<>();


		// 入力値を格納
		String cdStr = req.getParameter("classCd");

		System.out.println(cdStr);

		// 文字入力が未入力
		if (cdStr.equals(null)){
			// 学校コード未入力
			errors.put("error_schoolCd", "このフィールドを入力してください。");
			req.setAttribute("errors", errors);
			req.setAttribute("error_classCd", cdStr);

			req.getRequestDispatcher("ClassNumCreate.action").forward(req, res);
		}

		// 重複エラー検知
		if(classDao.get(cdStr, user.getSchool()) != null){
			// すでに該当する学校コードが存在していた場合のエラー処理
			errors.put("error_duplication_cd", "クラス番号が重複しています。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("ClassNumCreate.action").forward(req, res);
		}


		// classNumにデータをセット
		classNum.setClassNum(cdStr);
		classNum.setSchool(user.getSchool());


		// 完了ページ
		if(classDao.save(classNum)){
			req.getRequestDispatcher("class_create_done.jsp").forward(req, res);
		}


	}
}
