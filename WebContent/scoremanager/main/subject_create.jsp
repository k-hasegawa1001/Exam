<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報登録</h2>

		<form method="post" action="SubjectCreateExecute.action">
			<div>
				<label for="cd">科目コード</label>
				<input type="text" id="cd" name="cd" class="form-control" required value="${error_cd }">
				<c:if test="${errors.get('error_invalid_length') != null }">
					<p class="text-warning">${errors.get("error_invalid_length") }</p>
				</c:if>
				<c:if test="${errors.get('error_duplication_cd') != null }">
					<p class="text-warning">${errors.get("error_duplication_cd") }</p>
				</c:if>
				<label for="name">科目名</label>
				<input type="text" id="name" name="name" class="mb-3 form-control" required value="${error_name }">
			</div>
			<button class="btn btn-primary mb-3" name="end">登録</button>
		</form>
		<a href="SubjectList.action">戻る</a>
	</c:param>
</c:import>