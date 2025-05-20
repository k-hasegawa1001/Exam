<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目変更</h2>
		<p class="text-center bg-4 bg-warning bg-opacity-50">科目コードが存在していない状態で追加しようとしています。</p>
		<a href="SubjectList.action">科目リスト</a>
	</c:param>
</c:import>