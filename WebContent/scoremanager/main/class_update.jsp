<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス管理変更</h2>
		<form method="post" action="ClassNumUpdateExecute.action">
			<div class="mb-3">
				<label class="form-label" for="classcd">クラスコード</label>
				<input class="form-control mb-3" id="classcd" name="classcd" value="${now_classCd }" placeholder="クラス番号を入力してください" required >
				<input type="text" class="form-control mb-3" id="old_classCd" name="old_classCd"value="${now_classCd }" hidden>
				<c:if test="${errors.get('errors_notEntry_cd') != null }">
					<p class="text-warning mb-3">${errors.get("errors_notEntry_cd") }</p>
				</c:if>
				<c:if test="${errors.get('error_duplication_cd') != null }">
					<p class="text-warning mb-3">${errors.get("error_duplication_cd") }</p>
				</c:if>
			</div>
			<button type="submit" class="btn btn-primary mb-3" name="update">変更</button>
		</form>
		<a href="ClassList.action">戻る</a>
	</c:param>
</c:import>