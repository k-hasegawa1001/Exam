package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Teacher;

public class TeacherDao extends Dao {
	public Teacher get(String id) throws Exception{
		Teacher teacher = new Teacher();

		return teacher;
	}

	public Teacher login(String id, String password) throws Exception{
		Teacher teacher = new Teacher();
		SchoolDao schoolDao = new SchoolDao();

		Connection con = getConnection();
		PreparedStatement st = null;
		try{
			st = con.prepareStatement("select * from teacher where id=? and password=?");
			st.setString(1, id);
			st.setString(2, password);

			ResultSet rs = st.executeQuery();

			if(rs.next()){
				// ログイン
				teacher.setId(rs.getString("id"));
				teacher.setPassword(rs.getString("password"));
				teacher.setName(rs.getString("name"));
				teacher.setSchool(schoolDao.get(rs.getString("school_cd")));
			}else{
				// ログイン失敗
				teacher = null;
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

		return teacher;
	}
}
