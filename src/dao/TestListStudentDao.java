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
import bean.TestListStudent;

public class TestListStudentDao extends Dao {
	// 入学年度と学校コードのみから生徒の成績情報を取得するためのsql
	private String baseSql = "select student_no,subject_cd, test.school_cd,test.no, point,test.class_num,name,ent_year,is_attend "
							+ "from test join student "
							+ "on test.student_no=student.no "
							+ "where student_no=? and test.school_cd=?"
							+ "order by subject_cd, no";


	private List<TestListStudent> postFilter(ResultSet rs,School school) throws Exception{
		List<TestListStudent> list = new ArrayList<>();

		SubjectDao subDao = new SubjectDao();
		SchoolDao schoolDao = new SchoolDao();

		while(rs.next()){
			Subject subject = new Subject();
			subject = subDao.get(rs.getString("subject_cd"), schoolDao.get(rs.getString("school_cd")));

			TestListStudent testListStudent = new TestListStudent();
			testListStudent.setSubjectName(subject.getName());
			testListStudent.setSubjectCd(subject.getCd());
			testListStudent.setNum(rs.getInt("no"));
			testListStudent.setPoint(rs.getInt("point"));
			list.add(testListStudent);
		}

		return list;
	}

	public List<TestListStudent> filter(Student student,School school) throws Exception{
		List<TestListStudent> list = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		try{
			if(student==null){
				return list=null;
			}
			st=con.prepareStatement(baseSql);
			st.setString(1, student.getNo());
			st.setString(2, school.getCd());
			rs=st.executeQuery();
			list=this.postFilter(rs,school);
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
    /**
     * 指定した学校の学生総数を返す
     */
    public int count(School school) throws Exception {
        int total = 0;
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement("select count(*) from test where school_cd=?");
            st.setString(1, school.getCd());
            rs = st.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return total;
    }

    /**
     * 指定した学校の学生をページング取得する
     */
    public List<TestListStudent> findByPageForStudent(School school, String studentNo, int offset, int limit) throws Exception {
        List<TestListStudent> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // 学生番号と学校コードで絞り込んでページング
            st = con.prepareStatement(
              "select * from test where school_cd=? and student_no=? order by no asc limit ? offset ?"
            );
            st.setString(1, school.getCd());
            st.setString(2, studentNo);
            st.setInt(3, limit);
            st.setInt(4, offset);

            rs = st.executeQuery();
            list = postFilter(rs, school);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return list;
    }
    
    public int countForStudent(School school, String studentNo) throws Exception {
        int total = 0;
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement("select count(*) from test where school_cd=? and student_no=?");
            st.setString(1, school.getCd());
            st.setString(2, studentNo);
            rs = st.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return total;
    }
}