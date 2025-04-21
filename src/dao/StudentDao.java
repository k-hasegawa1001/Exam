package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

public class StudentDao extends Dao {
	private String baseSql = "select * from student where school_cd=?";

	public Student get(String no) throws Exception{
		// 学生番号から生徒情報を一件のみ取得
		Student student = new Student();

		Connection con = getConnection();
		PreparedStatement st = null;

		try{
			st = con.prepareStatement("select * from student where no=?");
			st.setString(1, no);
			ResultSet rs = st.executeQuery();

			SchoolDao schoolDao = new SchoolDao();

			if(rs.next()){
				student.setNo(rs.getString("no"));
				student.setName(rs.getString("name"));
				student.setEntYear(rs.getInt("ent_year"));
				student.setClassNum(rs.getString("class_num"));
				student.setAttend(rs.getBoolean("is_attend"));
				student.setSchool(schoolDao.get(rs.getString("school_cd")));
			}else{
				student = null;
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

		return student;
	}

	private List<Student> postFilter(ResultSet rSet, School school) throws Exception{
		// リストを初期化
		List<Student> list = new ArrayList<>();
		try{
			// リザルトセットを全権操作
			while(rSet.next()){
				// 学生インスタンスを初期化
				Student student = new Student();
				// 学生インスタンスに検索結果をセット
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(school);
				// リストに追加
				list.add(student);
			}
		}catch(SQLException | NullPointerException e){
			e.printStackTrace();
		}

		return list;
	}

	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception{
		// リストを初期化
		List<Student> list = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		// 条件
		String condition = "and ent_year=? and class_num=?";
		// ソート
		String order = " order by no asc";

		// 在学フラグ
		String conditionIsAttend = "";
		// 在学フラグがtrue
		if (isAttend){
			conditionIsAttend = " and is_attend=true";
		}

		try{
			st = con.prepareStatement(baseSql + condition + conditionIsAttend + order);
			// バインド
			st.setString(1, school.getCd());
			st.setInt(2, entYear);
			st.setString(3, classNum);

			rs = st.executeQuery();
			list = postFilter(rs, school);
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

	public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception{
		// リストを初期化
		List<Student> list = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		// 条件
		String condition = "and ent_year=?";
		// ソート
		String order = " order by no asc";

		// 在学フラグ
		String conditionIsAttend = "";
		// 在学フラグがtrue
		if (isAttend){
			conditionIsAttend = " and is_attend=true";
		}

		try{
			st = con.prepareStatement(baseSql + condition + conditionIsAttend + order);
			// バインド
			st.setString(1, school.getCd());
			st.setInt(2, entYear);

			rs = st.executeQuery();
			list = postFilter(rs, school);
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

	public List<Student> filter(School school, boolean isAttend) throws Exception{
		// リストを初期化
		List<Student> list = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		// ソート
		String order = " order by no asc";

		// 在学フラグ
		String conditionIsAttend = "";
		// 在学フラグがtrue
		if (isAttend){
			conditionIsAttend = " and is_attend=true";
		}

		try{
			st = con.prepareStatement(baseSql + conditionIsAttend + order);
			// バインド
			st.setString(1, school.getCd());

			rs = st.executeQuery();
			list = postFilter(rs, school);
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

	public boolean save(Student student) throws Exception{
		// 登録と更新共通
		Connection con = getConnection();
		PreparedStatement st = null;

		// 実行件数
		int count = 0;

		try{
			Student old = get(student.getNo());
			if(old == null){
				// 学生が存在しなかった場合
				st = con.prepareStatement(
						"insert into student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ?, ?, ?, ?)");
				// バインド
				st.setString(1, student.getNo());
				st.setString(2, student.getName());
				st.setInt(3, student.getEntYear());
				st.setString(4, student.getClassNum());
				st.setBoolean(5, student.isAttend());
				st.setString(6, student.getSchool().getCd());
			}else{
				st = con.prepareStatement("update student set name=?, ent_year=?, class_num=?, is_attend=? where no=?");
				// バインド
				st.setString(1, student.getName());
				st.setInt(2, student.getEntYear());
				st.setString(3, student.getClassNum());
				st.setBoolean(4, student.isAttend());
				st.setString(5, student.getNo());
			}

			count = st.executeUpdate();
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

		if(count>0){
			return true;
		}else{
			return false;
		}
	}
}
