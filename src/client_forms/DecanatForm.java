package client_forms;

import database_instruments.DbTableForm;
import database_instruments.PosgtresDB;

import javax.swing.*;

public class DecanatForm extends JFrame {
    final PosgtresDB db;
    final int user_id;

    DecanatForm(PosgtresDB db, int id) {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.db = db;
        this.user_id = id;
        setTitle("Форма сотрудника деканата");

        JButton btnStudentsTable = new JButton("Редактирвоание студентов");
        btnStudentsTable.setBounds(50, 10, 200, 20);
        add(btnStudentsTable);

        JButton btnGroups = new JButton("Редактирвование групп");
        btnGroups.setBounds(50, 40, 200, 20);
        add(btnGroups);

        JButton btnMarks = new JButton("Редактирвоание оценок");
        btnMarks.setBounds(50, 70, 200, 20);
        add(btnMarks);

        JButton btnSubjects = new JButton("Редактирование предметов");
        btnSubjects.setBounds(50, 100, 200, 20);
        add(btnSubjects);

        JButton btnReport = new JButton("Отчеты");
        btnReport.setBounds(50, 130, 200, 20);
        add(btnReport);

        JButton btnRating = new JButton("Рейтинги");
        btnRating.setBounds(50, 160, 200, 20);
        add(btnRating);


        btnGroups.addActionListener((event) -> this.changeGroup());
        btnMarks.addActionListener((event) -> this.changeMarks());
        btnSubjects.addActionListener((event) -> this.changeSubjects());
        btnStudentsTable.addActionListener((event) -> this.addStudents());
        btnReport.addActionListener((event) -> this.showReport());
        btnRating.addActionListener((event) -> this.showRating());


        setSize(350, 240);
        setVisible(true);
    }


    void addStudents() {
        new DbTableForm(db, "student");
    }

    void changeGroup() {
        new DbTableForm(db, "group");
    }

    void changeSubjects() {
        new DbTableForm(db, "subjects");
    }

    void changeMarks() {
        new DbTableForm(db, "student_mark_in_register", new String[]{"mark"});
    }

    void showReport() {
        new UspevReportForm(db, user_id);
    }

    void showRating() {
        new StudentsRatingForm(db, user_id);
    }
}
