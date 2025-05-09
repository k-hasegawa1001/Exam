<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">
        得点管理システム
    </c:param>

    <c:param name="scripts"></c:param>

    <c:param name="content">
        <section class="no">
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>
            <form action="TestRegist.action" method="post">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
                    <!-- 入学年度 -->
                    <div class="col-2">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="ent_year">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- クラス -->
                    <div class="col-2">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="class_num">
                            <option value="0">-------</option>
                            <c:forEach var="num" items="${class_num_set}">
                                <option value="${num}" <c:if test="${num==f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- 科目 -->
                    <div class="col-4">
                        <label class="form-label" for="subject-f3-select">科目</label>
                        <select class="form-select" id="subject-f3-select" name="subject">
                            <option value="0">--------</option>
                            <c:forEach var="subject" items="${subject_set}">
                                <option value="${subject.cd}" <c:if test="${subject.cd==f3}">selected</c:if>>${subject.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- 回数 -->
                    <div class="col-2">
                        <label class="form-label" for="round-f4-select">回数</label>
                        <select class="form-select" id="round-f4-select" name="round">
                            <option value="0">--------</option>
                            <c:forEach var="round" items="${round_set}">
                                <option value="${round}" <c:if test="${round==f4}">selected</c:if>>${round}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- 検索ボタン -->
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary w-10" id="filter-button">検索</button>
                    </div>
                </div>
            </form>

            <c:if test="${isSearched}">
				<div>
				    <label>科目：${subject_name}（${round}回）</label>
				</div>

                <form action="TestRegistExecute.action" method="post">
                    <input type="hidden" name="ent_year" value="${ent_year}" />
                    <input type="hidden" name="class_num" value="${class_num}" />
                    <input type="hidden" name="subject" value="${subject}" />
                    <input type="hidden" name="round" value="${round}" />

                    <table class="table">
                        <thead>
                            <tr>
                                <th>入学年度</th>
                                <th>クラス</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>点数</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="test" items="${tests}" varStatus="status">
                                <tr>
                                    <td>${test.student.entYear}</td>
                                    <td>${test.student.classNum}</td>
                                    <td>
                                        ${test.student.no}
                                        <input type="hidden" name="student_no" value="${test.student.no}" />
                                    </td>
                                    <td>${test.student.name}</td>
                                    <td>
                                        <input type="number" name="score" class="form-control" min="0" max="100"
                                               value="${test.point}" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="text-center col-2">
                        <button type="submit" class="btn btn-secondary w-10">登録して終了</button>
                    </div>
                </form>
            </c:if>
        </section>
    </c:param>
</c:import>