package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

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
}

