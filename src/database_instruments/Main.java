package database_instruments;

import client_forms.LoginForm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        PosgtresDB db = new PosgtresDB();
        db.connect();
//        try {
//            ArrayList<String> tables = db.getTableNames();
//            System.out.println(tables);
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        try {
//            ArrayList<TableColumn> tables = db.getTableColumnNames("student");
//            System.out.println(tables);
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }

        new LoginForm(db);

//        TableColumn[] columns = new TableColumn[]{
//                new TableColumn("name", "varchar(20)"),
//                new TableColumn("age", "int"),
//                new TableColumn("birthday", "Date")
//        };
//
//            db.createTable("Test1", columns);
//        }catch (SQLException ex){
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }


//        try {
//            db.insert("Test1", new String[]{"name", "age", "birthday"}, new Object[]{"test2", 12, new Date()});
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//        try {
//            db.insert("Test1", new String[]{"name", "age", "birthday"}, new Object[]{"test3", 12, new Date()});
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//        try {
//            db.insert("Test1", new String[]{"name", "age", "birthday"}, new Object[]{"test2", 12, new Date()});
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        try {
//            db.delete("Test1", "name", "test2");
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//        try {
//            db.dropTable("Test2");
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        try {
//            Map<String, ArrayList<Object>> tmp =  db.select("Test1");
//            System.out.println("Data");
//            for (String column: tmp.keySet()){
//                System.out.print(column);
//                System.out.print("    ");
//            }
//            System.out.println();
//            int count = tmp.get(tmp.keySet().toArray()[0]).size();
//            for(int i =0; i<count; i++){
//                for (String column: tmp.keySet()){
//                    System.out.print(tmp.get(column).get(i));
//                    System.out.print("    ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
    }
}
