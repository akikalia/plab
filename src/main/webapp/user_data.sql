USE plabDatabase;

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

INSERT INTO users VALUES
    ( 'user0', 'user0'),
    ( 'user1', 'user1'),
    ( 'user2', 'user2'),
    ( 'user3', 'user3');

INSERT INTO posts(post_pic, owner_name) VALUES
    ('dog_pic', 'user0'),
    ('cat_pic', 'user0'),
    ('mouse_pic', 'user1'),
    ('bunny_pic', 'user1'),
    ('rat_pic', 'user4');

INSERT INTO connections (follower_name, followee_name) VALUES
    ( 'user0', 'user1'),
    ( 'user1', 'user0'),
    ( 'user1', 'user2'),
    ( 'user1', 'user3'),
    ( 'user3', 'user4');

INSERT INTO reviews(reviewer_name, post_id, rating) VALUES
    ( 'user0', 10, 1),
    ( 'user1', 10, 2),
    ( 'user1', 9, 2),
    ( 'user1', 8, 4),
    ( 'user4', 7, 5),
    ( 'user4', 9, 4);




