import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void createTable(String tableName, TableColumn[] columns) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(tableName);
        sql.append("(");


        for (int i = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            sql.append(column.name);
            sql.append(" ");
            sql.append(column.type);
            if (i != columns.length - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        Statement statement = connection.createStatement();

        statement.execute(sql.toString());
        statement.close();
    }

    public void insert(String tableName, String[] columnNames, Object[] values) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append("(");


        for (int i = 0; i < columnNames.length; i++) {
            sql.append(columnNames[i]);
            if (i != columnNames.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(") VALUES (");

        for (int i = 0; i < columnNames.length; i++) {
            sql.append("?");
            if (i != columnNames.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        for (int i = 0; i < columnNames.length; i++) {
            if (values[i] instanceof java.util.Date) {
                statement.setObject(i + 1, values[i], Types.DATE);
            } else {
                statement.setObject(i + 1, values[i]);
            }
        }
        statement.execute();
        statement.close();
    }

    void delete(String tableName, String columnName, Object value) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(tableName);
        sql.append(" WHERE ");
        sql.append(columnName);
        sql.append("=?");
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        if (value instanceof java.util.Date) {
            statement.setObject(1, value, Types.DATE);
        } else {
            statement.setObject(1, value);
        }
        statement.execute();
        statement.close();
    }

    public void dropTable(String table) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("DROP TABLE ");
        sql.append(table);

        PreparedStatement statement = connection.prepareStatement(sql.toString());

        statement.execute();
        statement.close();
    }

    public Map<String, ArrayList<Object>> select(String table) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(table);
        PreparedStatement statement = connection.prepareStatement(sql.toString());

        ResultSet resultSet = statement.executeQuery();
        Map<String, ArrayList<Object>> results = new HashMap<>();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();

// The column count starts from 1
        for (int i = 1; i <= columnCount; i++) {
            String name = rsmd.getColumnName(i);
            results.put(name, new ArrayList<>());
        }

        while(resultSet.next()){
            for(String columnName: results.keySet()){
                Object value = resultSet.getObject(columnName);
                results.get(columnName).add(value);
            }
        }

        // for(Strint column: results.)
        statement.close();
        return results;
    }
}
