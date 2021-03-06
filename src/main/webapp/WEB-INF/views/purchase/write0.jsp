<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <%@include file="../common/head-meta.jsp" %>
    <%@include file="../common/head-css.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/purchase/write.css">
    <title>대행구매 작성</title>
</head>
<body>
<%@include file="../common/navbar2.jsp" %>
<div class="container-wrap">
    <form action="/pur/setWriteRequest" id="writeForm" class="write-form" method="post">
        <div class="hidden">
            <sec:authentication var="userDetail" property="principal"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="nickname" value="${userDetail.nickname}">
            <input type="hidden" name="writeUserSeq" value="${userDetail.seq}">
            <input type="hidden" name="username" value="${userDetail.username}">
            <input type="hidden" name="con" value="2">
        </div>

        <div class="input-wrap">
            <label for="title">제목</label>
            <input type="text" id="title" name="title" class="v"/>
            <div class="v-feed"></div>
            <div class="iv-feed"></div>
        </div>

        <div class="input-wrap">
            <label for="location">여행지역</label>
            <input type="text" id="location" name="location" class="v"/>
            <div class="v-feed"></div>
            <div class="iv-feed"></div>
        </div>

        <div class="input-wrap">
            <label for="date-start">여행 시작일</label>
            <input type="date" id="date-start" name="dateStart" class="v"/>
            <div class="v-feed"></div>
            <div class="iv-feed"></div>
        </div>
        <div class="input-wrap">
            <label for="date-end">여행 종료일</label>
            <input type="date" id="date-end" name="dateEnd" class="v"/>
            <div class="v-feed"></div>
            <div class="iv-feed"></div>
        </div>

        <div class="input-wrap textarea">
            <label for="content">상세내용</label>
            <textarea id="content" name="content"></textarea>
            <div class="v-feed"></div>
            <div class="iv-feed"></div>
        </div>

        <div class="button-wrap">
            <button id="next-btn" type="button" class="btn btn-tsave">저장</button>
            <button id="cancel" type="button" class="btn btn-tdanger">취소</button>
        </div>
    </form>
</div>
<%@include file="../common/footer.jsp" %>
<%@include file="../common/foot-js.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/purchase/write.js"></script>
</body>
</html>
