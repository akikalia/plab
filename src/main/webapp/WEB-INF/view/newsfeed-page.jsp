<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<jsp:include page="head.jsp" />
<body>
    <jsp:include page="header-w-search.jsp" />
    <main>
        <button href="/addpost">Add Post</button>
        <div class="posts">
            <c:forEach var="post" items="${posts}">
                <div class="post">
                    <a class="profile_pic_a" href="/profile?u=${post.getOwner_name()}"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/${post.getOwner_name()}.jpg" alt="profile_picture" class="profile_pic"></a></br>
                    <label>${post.getOwner_name()}</label></br>
                    <label>post id :${post.getPost_id()}</label></br>
                    <img src="${pageContext.request.contextPath}/resources/userData/posts/${post.getPost_id()}.jpg" alt="post" class="post_img"></br>
                    <form id="post-rating-form" method="post">
                        <div class="post-rating">
                            <input type="hidden" name="post_id" value="${post.getPost_id()}">
                            <input type="number" name="rating" />
                            <button type="submit" value="" >Rate</button>
                        </div>
                    </form>
                </div>
            </c:forEach>
        </div>
    </main>
    <jsp:include page="footer.jsp" />
</body>
</html>