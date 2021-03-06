<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!-- 헤더, 네비게이션 영역 -->
<div class="header-wrap">
    <header>
        <div class="hidden">
            <sec:authentication var="userDetail" property="principal"/>
            <sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
                <input type="hidden" id="seq" value="${userDetail.seq}"/>
                <form action="/logout" method="post" id="logoutForm">
                    <input type="hidden" name="${_csrf.parameterName}"
                           value="${_csrf.token}">
                </form>
            </sec:authorize>
            <input type="hidden" id="csrfTokenName"
                   value="${_csrf.parameterName}"/> <input type="hidden"
                                                           id="csrfTokenValue" value="${_csrf.token}"/>
        </div>
        <a href="${pageContext.request.contextPath}/">
            <div class="logo"></div>
        </a>
        <nav class="gnb-wrap">
            <ul class="gnb-group">
                <li class="gnb-item"><a href="/story">여행 이야기</a></li>
                <li class="gnb-item"><a href="/store/list/1">스토어</a></li>
                <li class="gnb-item"><a href="/friend/list/1">동행</a></li>
                <li class="gnb-item"><a href="/pur/list/1">대리구매</a></li>
            </ul>
        </nav>
        <div class="header-group">
            <div class="search-bar">
                <div class="search-wrap">
                    <input type="text" class="input-search" placeholder="검색..."/>
                </div>
                <button type="button" class="search-btn">
                    <img
                            src="${pageContext.request.contextPath}/resources/img/search.svg"
                            alt="검색"/>
                </button>
            </div>
            <sec:authentication var="userDetail" property="principal"/>
            <sec:authorize access="!isAuthenticated()">
                <button type="button" class="btn btn-header btn-login">로그인
                </button>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
                <button type="button" class="btn btn-header" onclick="logoutSubmit()">로그아웃</button>
            </sec:authorize>
        </div>

    </header>
</div>
<div id="modal"></div>
<div class="lds-back hidden">
    <div class="lds-roller">
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
        <div></div>
    </div>
</div>
<!-- 헤더, 네비게이션 영역 -->