package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {
	// 入学年度と学校コードのみから生徒の成績情報を取得するためのsql
//	private String baseSql = "select student_no,subject_cd, test.school_cd,test.no, point,test.class_num,name,ent_year,is_attend "
//							+ "from test join student "
//							+ "on test.student_no=student.no "
//							+ "where ent_year=? and test.school_cd=? ";

	private String baseSql =
		"SELECT t.subject_cd, sub.name AS subject_name, t.no, t.point, s.no AS student_no, s.name AS student_name, s.ent_year as ent_year, t.class_num as class_num " +
        "FROM test t " +
        "JOIN subject sub ON t.subject_cd = sub.cd " +
        "JOIN student s ON t.student_no = s.no " +
        "WHERE s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND s.school_cd = ?" +
        "ORDER BY student_no";


//	private String baseSql2 = "select * from test";

	private List<TestListSubject> postFilter(ResultSet rs) throws Exception{
		List<TestListSubject> list = new ArrayList<>();
		List<String> existStudentNo = new ArrayList<>();

		while(rs.next()){
			System.out.println("test");
			TestListSubject testListSubject= new TestListSubject();

			if(existStudentNo.contains(rs.getString("student_no"))){
				for(TestListSubject testSub : list){
					if(testSub.getStudentNo()==rs.getString("student_no")){
						testSub.putPoint(rs.getInt("no"), rs.getInt("point"));
					}
				}
			}else{
				testListSubject.setEntYear(rs.getInt("ent_year"));
				testListSubject.setClassNum(rs.getString("class_num"));
				testListSubject.setStudentNo(rs.getString("student_no"));
				testListSubject.setStudentName(rs.getString("student_name"));
				testListSubject.putPoint(rs.getInt("no"), rs.getInt("point"));
				list.add(testListSubject);
			}
			existStudentNo.add(rs.getString("student_no"));
		}

		return list;
	}

	// 入力された条件に合致する生徒の一覧をstudent表から取得
	// 生徒ごとにfor文で回して成績を取得してpostFilterを通す
	public List<TestListSubject> filter(int entYear, String classNum, Subject subject, School school) throws Exception{
		List<TestListSubject> list = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

//		System.out.println(school.getCd());
//		String condition = "and t.class_num=? and t.subject_cd=? ";

		try{
			st=con.prepareStatement(baseSql);
			st.setInt(1, entYear);
			st.setString(2, classNum);
			st.setString(3, subject.getCd());
			st.setString(4, school.getCd());
			rs=st.executeQuery();

			list=this.postFilter(rs);
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
}
