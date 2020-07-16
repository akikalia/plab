
<div class="post">
        <a class="profile_pic_a" href="/<%=//user_name%>>"><img src="${pageContext.request.contextPath}/resources/userData/profilePics/<%=//user_name%>>" alt="profile_picture" class="profile_pic_img"></a>
        <label><%=//user_name%></label>
        <img src="${pageContext.request.contextPath}/resources/userData/posts/<%=//post_id%>>" alt="post" class="post_img">
        <form id="post-rating-form" method="post">
                <div class="post-rating">
                        <input type="text" name="rating" />
                        <button type="submit" value="" >Rate</button>
                </div>
        </form>
</div>

