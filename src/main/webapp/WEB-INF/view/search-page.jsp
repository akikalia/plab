<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<jsp:include page="head.jsp" />

<body>
    <jsp:include page="header.jsp" />
    <main>
        <div class="profiles">
            <c:forEach var="profile" items="${profiles}">
                <div class="profile">
                    <a class="profile_pic_a" href="/profile?u=${profile}"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/${profile}.jpg" alt="profile_picture" class="profile_pic"></a>
                    <label>${profile}</label>
                </div></br>
            </c:forEach>
        </div>
    </main>
    <jsp:include page="footer.jsp" />
</body>
</html>