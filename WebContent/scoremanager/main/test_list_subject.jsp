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
					${errors.get("error_subject")}
				</div>
			</div>
		</form>

		<hr class="mt-2">

		<form method="get" action="TestListStudentExecute.action">
			<div class="row mx-3 align-items-center" id="filter">
				<div class="col-2 text-center">
					学生情報
				</div>
				<div class="col-4">
					<label class="form-label" for="f4">学生番号</label>
					<input class="form-control" type="text" id="f4" name="f4" placeholder="学生番号を入力してください"
						<c:if test="${f4 != null }">value="${f4 }"</c:if>
					>
				</div>
				<div class="col-2 text-center">
					<button class="btn btn-secondary w-50" id="filter-button">検索</button>
				</div>
			</div>
		</form>

		<c:choose>
			<c:when test="${testListSubject.size() > 0 }">
				<div>科目：${subjectName }</div>
                    <table class="table table-hover">
                        <tr>
                            <th>入学年度</th>
                            <th>クラス</th>
                            <th>学生番号</th>
                            <th>氏名</th>
                            <th>１回</th>
                            <th>２回</th>
                        </tr>
                        <c:forEach var="testSubject" items="${testListSubject }">
                        	<tr>
                        		<td>${testSubject.entYear }</td>
                        		<td>${testSubject.classNum }</td>
                        		<td>${testSubject.studentNo }</td>
                        		<td>${testSubject.studentName }</td>
                        		<td>${testSubject.getPoint(1) }</td>
                        		<td>${testSubject.getPoint(2) }</td>
                        	</tr>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <div>成績情報が存在しませんでした</div>
                </c:otherwise>
		</c:choose>
	</c:param>
</c:import>