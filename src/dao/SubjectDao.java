package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {
	public Subject get(String cd, School school) throws Exception{
		// 一件取得
		Connection con = getConnection();
		PreparedStatement st = null;

		Subject subject = new Subject();

		try{
			st=con.prepareStatement("select * from subject where school_cd=? and cd=?");
			st.setString(1, school.getCd());
			st.setString(2, cd);
			ResultSet rs = st.executeQuery();

			if(rs.next()){
				subject.setCd(cd);
				subject.setName(rs.getString("name"));
				subject.setSchool(school);
			}else{
				subject = null;
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

		return subject;
	}

	public List<Subject> filter(School school) throws Exception{
		// ユーザ所属学校の科目情報を全件取得
		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Subject> list = new ArrayList<>();

		Subject subject = new Subject();

		try{
			st = con.prepareStatement("select * from subject where school_cd=?");
			st.setString(1, school.getCd());
			rs=st.executeQuery();
			while(rs.next()){
				subject.setCd(rs.getString("cd"));
				subject.setName(rs.getString("name"));
				subject.setSchool(school);
				list.add(subject);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return list;
	}

	public boolean save(Subject subject) throws Exception{
		// 新規登録 / 更新
		Connection con = getConnection();
		PreparedStatement st = null;

		// 実行件数
		int cnt = 0;

		try{
			Subject old = this.get(subject.getCd(),subject.getSchool());
			if(old == null){
				// 新規登録
				st = con.prepareStatement("insert into subject(cd, name, school_cd) values(?, ?, ?)");
				st.setString(1,	subject.getCd());
				st.setString(2, subject.getName());
				st.setString(3, subject.getSchool().getCd());
				cnt = st.executeUpdate();
			}else{
				// 更新
				st=con.prepareStatement("update subject set name=? where cd=? and school_cd=?");
				st.setString(1, subject.getName());
				st.setString(2, old.getCd());
				st.setString(3, old.getSchool().getCd());
				cnt=st.executeUpdate();
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

	public boolean delete(Subject subject) throws Exception{
		// 新規登録
		Connection con = getConnection();
		PreparedStatement st = null;

		// 実行件数
		int cnt = 0;

		try{
			Subject old = this.get(subject.getCd(),subject.getSchool());
			if(old==null){
				//@TODO エラーstudentが見つからなかった時 = DBがぶっ壊れてる可能性大
			}else{
				st=con.prepareStatement("delete from subject where cd=? and school_cd=?");
				st.setString(1, subject.getCd());
				st.setString(2, subject.getSchool().getCd());
				cnt=st.executeUpdate();
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
