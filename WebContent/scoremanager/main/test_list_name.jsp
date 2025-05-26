<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp">
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>
		<form method="get" action="TestListSubjectExecute.action">
			<div class="row mx-3 mb-3 py-2 align-items-center rounded" id="filter">
				<div class="col-2 text-center">
					科目情報
				</div>
				<div class="col-2">
					<label class="form-label" for="f1">入学年度</label>
					<select class="form-select" id="f1" name="f1">
						<option value="0">--------</option>
						<c:forEach var="year" items="${ent_year_set}">
							<option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-2">
					<label class="form-label" for="f2">クラス</label>
					<select class="form-select" id="f2" name="f2">
						<option value="0">--------</option>
						<c:forEach var="class_num" items="${class_num_set}">
							<option value="${class_num}" <c:if test="${class_num==f2}">selected</c:if>>${class_num}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-4">
					<label class="form-label" for="f3">科目</label>
					<select class="form-select" id="f3" name="f3">
						<option value="0">--------</option>
						<c:forEach var="subject" items="${subject_set}">
							<option value="${subject.getCd()}" <c:if test="${subject.getCd()==f3}">selected</c:if>>${subject.getName()}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-2 text-center">
					<button class="btn btn-secondary w-50" id="filter-button">検索</button>
				</div>
				<div class="mt-2 text-warning">
				<c:if test="${errors.get('errors_notFound_Cd') != null }">
					<p class="text-warning mb-3">${errors.get("errors_notFound_cd") }</p>
				</c:if>
				</div>
			</div>
		</form>

		<hr class="mt-2">

		<form method="get" action="TestListStudentExecute.action">
			<div class="row mx-3 mb-3 py-2 align-items-center rounded" id="filter">
				<div class="col-2 text-center">
					学生情報
				</div>
				<div class="col-4">
					<label class="form-label" for="f4">学生番号</label>
					<input class="form-control" type="text" id="f4" name="f4" placeholder="学生番号を入力してください" required
						<c:if test="${f4 != null }">value="${f4 }"</c:if>
					>
				</div>
				<div class="col-2 text-center">
					<button class="btn btn-secondary w-50" id="filter-button">検索</button>
				</div>
			</div>
		</form>

		<hr class="mt-2">

		<form method="get" action="TestListNameExecute.action">
		    <div class="row mx-3 align-items-center" id="filter">
		        <div class="col-2 text-center">学生氏名</div>
		        <div class="col-4">
		            <label class="form-label" for="f5">学生氏名</label>
		            <input class="form-control" type="text" id="f5" name="f5" placeholder="検索したい名前を入力してください"
		                <c:if test="${f5 != null }">value="${f5 }"</c:if>
		            >
		        </div>
		        <div class="col-2 text-center">
		            <button class="btn btn-secondary w-50" id="filter-button">検索</button>
		        </div>
		    </div>
		</form>

		<hr class="mt-2">

<!-- 	<c:if test="${not empty errors.name}">
		    <div class="mt-2 text-warning">${errors.name}</div>
		</c:if> -->

		<c:choose>
		    <c:when test="${searchStudents != null && searchStudents.size() > 0}">
		        <div>検索結果：${searchStudents.size()}件</div>
		        <table class="table table-hover">
		            <tr>
		                <th>学生番号</th>
		                <th>氏名</th>
		                <th>入学年度</th>
		                <th>クラス</th>
		                <th>確認ページ</th>
		            </tr>
		            <c:forEach var="student" items="${searchStudents}">
		                <tr>
		                    <td>${student.no}</td>
		                    <td>${student.name}</td>
		                    <td>${student.entYear}</td>
		                    <td>${student.classNum}</td>
		                    <td><a href="TestListStudentExecute.action?f4=${student.getNo()}">確認</a></td>
		                </tr>
		            </c:forEach>
		        </table>
		    </c:when>
		    <c:otherwise>
		        <div>該当する学生が見つかりませんでした。</div>
		    </c:otherwise>
		</c:choose>
    </c:param>
</c:import>