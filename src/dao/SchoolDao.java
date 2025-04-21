package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.School;

public class SchoolDao extends Dao {
	/*
	 * get：学校コードを指定して学校インスタンスを一件取得
	 *
	 * @param cd:string
	 * 			学校コード
	 *
	 * @return 学校クラスのインスタンス 存在しない場合はnull
	 * @throws Exception
	 * */
	public School get(String cd) throws Exception {
		School school = new School();

		Connection con = getConnection();
		PreparedStatement st = null;
		try{
			st = con.prepareCall("select * from school where cd=?");
			st.setString(1, cd);
			ResultSet rs = st.executeQuery();

			if(rs.next()){
				school.setCd(rs.getString("cd"));
				school.setName(rs.getString("name"));
			}else{
				school = null;
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

		 return school;
	}

}
