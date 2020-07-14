package main.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {

    public static final String ATTRIBUTE = "Account Manager";
    public enum Column {
        USERNAME(1),
        PASSWORD(2),
        USER_RATING(3),
        PROFILE_PICTURE_URL(4);

        public final int num;

        Column(int num) {
            this.num = num;
        }
    }

    public AccountManager() { }

    public boolean isAccountCreated(String username) {
        boolean accountCreated = false;

        try {
            ResultSet resultSet = getResultSetFromUsername(username);
            accountCreated = resultSet.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return accountCreated;
    }

    public boolean isPasswordCorrect(String username, String password) {
        boolean passwordCorrect = false;

        try {
            ResultSet resultSet = getResultSetFromUsername(username);
            resultSet.next();

            Account account = getAccountFromResultSet(resultSet);
            passwordCorrect = account.getPassword().equals(password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return passwordCorrect;
    }

    public void createNewAccount(String username, String password, String pictureURL) {
        Connection connection = DataSource.getConnection();
        assert (connection != null);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into accounts values (?, ?, ?, ?);");
            preparedStatement.setString(Column.USERNAME.num, username);
            preparedStatement.setString(Column.PASSWORD.num, password);
            preparedStatement.setBigDecimal(Column.USER_RATING.num, new BigDecimal(0));
            preparedStatement.setString(Column.PROFILE_PICTURE_URL.num, pictureURL);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Account getAccountFromResultSet(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString(Column.USERNAME.num);
        String password = resultSet.getString(Column.PASSWORD.num);
        BigDecimal userRating = resultSet.getBigDecimal(Column.USER_RATING.num);
        String profilePictureURL = resultSet.getString(Column.PROFILE_PICTURE_URL.num);

        return new Account(username, password, userRating, profilePictureURL);
    }

    private ResultSet getResultSetFromUsername(String username) throws SQLException {
        Connection connection = DataSource.getConnection();
        assert (connection != null);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where username = ?;");
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
