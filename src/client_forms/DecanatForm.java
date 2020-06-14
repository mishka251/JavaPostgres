package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DecanatForm extends JFrame {
    PosgtresDB db;
    int user_id;

//    JComboBox<String> group;
//    JComboBox<String> subject;

    JComboBox<Integer> register;

//    Integer[] subject_ids;
//    Integer[] group_ids;

    Report report;

    JLabel lblExcellent;
    JLabel lblStriker;
    JLabel lblThreesome;
    JLabel lblLooser;

    DecanatForm(PosgtresDB db, int id) {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.db = db;
        this.user_id = id;

        JLabel lblGroups = new JLabel("Группа");
        lblGroups.setBounds(10, 10, 100, 40);
        add(lblGroups);

        try {
            Map<String, ArrayList<Object>> groups =
                    db.select("register");
            // group_ids = Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class);
            register = new JComboBox<Integer>(Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class));
            register.setBounds(130, 10, 100, 30);
            add(register);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JLabel lblSubj = new JLabel("Предмет");
        lblSubj.setBounds(250, 10, 100, 40);
        add(lblSubj);

//        try {
//            Map<String, ArrayList<Object>> subjects =
//                    db.select("subjects");
//            subject_ids = Arrays.copyOf(subjects.get("id").toArray(), subjects.get("id").size(), Integer[].class);
//            subject = new JComboBox<>(Arrays.copyOf(subjects.get("name").toArray(), subjects.get("name").size(), String[].class));
//            subject.setBounds(380, 10, 100, 30);
//            add(subject);
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, ex.getMessage());
//        }

        JButton btnLoad = new JButton("Load");
        btnLoad.setBounds(390, 40, 100, 30);
        add(btnLoad);
        btnLoad.addActionListener(event -> this.loadReport());

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(390, 80, 100, 30);
        add(btnSave);
        btnSave.addActionListener(event -> this.saveReport());


        lblExcellent = new JLabel();
        lblExcellent.setBounds(10, 100, 100, 20);
        lblExcellent.setVisible(false);
        add(lblExcellent);

        lblStriker = new JLabel();
        lblStriker.setBounds(10, 130, 100, 20);
        lblStriker.setVisible(false);
        add(lblStriker);


        lblThreesome = new JLabel();
        lblThreesome.setBounds(10, 160, 100, 20);
        lblThreesome.setVisible(false);
        add(lblThreesome);


        lblLooser = new JLabel();
        lblLooser.setBounds(10, 190, 100, 20);
        lblLooser.setVisible(false);
        add(lblLooser);


        setSize(500, 400);
        setVisible(true);
    }

    void loadReport() {
        //int groupId = group_ids[group.getSelectedIndex()];
        int registerId = (int) register.getSelectedItem();
        try {
            this.report = new Report(db, registerId);
            this.report.calculateStatistic();

            lblExcellent.setText("5 - " + (report.excellentPercent * 100) + "%");
            lblStriker.setText("4 - " + (report.strikerPercent * 100) + "%");
            lblThreesome.setText("3 - " + (report.threesomePercent * 100) + "%");
            lblLooser.setText("2 - " + (report.looserPercent * 100) + "%");

            lblExcellent.setVisible(true);
            lblStriker.setVisible(true);
            lblThreesome.setVisible(true);
            lblLooser.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }


    }

    void saveReport() {
        try {
            report.saveToDb();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

}
