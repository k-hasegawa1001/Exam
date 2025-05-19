// 科目更新
package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

public class SubjectUpdateAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{

		Teacher user = this.getUserFromSession(req, res);

		// DaoとBeanのインスタンス化
		SubjectDao subDao = new SubjectDao();
		Subject subject = new Subject();

		// Map
		Map<String,String> errors = new HashMap<>();

		// 画面遷移前から学校コードの取得
		String subjectCd = req.getParameter("subCd");
		System.out.println(subjectCd);

		// subjectテーブルからデータの取得
		subject = subDao.get(subjectCd, user.getSchool());

		// デバック
		if (subject != null) {
			System.out.println(subject.getName());
		} else {
			System.out.println("科目が見つかりませんでした。");
		}

		// 科目がない場合
		if (subject == null){
		    errors.put("errors_notFound_cd", "科目が存在していません。");
		    req.setAttribute("errors", errors);
		    req.getRequestDispatcher("subject_update.jsp").forward(req, res);
		    return;
		}

		// セット
		req.setAttribute("now_cd", subject.getCd());
		req.setAttribute("now_name", subject.getName());

		// フォワード
		req.getRequestDispatcher("subject_update.jsp").forward(req, res);
	}
}
