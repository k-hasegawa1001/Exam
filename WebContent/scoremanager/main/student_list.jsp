<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">
    	得点管理システム
    </c:param>

    <c:param name="scripts"></c:param>

    <c:param name="content">
        <section class="no">
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>
            <div class="my-2 text-end px-4">
                <a href="StudentCreate.action">新規登録</a>
            </div>

            <form method="get">
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">
                    <div class="col-4">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">--------</option>
                            <c:forEach var="year" items="${ent_year_set}">
                                <option value="${year}" <c:if test="${year==f1}">selected</c:if>>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-4">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">-------</option>
                            <c:forEach var="num" items="${class_num_set}">
                            	<%-- 現在のnumと選択されていたf2が一致していた場合selectedを追記 --%>>
                                <option value="${num}" <c:if test="${num==f2}">selected</c:if>>${num}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-2 form-check text-center">
                        <label class="form-check-label" for="student-f3-check">在学中
                        <%-- パラメーターf3が存在している場合checkedを追記 --%>
                            <input class="form-check-input" type="checkbox"
                                   id="student-f3-check" name="f3" value="t"
                                   <c:if test="${not empty f3}">checked</c:if>>
                        </label>
                    </div>

                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button">絞込み</button>
                    </div>

                    <div class="mt-2 text-warning">${errors.get("f1")}</div>
                </div>
            </form>

            <c:choose>
                <c:when test="${students.size() > 0}">
                	<div><strong>最大件数：${totalItems}件</strong></div>
                    <div><strong>現在のページ表示件数：${students.size()}件</strong></div>
                    <table class="table table-hover">
                        <tr>
                            <th>入学年度</th>
                            <th>学生番号</th>
                            <th>氏名</th>
                            <th>クラス</th>
                            <th class="text-center">在学中</th>
                            <th></th>
                            <th></th>
                        </tr>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>${student.entYear}</td>
                                <td>${student.no}</td>
                                <td>${student.name}</td>
                                <td>${student.classNum}</td>
                                <td class="text-center">
                                <%-- 在学フラグが立っている場合「〇」それ以外は「×」を表示 --%>
                                    <c:choose>
                                        <c:when test="${student.isAttend()}">
                                         ○
                                        </c:when>
                                        <c:otherwise>
                                         ×
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="StudentUpdate.action?no=${student.no}">変更</a>
                                </td>
                                <td>
                                    <%-- <a href="StudentDelete.action?no=${student.no}">削除</a> --%>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

                    <!-- ページネーション -->
                    <c:if test="${totalItems > 10}">
	                    <div style="margin-top:20px; text-align:center;">
						  <c:if test="${currentPage > 1}">
						    <a href="${paginationBaseUrl}?page=${currentPage - 1}&f1=${f1}&f2=${f2}&f3=${f3}">前へ</a>
						  </c:if>
						  <c:forEach var="i" begin="1" end="${totalPages}">
						    <c:choose>
						      <c:when test="${i == currentPage}">
						        <b style="margin:0 4px;">${i}</b>
						      </c:when>
						      <c:otherwise>
						        <a style="margin:0 4px;" href="${paginationBaseUrl}?page=${i}&f1=${f1}&f2=${f2}&f3=${f3}">${i}</a>
						      </c:otherwise>
						    </c:choose>
						  </c:forEach>
						  <c:if test="${currentPage < totalPages}">
						    <a href="${paginationBaseUrl}?page=${currentPage + 1}&f1=${f1}&f2=${f2}&f3=${f3}">次へ</a>
						  </c:if>
						</div>
					</c:if>

                </c:when>
                <c:otherwise>
                    <div>学生情報が存在しませんでした</div>
                </c:otherwise>
            </c:choose>
        </section>
    </c:param>
</c:import>
