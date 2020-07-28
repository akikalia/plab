<html>

<head>
    <link rel="stylesheet" href="../../resources/css/styles.css">
    <title>plab</title>
</head>

<body>
<jsp:include page="header-w-search.jsp"/>
<main>
    <form class="add-post" action="addpost" method="post" enctype="multipart/form-data">
        <label for="post-img">Upload A Picture:</label>
        <input type="file" placeholder="Post" id="post-img" accept=".jpg" name="postPic" required></br>
        <button class="add-post-button" type="submit">Post</button>
        </br>
    </form>
</main>
<jsp:include page="footer.jsp"/>
</body>

</html>