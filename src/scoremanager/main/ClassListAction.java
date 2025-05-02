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

        // 一覧表示を行う為のSQL

        ClassNumDao classNumDao = new ClassNumDao();

        // ClassNum の Bean

        ClassNum classnum = new ClassNum();

        // classNumのリスト化
        List<String> classNumList = new ArrayList<>();

		classNumList = classNumDao.filter(user.getSchool());

		// jspファイルに送信するデータ
		List<ClassNum> list = new ArrayList<>();

        // データを一つずつ取り出し保存
        for (String classNum : classNumList) {
        	ClassNum cNum = new ClassNum();
        	cNum = classNumDao.get(classNum, user.getSchool());
        	list.add(cNum);
        }

        req.setAttribute("classList", list);

        req.getRequestDispatcher("class_list.jsp").forward(req, res);

    }

}

