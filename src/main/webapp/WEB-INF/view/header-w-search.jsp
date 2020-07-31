<header>
    <div class="navbar">
        <a href="/"><img src="${pageContext.request.contextPath}/resources/logo/plab_png.png" alt="logo" class="logo"></a>
        <jsp:include page="search-bar.jsp" />
        <nav>
            <ul>
                <li><a href="/"><img src="${pageContext.request.contextPath}/resources/icons/home-icon-blue.png" alt="home" class="icon"></a></li>
                <li><a href="/profile?u=${pageContext.session.getAttribute("user")}"><img src="${pageContext.request.contextPath}/resources/icons/profile-icon-blue.png" alt="profile" class="icon"></a></li>
                <li><a href="/logout"><img src="${pageContext.request.contextPath}/resources/icons/logout-icon-blue.png" alt="logout" class="icon"></a></li>
            </ul>
        </nav>
    </div>
</header>
