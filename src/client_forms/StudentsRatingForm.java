package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class StudentsRatingForm extends JFrame {

    PosgtresDB db;
    int user_id;

    final JPanel studentsPanel;

    final ArrayList<StudentInfoPanel> studentPanels = new ArrayList<>();
    final JScrollPane scroll;

    Integer[] group_ids;
    JComboBox<String> group;

    StudentsRatingForm(PosgtresDB db, int user_id) {
        this.db = db;
        this.user_id = user_id;
setTitle("Рейтинг студентов");
        setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel lblGroups = new JLabel("Группа");
        lblGroups.setBounds(10, 10, 100, 40);
        add(lblGroups);

        try {
            Map<String, ArrayList<Object>> groups =
                    db.select("group");
            group_ids = Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class);


            group = new JComboBox<>(Arrays.copyOf(groups.get("name").toArray(), groups.get("name").size(), String[].class));
            group.addItem("все");
            group.setBounds(130, 10, 100, 30);
            add(group);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JButton btnLoad = new JButton("Загрузить рейтинг");
        btnLoad.setBounds(320, 40, 150, 30);
        add(btnLoad);
        btnLoad.addActionListener(event -> this.loadStudents());


        setSize(500, 400);
        setVisible(true);

        studentsPanel = new JPanel();
        studentsPanel.setLayout(null);

        scroll = new JScrollPane(studentsPanel);
        add(scroll);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(10, 50, 300, 250);
    }

    void loadStudents() {
        for (StudentInfoPanel panel : studentPanels) {
            studentsPanel.remove(panel);
        }
        repaint();
        studentPanels.clear();
        try {
            Map<String, ArrayList<Object>> students;
            if (("все").equals(group.getSelectedItem())) {
                students = db.select("student");
            } else {
                int groupId = group_ids[group.getSelectedIndex()];
                students = db.selectWhere("student", "group_id=" + groupId);
            }

            Integer[] studentIds = Arrays.copyOf(students.get("id").toArray(), students.get("id").size(), Integer[].class);
            String[] studentNames = new String[students.get("id").size()];
            double[] studentMarks = new double[students.get("id").size()];

            for (int i = 0; i < studentNames.length; i++) {

                String name = students.get("surname").get(i) + " " + students.get("name").get(i);
                studentNames[i] = name;

                Map<String, ArrayList<Object>> studentMarksTable = db.selectWhere("student_mark_in_register",
                        "student_id=" + studentIds[i]);
                double mid = 0;
                for (int j = 0; j < studentMarksTable.get("mark").size(); j++) {
                    mid += (int) studentMarksTable.get("mark").get(j);
                }

                if (studentMarksTable.get("mark").size() != 0) {
                    mid /= studentMarksTable.get("mark").size();
                }

                studentMarks[i] = mid;
            }

            for (int i = 0; i < studentMarks.length; i++) {
                for (int j = i + 1; j < studentMarks.length; j++) {
                    if (studentMarks[j] > studentMarks[i]) {
                        double tmp = studentMarks[j];
                        studentMarks[j] = studentMarks[i];
                        studentMarks[i] = tmp;

                        String tmp2 = studentNames[i];
                        studentNames[i] = studentNames[j];
                        studentNames[j] = tmp2;
                    }
                }
            }


            for (int i = 0; i < studentMarks.length; i++) {
                StudentInfoPanel panel = new StudentInfoPanel(studentNames[i], studentMarks[i]);
                panel.setBounds(5, 30 * i, 250, 20);
                studentsPanel.add(panel);
                studentPanels.add(panel);
            }
            studentsPanel.setPreferredSize(new Dimension(300, 40 + 30 * studentNames.length));
            scroll.setViewportView(studentsPanel);
            repaint();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}

class StudentInfoPanel extends JPanel {
    StudentInfoPanel(String name, double mark) {
        super();
        setLayout(null);
        JLabel lbl = new JLabel(name + " - " + mark);
        lbl.setBounds(0, 0, 200, 20);
        add(lbl);
        setVisible(true);
    }
}
