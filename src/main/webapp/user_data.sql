USE oopp;

DROP TABLE IF EXISTS users;
 -- remove table if it already exists and start from scratch

CREATE TABLE users (
    user_name CHAR(64) NOT NULL UNIQUE PRIMARY KEY ,
    password CHAR(64) NOT NULL
);

DROP TABLE IF EXISTS connections;
 -- remove table if it already exists and start from scratch

CREATE TABLE connections (
    connection_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
	follower_name CHAR(64) NOT NULL,
    followee_name CHAR(64) NOT NULL
);

DROP TABLE IF EXISTS posts;
 -- remove table if it already exists and start from scratch

CREATE TABLE posts (
	post_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    post_pic CHAR(64) NOT NULL,
    owner_name CHAR(64) NOT NULL,
    date_added DATETIME DEFAULT CURRENT_TIMESTAMP
);


DROP TABLE IF EXISTS reviews;
-- remove table if it already exists and start from scratch

CREATE TABLE reviews (
                         review_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
                         reviewer_name CHAR(64) NOT NULL,
                         post_id INT NOT NULL,
                         rating DOUBLE NOT NULL
);



