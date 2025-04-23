<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>
		<form method="post" action="SubjectUpdateExecute.action">
			<div class="mb-3">
				<label class="form-label" for="cd">科目コード</label>
				<input class="form-control mb-3" id="cd" name="cd" value="${now_cd }" readonly>
				<c:if test="${errors.get('errors_notFound_Cd') != null }">
					<p class="text-warning mb-3">${errors.get("errors_notFound_cd") }</p>
				</c:if>
				<label class="form-label" for="name">科目名</label>
				<input class="form-control mb-3" id="name" name="name" value="${now_name }" required>
			</div>
			<button class="btn btn-primary mb-3" name="update">変更</button>
		</form>
		<a href="SubjectList.action">戻る</a>
	</c:param>
</c:import>