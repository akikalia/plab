<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<jsp:include page="head.jsp" />
<body>
    <jsp:include page="header.jsp" />
    <main>
        <form method = get action = "addpost">
            <button type="submit">Add Post</button></br>
        </form>
        <div class="posts">
            <c:forEach var="post" items="${posts}">
                <div class="post">
                    <a class="profile_pic_a" href="/profile?u=${post.getOwner_name()}"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/${post.getOwner_name()}.jpg" alt="profile_picture" class="profile_pic"></a></br>
                    <label>${post.getOwner_name()}</label></br>
                    <label>post id :${post.getPost_id()}</label></br>
                    <img src="${pageContext.request.contextPath}/resources/userData/posts/${post.getPost_id()}.jpg" alt="post" class="post_img"></br>
                    <form id="post-rating-formid=${post.getPost_id()}" class='star-rating-form'  method="POST">
                        <input class='rating-input' type="hidden" name="rating" >
                        <input type="hidden" name="post_id" value="${post.getPost_id()}">
                        <div class="stars"  data-rating="${post.getPost_rating()}">
                            <img style="width:55px; height:auto;" val="1" class="star" src="${pageContext.request.contextPath}/resources/icons/empty_star.png">
                            <img style="width:55px; height:auto;" val="2" class="star" src="${pageContext.request.contextPath}/resources/icons/empty_star.png">
                            <img style="width:55px; height:auto;" val="3" class="star" src="${pageContext.request.contextPath}/resources/icons/empty_star.png">
                            <img style="width:55px; height:auto;" val="4" class="star" src="${pageContext.request.contextPath}/resources/icons/empty_star.png">
                            <img style="width:55px; height:auto;" val="5" class="star" src="${pageContext.request.contextPath}/resources/icons/empty_star.png">
                        </div>
                    </form>
                </div>
            </c:forEach>
        </div>
    </main>
    <jsp:include page="footer.jsp" />
</body>
</html>