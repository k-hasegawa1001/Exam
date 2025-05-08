<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス管理登録</h2>

		<form method="post" action="ClassNumCreateExecute.action">
			<div>
				<label for="classCd">クラスコード</label>
				<input type="text" id="classCd" name="classCd" class="mb-3 form-control" required value="${error_classCd }">
				<c:if test="${errors.get('error_schoolCd') != null }">
					<p class="text-warning mb-3">${errors.get("error_school_cd") }</p>
				</c:if>
			</div>
			<button class="btn btn-primary mb-3" name="end">登録</button>
		</form>
		<a href="ClassList.action">戻る</a>
	</c:param>
</c:import>