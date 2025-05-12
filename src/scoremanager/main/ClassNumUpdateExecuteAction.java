package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

public class ClassNumUpdateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{

		Teacher user = this.getUserFromSession(req, res);

		ClassNumDao classDao = new ClassNumDao();
//		ClassNum classnum = new ClassNum();


		Map<String,String> errors = new HashMap<>();

		String inputclassNum = null;
		String oldClassNumStr=null;

		// パラメータ
		inputclassNum=req.getParameter("classcd");
		oldClassNumStr=req.getParameter("old_classCd");

		// oldClassNumStr（文字列）をoldClassNum（ClassNum型）に変換
		ClassNum oldClassNum = classDao.get(oldClassNumStr, user.getSchool());

		// デバッグ
		System.out.println(oldClassNumStr);
		System.out.println(inputclassNum);


		// エラー表示
		if(inputclassNum == null) {
			errors.put("errors_notEntry_cd", "クラス番号を入力してください。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("ClassNumUpdate.action").forward(req, res);
		}



		// セット
//		classnum.setClassNum(inputclassNum);
//		classnum.setSchool(user.getSchool());




		if(classDao.save(oldClassNum,inputclassNum)){
			req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
		}else{

			/////
			errors.put("errors_duplication_cd", "クラス番号が重複しています。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("ClassNumUpdate.action?Cd="+oldClassNumStr).forward(req, res);
		}

	}
}
