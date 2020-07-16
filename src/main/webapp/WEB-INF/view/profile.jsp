<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="profile">
    <a class="profile_pic_a" href="/<%=//user_name%>>"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/<%=//user_name%>>" alt="profile_picture" class="profile_pic_img"></a>
    <label><%=//user_name%></label>
    <form:checkbox path="">Follow</form:checkbox>
    <label>followers: <%=//num_followers%></label>
    <label>following: <%=//num_following%></label>
    <label>reviews: <%=//num_reviews%></label>
    <label>rating: <%=//rating/5 star%></label>
</div>