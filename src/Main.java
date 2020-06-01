public class Main {
    public static void main(String[] args) {
        PosgtresDB db = new PosgtresDB();
        db.connect();
        TableColumn[] columns = new TableColumn[]{
                new TableColumn("name", "varchar(20)"),
                new TableColumn("age", "int")
        };
        db.createTable("Test", columns);
    }
}
