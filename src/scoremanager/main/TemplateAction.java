// 科目登録
package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Teacher;
import tool.Action;

public class TemplateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		// エラーの解除文
		@SuppressWarnings("unused")
		// this.getUserFromSession(req, res); を呼び出すことで、未ログイン時のurl書き換えによる不正操作を防止しているので
		// この「ローカル変数 user の値は使用されていません」と警告されても消さないこと
		//// もし上記の警告が表示され、警告を消したい場合には長谷川に相談すること
		Teacher user = this.getUserFromSession(req, res);
	}
}
