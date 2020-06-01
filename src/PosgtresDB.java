import java.sql.*;

public class PosgtresDB {
    String dbUrl = "jdbc:postgresql://localhost/laba9Db";
    String login = "Laba9Role";
    String password = "laba9_password";
    Connection connection;

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");
        connection = null;

        try {
            connection = DriverManager
                    .getConnection(dbUrl, login, password);

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
    }

    public void createTable(String tableName, TableColumn[] columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(tableName);
        sql.append("(");


        for (int i = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            sql.append(column.name);
            sql.append(" ");
            sql.append(column.type);
            if(i!=columns.length-1){
                sql.append(",");
            }
        }
        sql.append(")");
        try {
            Statement statement = connection.createStatement();

            statement.execute(sql.toString());
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
