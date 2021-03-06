<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <%@include file="../common/head-meta.jsp" %>
    <%@include file="../common/head-css.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/friend/list2.css"/>
    <title>동행 리스트</title>
</head>
<body>
<%@include file="../common/navbar2.jsp" %>
<sec:authentication var="userDetail" property="principal"/>
<!-- 메인 컨텐츠 영역 -->
<div class="hidden">
    <input type="hidden" id="pg" value="${pg}"/>
    <sec:authorize access="isAuthenticated()">
        <input type="hidden" id="loginId" value="${userDetail.username}">
    </sec:authorize>
</div>

<div class="container-wrap">
    <div class="header-back">
        <div class="header">
            <div class="description"></div>
            <sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
                <button id="btn-friend-write" type="button">동행 구하기</button>
            </sec:authorize>
        </div>
    </div>

    <div class="content-area">
        <ul class="content-group"></ul>
    </div>
    <br/>
    <ul class="page-group"></ul>
    <br/>
</div>
<!-- 메인 컨텐츠 영역 -->
<%@include file="../common/footer.jsp" %>
<%@include file="../common/foot-js.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/friend/list2.js"></script>
</body>
</html>
