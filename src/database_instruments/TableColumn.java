package database_instruments;

public class TableColumn {
    final public String name;
    final public String type;

    public TableColumn(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TableColumn{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
