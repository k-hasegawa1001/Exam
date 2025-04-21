<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>
		<form method="post" action="StudentUpdateExecute.action">
			<div class="mb-3">
				<label class="form-label" for="ent_year">入学年度</label>
				<input class="form-control mb-3" id="ent_year" name="ent_year" value="${now_ent_year }" readonly>
				<label class="form-label" for="no">学生番号</label>
				<input class="form-control mb-3" id="no" name="no" value="${now_no }" readonly>
				<label class="form-label" for="name">氏名</label>
				<input class="form-control mb-3" id="name" name="name" value="${now_name }" required>
				<label class="form-label" for="class_num">クラス</label>
				<select class="form-select mb-3" id="class_num" name="class_num">
					<c:forEach var="class_num" items="${class_num_set }">
						<option value="${class_num }" <c:if test="${class_num == now_class_num}">selected</c:if>>${class_num }</option>
					</c:forEach>
				</select>
				<label class="form-label" for="is_attend">在学中</label>
				<input class="form-check-input" type="checkbox" id="is_attend"
					name="is_attend" value="t"
					<c:if test="${now_is_attend == true}">checked</c:if>>
			</div>
			<button class="btn btn-primary mb-3" name="update">変更</button>
		</form>
		<a href="StudentList.action">戻る</a>
	</c:param>
</c:import>