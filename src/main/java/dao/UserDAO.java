package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDao{
    private String jdbcURL = "jdbc:mysql://localhost:3306/user";
    private String jdbcUsername = "root";
    private String jdbcPassword = "761321";

    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

    public UserDAO() {
    }
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            System.out.println("Ket noi thanh cong");
        } catch (SQLException e) {
            System.out.println("Ket noi false");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Ket noi false");
            e.printStackTrace();
        }
        return connection;
    }
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        //câu lệnh try-with-resource sẽ tự động đóng kết nối.
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public User selectUser(int id) {
        User user = null;
        // Bước 1: Thiết lập kết nối
        try (Connection connection = getConnection();
             //Bước 2:Tạo một câu lệnh bằng cách sử dụng đối tượng kết nối
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Bước 3: Thực thi truy vấn hoặc cập nhật truy vấn
            ResultSet rs = preparedStatement.executeQuery();

            // Bước 4: Xử lý đối tượng ResultSet
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }
    public List<User> selectAllUsers() {

        // sử dụng try-with-resources để tránh đóng tài nguyên
        List<User> users = new ArrayList<>();
        // Step 1: Thiết lập kết nối
        try (Connection connection = getConnection();

             // Step 2:Tạo một câu lệnh bằng cách sử dụng đối tượng kết nối
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            System.out.println(preparedStatement);
            // Step 3: Thực thi truy vấn hoặc cập nhật truy vấn
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Xử lý đối tượng ResultSet
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return users;
    }
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Ma loi: " + ((SQLException) e).getErrorCode());
                System.err.println("Thong bao: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Sinh ra: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
