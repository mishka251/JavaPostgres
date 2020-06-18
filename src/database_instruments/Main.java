package database_instruments;

import client_forms.LoginForm;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Random;

public class Main {

    static void createDB(PosgtresDB db) throws SQLException {
        db.createTable("faculty",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("name", "VARCHAR (100)"),
                });

        db.createTable("department",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("name", "VARCHAR (100)"),
                        new TableColumn("faculty_id", "INTEGER", "REFERENCES faculty (id)")
                });

        db.createTable("group",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("name", "VARCHAR (100)"),
                        new TableColumn("speciality", "VARCHAR (100)"),
                        new TableColumn("number", "VARCHAR (100)"),
                        new TableColumn("word_code", "VARCHAR (100)"),
                        new TableColumn("number_code", "VARCHAR (100)"),
                        new TableColumn("department_id", "INTEGER", "REFERENCES department (id)")
                });

        db.createTable("student",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("name", "VARCHAR (100)"),
                        new TableColumn("surname", "VARCHAR (100)"),
                        new TableColumn("patronymic", "VARCHAR (100)"),
                        new TableColumn("no_zk", "VARCHAR (100)"),
                        new TableColumn("group_id", "INTEGER", "REFERENCES [group] (id)")
                });

        db.createTable("subjects",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("name", "VARCHAR (100)"),
                });

        db.createTable("user",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("name", "VARCHAR (100)"),
                        new TableColumn("surname", "VARCHAR (100)"),
                        new TableColumn("patronymic", "VARCHAR (100)"),
                        new TableColumn("position", "VARCHAR (100)"),
                        new TableColumn("email", "VARCHAR (100)"),
                        new TableColumn("phone", "VARCHAR (100)"),
                        new TableColumn("login", "VARCHAR (100)"),
                        new TableColumn("password", "VARCHAR (100)"),
                });

        db.createTable("register",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("control_type", "VARCHAR (100)"),
                        new TableColumn("date", "DATE"),
                        new TableColumn("subject_id", "INTEGER", "REFERENCES subjects (id)"),
                        new TableColumn("group_id", "INTEGER", "REFERENCES [group] (id)"),
                        new TableColumn("teacher_id", "INTEGER", "REFERENCES user (id)"),
                });

        db.createTable("rating",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("number", "INTEGER"),
                        new TableColumn("student_marks", "STRING"),
                        new TableColumn("group_id", "INTEGER", "REFERENCES [group] (id)"),
                });

        db.createTable("report",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("excellent_percent", "DOUBLE"),
                        new TableColumn("strikers_percent", "DOUBLE"),
                        new TableColumn("threesome_percent", "DOUBLE"),
                        new TableColumn("loosers_percent", "DOUBLE"),
                        new TableColumn("group_id", "INTEGER", "REFERENCES [group] (id)"),
                        new TableColumn("subject_id", "INTEGER", "REFERENCES subjects (id)"),
                });

        db.createTable("student_mark_in_register",
                new TableColumn[]{
                        new TableColumn("id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
                        new TableColumn("mark", "INTEGER"),
                        new TableColumn("student_id", "INTEGER", "REFERENCES student (id)"),
                        new TableColumn("register_id", "INTEGER", "REFERENCES register (id)"),
                });
    }

    static void createUsers(PosgtresDB db) throws SQLException {
        db.insert("user", new String[]{
                        "login",
                        "password",
                        "position"
                },
                new Object[]{
                        "teacher",
                        "teacher",
                        "teacher",
                });

        db.insert("user", new String[]{
                        "login",
                        "password",
                        "position",
                },
                new Object[]{
                        "decanat",
                        "decanat",
                        "decanat",
                });

        db.insert("user", new String[]{
                        "login",
                        "password",
                        "position",
                },
                new Object[]{
                        "admin",
                        "admin",
                        "admin",
                });
    }

    static String getRandomName() {
        String[] names = new String[]{
                "Иван", "Петр", "Сидор", "Вася", "Саша", "Азат"
        };
        Random r = new Random();
        return names[r.nextInt(names.length)];
    }

    static String getRandomSurname() {
        String[] names = new String[]{
                "Иванов", "Петров", "Сидоров", "Васильев", "Чернов", "Юсупов"
        };
        Random r = new Random();
        return names[r.nextInt(names.length)];
    }

    static void createStudents(PosgtresDB db) throws SQLException {
        long fac_id = db.insert("faculty", new String[]{"name"}, new Object[]{"ФИРТ"});
        long caf_id = db.insert("department", new String[]{
                        "name",
                        "faculty_id"
                },
                new Object[]{
                        "ВИиК",
                        fac_id
                });

        String[] groupNames = new String[]{
                "ЭАС",
                "ПИ-1",
                "ПИ-2"
        };

        long[] groups = new long[groupNames.length];
        for (int i = 0; i < groupNames.length; i++) {
            groups[i] = db.insert("group", new String[]{
                            "name",
                            "department_id"
                    },
                    new Object[]{
                            groupNames[i],
                            caf_id
                    });
        }


        for (long group : groups) {
            for (int i = 0; i < 20; i++) {
                db.insert("student", new String[]{
                                "name",
                                "surname",
                                "group_id"
                        },
                        new Object[]{
                                getRandomName(),
                                getRandomSurname(),
                                group
                        });
            }
        }


        db.insert("subjects",
                new String[]{"name"},
                new Object[]{"матан"});

        db.insert("subjects",
                new String[]{"name"},
                new Object[]{"физра"});
    }


    public static void main(String[] args) {
        PosgtresDB db = new PosgtresDB();
        db.connect();
        try {
            db.select("user");
        } catch (Exception _ex) {
            try {
                Main.createDB(db);
                Main.createUsers(db);
                Main.createStudents(db);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        new LoginForm(db);
    }
}
