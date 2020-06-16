package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class DecanatForm extends JFrame {
    PosgtresDB db;
    int user_id;

    JComboBox<Integer> register;

    Report report;

    JTextPane reportEditor;

    JFileChooser fileChooser;


    DecanatForm(PosgtresDB db, int id) {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.db = db;
        this.user_id = id;

        JLabel lblGroups = new JLabel("Ведомость №");
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


        JButton btnLoad = new JButton("Load");
        btnLoad.setBounds(390, 40, 100, 30);
        add(btnLoad);
        btnLoad.addActionListener(event -> this.loadReport());

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(390, 80, 100, 30);
        add(btnSave);
        btnSave.addActionListener(event -> this.saveReport());


        reportEditor = new JTextPane();
        reportEditor.setBounds(10, 100, 400, 300);
        add(reportEditor);

        fileChooser = new JFileChooser();

        setSize(500, 400);
        setVisible(true);
    }

    void loadReport() {
        //int groupId = group_ids[group.getSelectedIndex()];
        int registerId = (int) register.getSelectedItem();
        try {
            this.report = new Report(db, registerId);
            this.report.calculateStatistic();
            ReportFormatter formatter = new ReportFormatter(report);
            reportEditor.setText(formatter.format());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }


    }

    void saveReport() {
        ErrorChecker checker = new ErrorChecker();
        ArrayList<String> errors = checker.checkDecanatForm(this);
        if (errors.size() > 0) {
            JOptionPane.showMessageDialog(this, String.join(",\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            report.saveToDb();
            int dialogResult = fileChooser.showSaveDialog(this);
            if (dialogResult != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File selected = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(selected);
                fw.write(reportEditor.getText());
                fw.close();
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

}
