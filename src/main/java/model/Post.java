package model;

import java.sql.Date;

public class Post {
    private final int id;
    private final String pictureURL;
    private final String posterUsername;
    private final Date postDate;

    public Post(int id, String pictureURL, String posterUsername, Date postDate) {
        this.id = id;
        this.pictureURL = pictureURL;
        this.posterUsername = posterUsername;
        this.postDate = postDate;
    }

    public int getId() {
        return id;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public String getPosterUsername() {
        return posterUsername;
    }

    public Date getPostDate() {
        return postDate;
    }
}
