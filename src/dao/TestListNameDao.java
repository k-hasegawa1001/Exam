package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.TestListStudent;

public class TestListNameDao extends Dao {
	// 入力された名前を検索
	public List<Student> findByName(String name, School school) throws Exception {
	    List<Student> list = new ArrayList<>();
	    Connection con = getConnection();
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    try {
	        // 部分一致検索 (例: "山田" → %山田%)
	        String sql = "SELECT * FROM student WHERE name LIKE ? AND school_cd = ?";
	        st = con.prepareStatement(sql);
	        st.setString(1, "%" + name + "%"); // 部分一致
	        st.setString(2, school.getCd());
	        rs = st.executeQuery();
	        while (rs.next()) {
	            Student student = new Student();
	            student.setNo(rs.getString("no"));
	            student.setName(rs.getString("name"));
	            student.setEntYear(rs.getInt("ent_year"));
	            student.setClassNum(rs.getString("class_num"));
	            // 必要な項目をセット
	            list.add(student);
	        }
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
	        if (st != null) try { st.close(); } catch (SQLException ignore) {}
	        if (con != null) try { con.close(); } catch (SQLException ignore) {}
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

    private List<TestListStudent> postFilter(ResultSet rs, School school) throws SQLException {
        List<TestListStudent> list = new ArrayList<>();
        while (rs.next()) {
            TestListStudent student = new TestListStudent();
            list.add(student);
        }
        return list;
    }
}
