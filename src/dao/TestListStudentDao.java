package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {
	// 入学年度と学校コードのみから生徒の成績情報を取得するためのsql
	private String baseSql = "select student_no,subject_cd, test.school_cd,test.no, point,test.class_num,name,ent_year,is_attend "
							+ "from test join student "
							+ "on test.student_no=student.no "
							+ "where student_no=? "
							+ "order by subject_cd, no";


	private List<TestListStudent> postFilter(ResultSet rs) throws Exception{
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

	public List<TestListStudent> filter(Student student) throws Exception{
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
