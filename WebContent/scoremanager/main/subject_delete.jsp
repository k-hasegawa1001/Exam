<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2>
            <form method="post" action="SubjectDeleteExecute.action">
				<label class="form-label"  for="delete">
				<p>「
					<input type="hidden" id="name" name="name" value="${subject_name }" readonly>（
					<input type="hidden" id="cd" name="cd" value="${subject_cd }" readonly>
						）」を削除してもよろしいですか</p>
				</label>
				<button class="btn btn-danger mb-3" name="delete">削除</button>
			</form>
			<a href="SubjectList.action">戻る</a>
	</c:param>
</c:import>