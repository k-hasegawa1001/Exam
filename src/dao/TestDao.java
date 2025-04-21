/*
 * 設計書からの変更内容
 * get()メソッドで、引数にSubject型のsubjectではなく、String型のsubjectCdを受け取るfilter()メソッドをオーバーロードを追加
 * post()メソッドで、引数にSubject型のsubjectではなく、String型のsubjectCdを受け取るfilter()メソッドをオーバーロードを追加
 *
 * */

///// testテーブルとstudentテーブルを結合するためのsql（データの重複がないようにカラムを抽出）
//select student_no,subject_cd, test.school_cd,test.no, point,test.class_num,name,ent_year,is_attend from test
//join
//student
//on
//test.student_no=student.no
//where ent_year=? and test.school_cd=?;

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {
	/*
	 * primary key:student_no(String), subject_cd(String), school_cd(String), no(int)
	 * */
	// 入学年度と学校コードのみから生徒の成績情報を取得するためのsql
	private String baseSql = "select student_no,subject_cd, test.school_cd,test.no, point,test.class_num,name,ent_year,is_attend "
							+ "from test join student "
							+ "on test.student_no=student.no "
							+ "where ent_year=? and test.school_cd=? ";

	// save()メソッド内からデータが存在するかの確認のため呼び出される
	public Test get(Student student, Subject subject, School school, int no) throws Exception{
		// 一件取得
		Test test=new Test();
		String student_no = student.getNo();
		String subject_cd = subject.getCd();
		String school_cd = school.getCd();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs= null;

		try{
			st = con.prepareStatement(baseSql);
			st.setString(1, student_no);
			st.setString(2, subject_cd);
			st.setString(3, school_cd);
			st.setInt(4, no);
			rs=st.executeQuery();
			if(rs.next()){
				test.setStudent(student);
				test.setClassNum(rs.getString("class_num"));
				test.setSubject(subject);
				test.setSchool(school);
				test.setPoint(rs.getInt("point"));
				test.setNo(no);
			}else{
				test=null;
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException sqle){
					throw sqle;
				}
			}

			if(con != null){
				try{
					con.close();
				}catch(SQLException sqle){
					throw sqle;
				}
			}
		}

		return test;
	}

//	public Test get(String studentNo, String subjectCd, String schoolCd, int no) throws Exception{
//		// 一件取得
//		Test test=new Test();
//
//		Connection con = getConnection();
//		PreparedStatement st = null;
//		ResultSet rs= null;
//
//		try{
//			st = con.prepareStatement(baseSql);
//			st.setString(1, studentNo);
//			st.setString(2, subjectCd);
//			st.setString(3, schoolCd);
//			st.setInt(4, no);
//			rs=st.executeQuery();
//			if(rs.next()){
//				test.setStudent(student);
//				test.setClassNum(rs.getString("class_num"));
//				test.setSubject(subject);
//				test.setSchool(school);
//				test.setPoint(rs.getInt("point"));
//				test.setNo(no);
//			}else{
//				test=null;
//			}
//		}catch(Exception e){
//			throw e;
//		}finally{
//			if(st != null){
//				try{
//					st.close();
//				}catch(SQLException sqle){
//					throw sqle;
//				}
//			}
//
//			if(con != null){
//				try{
//					con.close();
//				}catch(SQLException sqle){
//					throw sqle;
//				}
//			}
//		}
//
//		return test;
//	}

	//////////////////////////////////////////// ここからむずい
	// postfilter：受け取ったデータをより綺麗な状態に整えるもの（ex.音声データから少しでもノイズを除去する）
	// フロー↓↓
	/*
	 * filter()で、入力された入学年度の生徒一覧を取得
	 * 対象生徒の全成績情報をTESTテーブルから取得
	 * postFilter()を通してデータを整える（TESTテーブルには各テーブルの主キーのみが入っているので、
	 * 		postFilter()メソッドを通してStudent型、ClassNum型、...に直してTest型の配列を返す）
	 * filter()呼び出し元に整形後の配列を返す
	 **/
	private List<Test> postFilter(ResultSet rs, School school) throws Exception{
		// rs:TESTテーブルから一覧を取得し、そのデータを整える

		List<Test> list = new ArrayList<>();
		StudentDao stuDao = new StudentDao();
		SubjectDao subDao = new SubjectDao();
		try{
			Test test = new Test();
			while(rs.next()){
				test.setStudent(stuDao.get(rs.getString("student_no")));
				test.setClassNum(rs.getString("class_num"));
				test.setSubject(subDao.get(rs.getString("subject_cd"), school));
				test.setSchool(school);
				test.setPoint(rs.getInt("point"));
				test.setNo(rs.getInt("no"));
				list.add(test);
			}
		}catch(SQLException | NullPointerException e){
			e.printStackTrace();
		}

		return list;
	}

	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception{
		// 入学年度、クラス、科目、回数（、学校コード）で絞り込み
		// ↑シーケンス図、画面設計書を参照すること
		// 最終的にリターンするTest配列
		List<Test> list = new ArrayList<>();
		// listに格納するために一時的にTestインスタンスを格納しておくための変数
		Test test = new Test();

		StudentDao stuDao = new StudentDao();

		// 該当入学年度の生徒情報一覧を格納するstuList
		List<Student> stuList = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs=null;

		try{
			// 入学年度該当生徒の一覧を取得
			stuList = stuDao.filter(school, entYear, classNum, true);
			/////// 取得した生徒一覧から一件ずつ学生データを取り出して、他の入力データと組み合わせてtestテーブルから成績一覧を取得する
			for(Student student : stuList){
				test = this.get(student, subject, school, num);
				list.add(test);
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException sqle){
					throw sqle;
				}
			}

			if(con != null){
				try{
					con.close();
				}catch(SQLException sqle){
					throw sqle;
				}
			}
		}

		return list;
	}

	////////////////////////// 追加オーバーロード
	public List<Test> filter(int entYear, String classNum, String subject, int num, School school) throws Exception{
		List<Test> testList = new ArrayList<>();

		Test test = new Test();

		StudentDao stuDao = new StudentDao();

//		List<Test> testList = new ArrayList<>();

		// クラス番号、科目、回数をsqlに追加
		String sqlCondition = "and test.class_num=? and test.subject_cd=? and test.no=?";

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs=null;

		try{
			st = con.prepareStatement(baseSql+sqlCondition);
			st.setInt(1, entYear);
			st.setString(2, school.getCd());
			st.setString(3, classNum);
			st.setString(4, subject);
			st.setInt(5, num);
			rs=st.executeQuery();
			if(rs.next()){
				testList=this.postFilter(rs, school);
			}else{
				testList=null;
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException sqle){
					throw sqle;
				}
			}

			if(con != null){
				try{
					con.close();
				}catch(SQLException sqle){
					throw sqle;
				}
			}
		}

		return testList;
	}

	public boolean save(List<Test> list) throws Exception{
		return false;
	}

	private boolean save(Test test, Connection connection) throws Exception{
		// 同一クラス内のsave(List<Test> list)から呼び出される
		TestDao testDao = new TestDao();
		//
		String sqlSave = "";
		return true;
	}
}
