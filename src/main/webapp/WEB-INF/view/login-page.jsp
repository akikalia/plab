<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <link rel="icon" href="${pageContext.request.contextPath}/resources/logo/plab_title_logo2.png" type="image/icon type">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/styles.css">
    <title>plab</title>
</head>
<body>
    <jsp:include page="header.jsp" />
    </br>
    <main>
        <jsp:include page="sign-in.jsp" />
        <jsp:include page="sign-up.jsp" />
    </main>
    </br>
    <jsp:include page="footer.jsp" />
</body>
</html>