<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<header>
    <div class="navbar">
        <a href="/"><img src="${pageContext.request.contextPath}/resources/logo/plab_png.png" alt="logo" class="logo"></a>
        <c:if test="${pageContext.request.getSession().getAttribute('user') != null}">
            <jsp:include page="search-bar.jsp" />
        </c:if>
        <nav>
            <ul>
                <li><a href="/"><img src="${pageContext.request.contextPath}/resources/icons/home-icon-blue.png" alt="home" class="icon"></a></li>
                <c:if test="${pageContext.request.getSession().getAttribute('user') != null}">
                    <li><a href="/profile?u=${pageContext.request.getSession().getAttribute('user')}"><img src="${pageContext.request.contextPath}/resources/icons/profile-icon-blue.png" alt="profile" class="icon"></a></li>
                </c:if>
                <li><a href="/logout"><img src="${pageContext.request.contextPath}/resources/icons/logout-icon-blue.png" alt="logout" class="icon"></a></li>
            </ul>
        </nav>
    </div>
</header>