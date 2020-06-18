package client_forms;

import database_instruments.DbTableForm;
import database_instruments.PosgtresDB;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DecanatForm extends JFrame {
    final PosgtresDB db;
    final int user_id;

    DecanatForm(PosgtresDB db, int id) {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.db = db;
        this.user_id = id;
        setTitle("Форма сотрудника деканата");

//        JLabel lblGroups = new JLabel("Ведомость №");
//        lblGroups.setBounds(10, 10, 100, 40);
//        add(lblGroups);

//        try {
//            Map<String, ArrayList<Object>> groups =
//                    db.select("register");
//            // group_ids = Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class);
//            register = new JComboBox<>(Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class));
//            register.setBounds(130, 10, 100, 30);
//            add(register);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, ex.getMessage());
//        }
//
//
//        JButton btnLoad = new JButton("Load");
//        btnLoad.setBounds(390, 40, 100, 30);
//        add(btnLoad);
//        btnLoad.addActionListener(event -> this.loadReport());
//
//        JButton btnSave = new JButton("Save");
//        btnSave.setBounds(390, 80, 100, 30);
//        add(btnSave);
//        btnSave.addActionListener(event -> this.saveReport());


//        reportEditor = new JTextPane();
//        reportEditor.setBounds(10, 100, 400, 300);
//        add(reportEditor);
//
//        fileChooser = new JFileChooser();


        JButton btnStudentsTable = new JButton("Students");
        btnStudentsTable.setBounds(10, 10, 100, 20);
        add(btnStudentsTable);

        JButton btnGroups = new JButton("Groups");
        btnGroups.setBounds(10, 40, 100, 20);
        add(btnGroups);

        JButton btnMarks = new JButton("Marks");
        btnMarks.setBounds(10, 70, 100, 20);
        add(btnMarks);

        JButton btnSubjects = new JButton("Subjects");
        btnSubjects.setBounds(10, 100, 100, 20);
        add(btnSubjects);

        JButton btnReport = new JButton("Report");
        btnReport.setBounds(10, 130, 100, 20);
        add(btnReport);

        JButton btnRating = new JButton("Rating");
        btnRating.setBounds(10, 160, 100, 20);
        add(btnRating);


        btnGroups.addActionListener((event) -> this.changeGroup());
        btnMarks.addActionListener((event) -> this.changeMarks());
        btnSubjects.addActionListener((event) -> this.changeSubjects());
        btnStudentsTable.addActionListener((event) -> this.addStudents());
        btnReport.addActionListener((event) -> this.showReport());
        btnRating.addActionListener((event) -> this.showRating());


        setSize(500, 400);
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
