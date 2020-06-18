package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class UspevReportForm extends JFrame {
    final PosgtresDB db;
    final int user_id;

    JComboBox<Integer> register;

    Report report;

    final JTextPane reportEditor;

    final JFileChooser fileChooser;
    JScrollPane scroll;

    UspevReportForm(PosgtresDB db, int id) {
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Отчёт по успеваемости");
        this.db = db;
        this.user_id = id;

        JLabel lblGroups = new JLabel("Ведомость №");
        lblGroups.setBounds(10, 10, 100, 40);
        add(lblGroups);

        try {
            Map<String, ArrayList<Object>> groups = db.select("register");
            register = new JComboBox<>(Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class));
            register.setBounds(130, 10, 100, 30);
            add(register);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }


        JButton btnLoad = new JButton("Загрузить из БД");
        btnLoad.setBounds(320, 10, 150, 30);
        add(btnLoad);
        btnLoad.addActionListener(event -> this.loadReport());

        JButton btnSave = new JButton("Сохранить в БД");
        btnSave.setBounds(320, 50, 150, 30);
        add(btnSave);
        btnSave.addActionListener(event -> this.saveReport());


        reportEditor = new JTextPane();
        reportEditor.setBounds(10, 100, 400, 300);
        add(reportEditor);

        scroll = new JScrollPane(reportEditor);
        add(scroll);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(10, 60, 300, 300);

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
            scroll.setViewportView(reportEditor);
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
