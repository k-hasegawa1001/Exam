package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {
	public ClassNum get(String class_num, School school) throws Exception{
		// 一件取得
		ClassNum classNum = new ClassNum();

		Connection con = getConnection();
		PreparedStatement st = null;

		try{
			st = con.prepareStatement("select * from class_num where class_num=? and school_cd=?");
			st.setString(1, class_num);
			st.setString(2, school.getCd());

			ResultSet rs = st.executeQuery();

			SchoolDao schoolDao = new SchoolDao();
			if(rs.next()){
				classNum.setClassNum(rs.getString("class_num"));
				classNum.setSchool(schoolDao.get(rs.getString("school_cd")));
			}else{
				classNum = null;
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

		return classNum;
	}

	public List<String> filter(School school) throws Exception{
		// ユーザ所属学校のクラス一覧を取得
		// リストを初期化
		List<String> list = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;

		try{
			st = con.prepareStatement("select class_num from class_num where school_cd=? order by class_num");
			// バインド
			st.setString(1, school.getCd());

			ResultSet rs = st.executeQuery();

			while(rs.next()){
				list.add(rs.getString("class_num"));
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

	public boolean save(ClassNum classNum) throws Exception{
		// 新規登録
		Connection con = getConnection();
		PreparedStatement st = null;

		// 実行件数
		int cnt = 0;

		try{
			st=con.prepareStatement("insert into class_num(school_cd, class_num) values(?, ?)");
			st.setString(1, classNum.getSchool().getCd());
			st.setString(2, classNum.getClassNum());

			cnt = st.executeUpdate();
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

		if(cnt>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean save(ClassNum classNum, String newClassNum) throws Exception{
		// 更新
		Connection con = getConnection();
		PreparedStatement st = null;

		School school=classNum.getSchool();

		// 実行件数
		int cnt = 0;

		try{
			if(this.get(newClassNum, school)==null){
				ClassNum old = this.get(classNum.getClassNum(),classNum.getSchool());
				st = con.prepareStatement("update class_num set class_num=? where class_num=? and school_cd=?");
				st.setString(1, newClassNum);
				st.setString(2, old.getClassNum());
				st.setString(3, classNum.getSchool().getCd());
				cnt=st.executeUpdate();
			}else{
				// 握りつぶす
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

		if(cnt>0){
			return true;
		}else{
			return false;
		}
	}
}
