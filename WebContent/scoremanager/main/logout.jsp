<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<h2 class="h3 bg-secondary bg-opacity-10 py-2 px-4">ログアウト</h2>
		<p class="text-center bg-4 bg-success bg-opacity-50">ログアウトしました</p>
		<a href="/exam/scoremanager/Login.action">ログイン</a>
	</c:param>
</c:import>