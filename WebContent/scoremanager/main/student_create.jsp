<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>

		<form method="post" action="StudentCreateExecute.action">
			<div>
				<label for="ent_year">入学年度</label>
				<select class="form-select mb-3" id="ent_year" name="ent_year">
					<option value="0">--------</option>
					<c:forEach var="year" items="${ent_year_set }">
						<option value="${year}">${year }</option>
					</c:forEach>
				</select>
				<c:if test="${errors.get('error_entYear') != null }">
					<p class="text-warning mb-3">${errors.get("error_entYear") }</p>
				</c:if>
				<label for="no">学生番号</label>
				<input type="text" id="no" name="no" class="mb-3 form-control" required value="${error_no }">
				<c:if test="${errors.get('erros_duplication_no') != null }">
					<p class="text-warning mb-3">${errors.get("erros_duplication_no") }</p>
				</c:if>
				<label for="name">氏名</label>
				<input type="text" id="name" name="name" class="mb-3 form-control" required value="${error_name }">
				<label for="class_num">クラス</label>
				<select class="form-select mb-3" id="class_num" name="class_num">
					<option value="0">--------</option>
					<c:forEach var="class_num" items="${class_num_set }">
						<option value="${class_num }" <c:if test="${class_num == error_class_num}">selected</c:if>>${class_num }</option>
					</c:forEach>
				</select>
			</div>
			<button class="btn btn-secondary mb-3" name="end">登録して終了</button>
		</form>
		<a href="#" onClick="history.back()" return false;>戻る</a>
	</c:param>
</c:import>