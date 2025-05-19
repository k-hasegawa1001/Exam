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

		// 空の値を設定
		String inputClassNum = null;
		String oldClassNumStr=null;

		// パラメータ
		inputClassNum=req.getParameter("classcd");
		oldClassNumStr=req.getParameter("old_classCd");

		// oldClassNumStr（文字列）をoldClassNum（ClassNum型）に変換
		ClassNum oldClassNum = classDao.get(oldClassNumStr, user.getSchool());
		System.out.println(oldClassNum);

		// デバッグ
		System.out.println(oldClassNumStr);
		System.out.println(inputClassNum);


		// 文字が未入力だった場合のエラー処理
		if(inputClassNum.equals(null)) {
			// クラス番号未入力
			errors.put("errors_notEntry_cd", "クラス番号を入力してください。");
			req.setAttribute("errors", errors);
//			req.setAttribute("classErrors", inputClassNum);
			req.getRequestDispatcher("ClassNumUpdate.action").forward(req, res);
		}

		// 重複エラー検知
		if (classDao.get(inputClassNum, user.getSchool()) != null){
			// 既に該当するクラス番号が存在していた場合のエラー処理
			errors.put("error_duplication_cd", "学校コードが重複しています。");
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("class_update.jsp").forward(req, res);
			return;
		}

		// 完了ページ
		if(classDao.save(oldClassNum,inputClassNum)){
			req.getRequestDispatcher("class_update_done.jsp").forward(req, res);
//		}else{
//
//			/////
//			errors.put("errors_duplication_cd", "クラス番号が重複しています。");
//			req.setAttribute("errors", errors);
//			req.getRequestDispatcher("ClassNumUpdate.action?Cd="+oldClassNumStr).forward(req, res);
		}

	}
}
