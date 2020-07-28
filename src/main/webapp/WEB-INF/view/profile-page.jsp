<%@ page import="model.Profile" %>
<%@ page import="dao.DBmanager" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page session = "true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<jsp:include page="head.jsp" />

<body>
    <jsp:include page="header-w-search.jsp" />
    <br>
        <div class="profile">
            <a class="profile_pic_a" href="/profile?u=${profile.getUsername()}"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/${profile.getUsername()}.jpg" alt="profile_picture" class="profile_pic"></a></br>
            <label>username: ${profile.getUsername()}</label></br>
            <%
                String pr = ((Profile) request.getAttribute("profile")).getUsername();
                String us = (String) session.getAttribute("user");
                String flag = "";
                if(pr.equals(us)) {
                    flag = "own_profile";
                } else {
                    ServletContext sc = request.getServletContext();
                    DBmanager db = (DBmanager) sc.getAttribute("db");
                    Set<String> set = db.getFollowings(us);
                    if(set.contains(pr)) {
                        flag = "yes";
                    } else {
                        flag = "no";
                    }
                }
                request.setAttribute("flag", flag);
            %>
            <c:if test="${flag != 'own_profile'}">
                <form action = "cbChanged" method = "post">
                    <input type="hidden" name="u" value=${profile.getUsername()}>
                    <input type="hidden" name="logged_in" value=${user}>
                    <c:choose>
                        <c:when test="${flag == 'yes'}">
                            following
                        </c:when>
                        <c:otherwise>
                            follow
                        </c:otherwise>
                    </c:choose>
                    <input type="checkbox" name="cb" <c:if test = "${flag == 'yes'}">checked</c:if> onChange="this.form.submit()">
                </form>
            </c:if>
            <label>followers: ${profile.getNumFollowers()}</label></br>
            <label>following: ${profile.getNumFollowing()}</label></br>
            <label>reviews: ${profile.getNumReviews()}</label></br>
            <label>rating: ${profile.getRating()}</label></br>
        </div>
        </br>
        <div class="posts">
            <c:forEach var="post" items="${profile.getPosts()}">
                <div class="post">
                    <a class="profile_pic_a" href="/profile?username=${post.getOwner_name()}"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/${post.getOwner_name()}.jpg" alt="profile_picture" class="profile_pic"></a></br>
                    <label>${post.getOwner_name()}</label></br>
                    <img src="${pageContext.request.contextPath}/resources/userData/posts/${post.getPost_id()}.jpg" alt="post" class="post_img"></br>
                    <form id="post-rating-form" method="post">
                        <div class="post-rating">
                            <input type="hidden" name="post_id" value="${post.getPost_id()}">
                            <input type="number" name="rating" />
                            <button type="submit" value="" >Rate</button>
                        </div>
                    </form>
                </div></br>
            </c:forEach>
        </div>
    </main>
    <jsp:include page="footer.jsp" />
</body>

</html>