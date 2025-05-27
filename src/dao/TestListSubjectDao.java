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
	// ページ用に新規追加されたコード
	// 件数取得（検索条件すべて）
	public int countForSubject(School school, int entYear, String classNum, String subjectCd) throws Exception {
	    int total = 0;
	    Connection con = getConnection();
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    try {
	        st = con.prepareStatement(
	            "SELECT COUNT(DISTINCT s.no) FROM test t " +
	            "JOIN student s ON t.student_no = s.no " +
	            "WHERE s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND s.school_cd = ?");
	        st.setInt(1, entYear);
	        st.setString(2, classNum);
	        st.setString(3, subjectCd);
	        st.setString(4, school.getCd());
	        rs = st.executeQuery();
	        if (rs.next()) total = rs.getInt(1);
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e) {}
	        if (st != null) try { st.close(); } catch (SQLException e) {}
	        if (con != null) try { con.close(); } catch (SQLException e) {}
	    }
	    return total;
	}

	// ページングも全条件で
	public List<String> findStudentNosByPage(School school, int entYear, String classNum, String subjectCd, int offset, int limit) throws Exception {
	    List<String> studentNos = new ArrayList<>();
	    Connection con = getConnection();
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    try {
	        st = con.prepareStatement(
	            "SELECT DISTINCT s.no FROM test t " +
	            "JOIN student s ON t.student_no = s.no " +
	            "WHERE s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND s.school_cd = ? " +
	            "ORDER BY s.no LIMIT ? OFFSET ?"
	        );
	        st.setInt(1, entYear);
	        st.setString(2, classNum);
	        st.setString(3, subjectCd);
	        st.setString(4, school.getCd());
	        st.setInt(5, limit);
	        st.setInt(6, offset);
	        rs = st.executeQuery();
	        while(rs.next()) {
	            studentNos.add(rs.getString("no"));
	        }
	    } finally {
	        if (rs != null) try { rs.close(); } catch (Exception e) {}
	        if (st != null) try { st.close(); } catch (Exception e) {}
	        if (con != null) try { con.close(); } catch (Exception e) {}
	    }
	    return studentNos;
	}

	// ページング用：生徒番号リストに対応する成績を取得
	public List<TestListSubject> findSubjectsByStudentNos(School school, int entYear, String classNum, String subjectCd, List<String> studentNos) throws Exception {
	    if (studentNos.isEmpty()) return new ArrayList<>();
	    List<TestListSubject> list = new ArrayList<>();
	    Connection con = getConnection();
	    PreparedStatement st = null;
	    ResultSet rs = null;
	    try {
	        // IN句の生成
	        String inClause = String.join(",", java.util.Collections.nCopies(studentNos.size(), "?"));
	        String sql =
	            "SELECT t.subject_cd, sub.name AS subject_name, t.no, t.point, s.no AS student_no, s.name AS student_name, s.ent_year, t.class_num " +
	            "FROM test t " +
	            "JOIN subject sub ON t.subject_cd = sub.cd " +
	            "JOIN student s ON t.student_no = s.no " +
	            "WHERE s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND s.school_cd = ? " +
	            "AND s.no IN (" + inClause + ") " +
	            "ORDER BY s.no, t.no";
	        st = con.prepareStatement(sql);
	        st.setInt(1, entYear);
	        st.setString(2, classNum);
	        st.setString(3, subjectCd);
	        st.setString(4, school.getCd());
	        int idx = 5;
	        for (String no : studentNos) {
	            st.setString(idx++, no);
	        }
	        rs = st.executeQuery();

	        // postFilterと同じ集約処理
	        List<String> existStudentNo = new ArrayList<>();
	        while(rs.next()){
	            TestListSubject testListSubject= new TestListSubject();
	            if(existStudentNo.contains(rs.getString("student_no"))){
	                for(TestListSubject testSub : list){
	                    if(testSub.getStudentNo().equals(rs.getString("student_no"))){
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
	    } finally {
	        if (rs != null) try { rs.close(); } catch (Exception e) {}
	        if (st != null) try { st.close(); } catch (Exception e) {}
	        if (con != null) try { con.close(); } catch (Exception e) {}
	    }
	    return list;
	}
}