package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassNum;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;


public class ClassListAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{

		Teacher user = this.getUserFromSession(req, res);


        ClassNumDao classDao = new ClassNumDao();
        ClassNum classNum = new ClassNum();
        List<ClassNum> classNumList = new ArrayList<>();

        // 学校に所属するクラス名のリストを取得
        List<String> classNames = classDao.filter(user.getSchool());

        // 取得したクラス名をClassNumオブジェクトに詰めてリストに追加
        for (String className : classNames) {

            classNum.setClassNum(className);
            classNumList.add(classNum);
            classNum.setSchool(user.getSchool());
        }

        // ClassNumオブジェクトのリストをリクエスト属性に設定
        req.setAttribute("classList", classNumList);


        req.getRequestDispatcher("class_list.jsp").forward(req, res);
    }
}