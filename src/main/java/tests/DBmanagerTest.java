package tests;

import dao.DBmanager;
import model.dbConnector;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SimplifiableAssertion")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DBmanagerTest {

    private DBmanager dBmanager;
    private static Connection connection;

    @BeforeAll
    void setUp() throws SQLException {
        connection = dbConnector.getConnection();
        assert connection != null;
        Statement statement = connection.createStatement();
        statement.execute("create database oopp_test");
        statement.execute("use oopp_test");

        statement.execute("DROP TABLE IF EXISTS users;");
        statement.execute("CREATE TABLE users (" +
                "user_name CHAR(64) NOT NULL UNIQUE PRIMARY KEY ," +
                "password CHAR(64) NOT NULL" +
                ");");

        statement.execute("DROP TABLE IF EXISTS connections;");
        statement.execute("CREATE TABLE connections (" +
                "connection_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "follower_name CHAR(64) NOT NULL," +
                "followee_name CHAR(64) NOT NULL" +
                ");");

        statement.execute("DROP TABLE IF EXISTS posts;");
        statement.execute("CREATE TABLE posts (" +
                "post_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "post_pic CHAR(64) NOT NULL," +
                "owner_name CHAR(64) NOT NULL," +
                "date_added DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");");

        statement.execute("DROP TABLE IF EXISTS reviews;");
        statement.execute("CREATE TABLE reviews (" +
                "review_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                "reviewer_name CHAR(64) NOT NULL," +
                "post_id INT NOT NULL," +
                "rating DOUBLE NOT NULL" +
                ");");
    }

    @AfterAll
    void tearDown() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP DATABASE oopp_test");
        connection.close();
    }

    @BeforeEach
    void init() {
        dBmanager = new DBmanager();
    }

    @Test
    @Order(1)
    void testNameUsed() {
        assertEquals(false, dBmanager.nameUsed(""));
        assertEquals(false, dBmanager.nameUsed("123"));
    }

    @Test
    @Order(2)
    void testAddUser() {
        assertEquals(true, dBmanager.addUser("1", "1"));
        assertEquals(true, dBmanager.nameUsed("1"));
        assertEquals(false, dBmanager.addUser("1", "1"));
        assertEquals(false, dBmanager.addUser("1", "2"));
        assertEquals(true, dBmanager.addUser("2", "2"));
        assertEquals(true, dBmanager.addUser("3", "3"));
    }

    @Test
    @Order(3)
    void testPasswordValidation() {
        assertEquals(false, dBmanager.passwordValidation("12", "12"));
        assertEquals(false, dBmanager.passwordValidation("1", "12"));
        assertEquals(true, dBmanager.passwordValidation("1", "1"));
    }

    @Test
    @Order(4)
    void testGetProfile() {
        assertNull(dBmanager.getProfile("12"));
        assertEquals("1", dBmanager.getProfile("1").getUsername());
    }

    @Test
    @Order(5)
    void testSearchProfile() {
        assertEquals(0, dBmanager.searchProfile("a").size());
        assertEquals(3, dBmanager.searchProfile("").size());
        assertEquals(1, dBmanager.searchProfile("1").size());
    }

    @Test
    @Order(6)
    void testGetUsersPosts() {
        dBmanager.addPost("1", "1_1.jpg");
        dBmanager.addPost("1", "1_1.jpg");
        dBmanager.addPost("2", "2_1.jpg");

        assertEquals(0, dBmanager.getUsersPosts("a").size());
        assertEquals(0, dBmanager.getUsersPosts("3").size());
        assertEquals(1, dBmanager.getUsersPosts("2").size());

        dBmanager.addPost("2", "2_2.jpg");
        dBmanager.addPost("3", "3_1.jpg");

        assertEquals(2, dBmanager.getUsersPosts("1").size());
        assertEquals(2, dBmanager.getUsersPosts("2").size());
        assertEquals(1, dBmanager.getUsersPosts("3").size());
    }

    @Test
    @Order(7)
    void testGetPostsCount() {
        assertEquals(5, dBmanager.getPostsCount());
    }


    @Test
    @Order(8)
    void testGetFeedPosts() {
        assertEquals(0, dBmanager.getFeedPosts("a").size());
        assertEquals(0, dBmanager.getFeedPosts("1").size());

        dBmanager.addConnection("1", "2");
        //assertEquals(2, dBmanager.getFeedPosts("1").size());
        //assertEquals(0, dBmanager.getFeedPosts("2").size());

        dBmanager.addConnection("1", "3");
        //assertEquals(3, dBmanager.getFeedPosts("1").size());

        dBmanager.removeConnection("1", "2");
        //assertEquals(1, dBmanager.getFeedPosts("1").size());
    }

    @Test
    @Order(9)
    void testGetFollowings() {
        assertEquals(0, dBmanager.getFollowings("a").size());
        assertEquals(0, dBmanager.getFollowings("2").size());
        assertEquals(1, dBmanager.getFollowings("1").size());

        dBmanager.addConnection("1", "2");
        assertEquals(2, dBmanager.getFollowings("1").size());

        dBmanager.addConnection("3", "1");
        assertEquals(2, dBmanager.getFollowings("1").size());
        assertEquals(0, dBmanager.getFollowings("2").size());
        assertEquals(1, dBmanager.getFollowings("3").size());
    }

    @Test
    @Order(10)
    void testReview() {
        assertEquals(-1, dBmanager.getReview("a", 13));
        assertEquals(-1, dBmanager.getReview("a", 1));
        assertEquals(-1, dBmanager.getReview("1", 3));

        assertEquals(true, dBmanager.setReview("1", 1, 5));
        assertEquals(true, dBmanager.setReview("1", 3, 1));

        assertEquals(5, dBmanager.getReview("1", 1));
        assertEquals(1, dBmanager.getReview("1", 3));
        assertEquals(-1, dBmanager.getReview("1", 4));
        assertEquals(-1, dBmanager.getReview("3", 1));

        assertEquals(false, dBmanager.removeReview("1", 4));
        assertEquals(true, dBmanager.removeReview("1", 1));

        assertEquals(-1, dBmanager.getReview("1", 1));
        assertEquals(1, dBmanager.getReview("1", 3));
    }
}