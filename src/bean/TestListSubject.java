package bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TestListSubject implements Serializable {
	private int entYear;
	private String studentNo;
	private String studentName;
	private String classNum;
	private Map<Integer,Integer> points;

	public int getEntYear() {
		return entYear;
	}
	public void setEntYear(int entYear) {
		this.entYear = entYear;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String class_num) {
		this.classNum = class_num;
	}
	public Map<Integer, Integer> getPoints() {
		return points;
	}
	public void setPoints(Map<Integer, Integer> points) {
		this.points = points;
	}
	// 点数を取得するときは以下のgetterを使う
	public int getPoint(int key) {
		Map<Integer, Integer> map = this.getPoints();
		System.out.println(key);
		int point = map.get(key);
		return point;
	}
	public void putPoint(int key, int value) {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(key, value);
		this.setPoints(map);
	}
}
