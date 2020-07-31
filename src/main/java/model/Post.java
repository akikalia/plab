package model;

import java.sql.Time;
import java.sql.Timestamp;

public class Post {
    private int post_id;
    private String owner_name;
    private String post_pic;
    private Timestamp date_added;
    private double post_rating;

    public Post(int post_id, String owner_name, String post_pic, double post_rating, Timestamp date_added){
        this.date_added = date_added;
        this.post_pic = post_pic;
        this.owner_name = owner_name;
        this.post_rating = post_rating;
        this.post_id = post_id;
    }

    public Post(){
    }

    public Timestamp getDate_added() {
        return date_added;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getPost_pic() {
        return post_pic;
    }

    public int getPost_id() {
        return post_id;
    }

    public double getPost_rating() { return post_rating; }

    public void setDate_added(Timestamp date_added) {
        this.date_added = date_added;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public void setPost_pic(String post_pic) {
        this.post_pic = post_pic;
    }

    public void setPost_rating(double post_rating) { this.post_rating = post_rating; }
}