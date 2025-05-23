package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;


public class StudentListAction extends Action {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 10;

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{

		Teacher user = this.getUserFromSession(req, res);

		String entYearStr=""; // 入力された入学年度
		String classNum=""; // 入力されたクラス番号
		String isAttendStr=""; // 入力された在学フラグ
		int entYear = 0;
		boolean isAttend=false;
		List<Student> students=null;
		LocalDate todayDate=LocalDate.now();
		int year =todayDate.getYear();
		StudentDao sDao=new StudentDao();
		ClassNumDao cNumDao=new ClassNumDao();
		Map<String,String> errors=new HashMap<>();

		entYearStr=req.getParameter("f1");
		classNum=req.getParameter("f2");
		isAttendStr=req.getParameter("f3");

		if(entYearStr != null){
			entYear=Integer.parseInt(entYearStr);
		}
		List<Integer> entYearSet=new ArrayList<>();
		// 10年前から1年後まで年をリストに追加
		for(int i=year-10;i<year+1;i++){
			entYearSet.add(i);
		}

		List<String> list = cNumDao.filter(user.getSchool());

		// 在学フラグが送信されていた場合
		if(isAttendStr != null){
			// 在学フラグを立てる
			isAttend=true;
			// リクエストに在学フラグをセット
			req.setAttribute("f3", isAttend);
		}

		if(entYear != 0 && !classNum.equals("0")){
			// 入学年度とクラス番号を指定
			students=sDao.filter(user.getSchool(), entYear, classNum, isAttend);
		}else if(entYear != 0 && classNum.equals("0")){
			// 入学年度のみ指定
			students=sDao.filter(user.getSchool(), entYear, isAttend);
		}else if(entYear == 0 && classNum == null || entYear == 0 && classNum.equals("0")){
			// 指定なし
			// 全学生情報を取得
			students = sDao.filter(user.getSchool(), isAttend);
		}else{
			errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
			req.setAttribute("errors", errors);
			// 全学生情報を取得
			students = sDao.filter(user.getSchool(), isAttend);
		}


		// ページネーション処理
		int totalStudents = sDao.count(user.getSchool());

        // ページ数計算（必ず1ページ以上にする）
        int totalPages = (int) Math.ceil((double) totalStudents / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        // ページ番号取得
        int page = 1;
        if (req.getParameter("page") != null) {
            try {
                page = Integer.parseInt(req.getParameter("page"));
                if (page < 1) page = 1;
                if (page > totalPages) page = totalPages;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int offset = (page - 1) * PAGE_SIZE;
        List<Student> student_page = sDao.findByPage(user.getSchool(), offset, PAGE_SIZE);

		// レスポンス値をセット
		// リクエストに入学年度をセット
		req.setAttribute("f1", entYear);
		// クラス番号セット
		req.setAttribute("f2", classNum);

		// リクエストに学生リストをセット
		req.setAttribute("students", students);
		// リクエストにデータをセット
		req.setAttribute("class_num_set", list);
		req.setAttribute("ent_year_set", entYearSet);

		// ページネーション
        req.setAttribute("students", student_page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("paginationBaseUrl", "StudentList.action");

		// jspへフォワード
		req.getRequestDispatcher("student_list.jsp").forward(req, res);
	}
}
