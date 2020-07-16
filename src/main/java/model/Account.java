package model;

import java.math.BigDecimal;

public class Account {
    private final String username;
    private final String password;
    private final BigDecimal userRating;
    private final String profilePictureURL;

    public Account(String username, String password, BigDecimal userRating, String profilePictureURL) {
        this.username = username;
        this.password = password;
        this.userRating = userRating;
        this.profilePictureURL = profilePictureURL;
    }

    public Account(String username, String password, String profilePictureURL) {
        this(username, password, new BigDecimal(0), profilePictureURL);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getUserRating() {
        return userRating;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }
}
