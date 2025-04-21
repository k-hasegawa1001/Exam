/*
 * 仕様書からの変更点
 * セッションからユーザデータを取得し、未ログイン時の不正操作をさせないためのヘルパー関数getUserFromSession()を追加
 * 		→LoginExecuteAction以外でHttpSessionクラスを使うことがほぼないのにいちいちインポートするのが非効率的だと感じたから
 * */
package tool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;

public abstract class Action {
	public abstract void execute(
			HttpServletRequest request, HttpServletResponse response
		)throws Exception;

	////////////////// 仕様書から追加のヘルパー関数
	public Teacher getUserFromSession(HttpServletRequest req, HttpServletResponse res) throws Exception{
		// セッションからユーザデータを取得し、もし未ログインならログインページにリダイレクトするためのヘルパー関数
		HttpSession session = req.getSession();
		Teacher user = (Teacher)session.getAttribute("user");
		if(user==null){
			// 未ログイン時
			res.sendRedirect("/exam/scoremanager/Login.action");
		}
		return user;
	}
}
