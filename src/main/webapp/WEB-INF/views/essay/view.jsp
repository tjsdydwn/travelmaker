<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@include file="../common/head-meta.jsp" %>
    <%@include file="../common/head-css.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/essay/view.css">
    <title>에세이 글 확인</title>
</head>
<body>
<%@include file="../common/navbar.jsp" %>
<input id="bno" type="hidden" value="${essayDTO.bno}"/>
<input id="seq" type="hidden" value="${essayDTO.seq}"/>
<div class="container-fluid">
    <header class="header-box">
        <div class="main-image" style="background-image:url('${pageContext.request.contextPath}/resources/storage/essay/${essayDTO.imageName}')"></div>
        <h1 class="header-title">${essayDTO.title}<span>written by 홍길동</span></h1>
        <!-- 해쉬태그 영역 -->
        <c:set var="hashes" value="${fn:split(essayDTO.hashtag, ',')}"/>
        <div class="header-hash">
            <c:forEach items="${hashes}" var="hash">
                <span class="hash">#${hash}</span>
            </c:forEach>
<%--            <span class="hash">#다녀옴</span>--%>
<%--            <span class="hash">#다녀옴</span>--%>
<%--            <span class="hash">#다녀옴</span>--%>
        </div>
        <!-- 해쉬태그 영역 -->
        <div class="header-info">
            <span>조회 : ${essayDTO.views}</span>
            <span>작성일 : ${essayDTO.dateUpdate}</span>
            <span>수정일 : ${essayDTO.dateUpdate}</span>
        </div>
    </header>
    <section class="container-fluid content-box">
        <div id="contents-area" class="content-area">
            ${essayDTO.content}
        </div>
        <div>
            <button>좋아요 : ${essayDTO.likes}</button>
        </div>
        <div id="comment-zone">
            <div class="input-group">
          <textarea
                  id="comment-content"
                  type="text"
                  class="form-control"
          ></textarea>
                <button id="btn-add-comment" class="input-group-append">작성</button>
            </div>
            <div>
                <ul id="comment-group" style="list-style:none;">
                    <li>
                        <%--                <div class="media text-muted pt-3">--%>
                        <%--                  <svg--%>
                        <%--                          class="bd-placeholder-img mr-2 rounded"--%>
                        <%--                          width="32"--%>
                        <%--                          height="32"--%>
                        <%--                          xmlns="http://www.w3.org/2000/svg"--%>
                        <%--                          preserveAspectRatio="xMidYMid slice"--%>
                        <%--                          focusable="false"--%>
                        <%--                          role="img"--%>
                        <%--                          aria-label="Placeholder: 32x32"--%>
                        <%--                  >--%>
                        <%--                    <rect width="100%" height="100%" fill="#6f42c1" />--%>
                        <%--                    <text x="50%" y="50%" fill="#6f42c1" dy=".3em">32x32</text>--%>
                        <%--                  </svg>--%>
                        <%--                  <p--%>
                        <%--                          class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray"--%>
                        <%--                  >--%>
                        <%--                    <strong class="d-block text-gray-dark">@아이디</strong>--%>
                        <%--                    내용을 적는곳--%>
                        <%--                  </p>--%>
                        <%--                  <div class="btn-group btn-group-sm">--%>
                        <%--                    <button class="btn-primary">좋아요</button>--%>
                        <%--                    <button class="btn-danger">싫어요</button>--%>
                        <%--                    <button class="btn-outline-secondary">답글</button>--%>
                        <%--                  </div>--%>
                        <%--                </div>--%>
                    </li>
                </ul>
            </div>
        </div>
    </section>
</div>
<%@include file="../common/foot-js.jsp" %>
<script src="${pageContext.request.contextPath}/resources/js/essay/view.js"></script>
</body>
</html>
