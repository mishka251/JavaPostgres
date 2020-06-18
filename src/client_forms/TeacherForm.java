package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

public class TeacherForm extends JFrame {

    final PosgtresDB db;

    JComboBox<String> group;
    JComboBox<String> subject;
    final int teacher_id;

    Integer[] group_ids;
    Integer[] subject_ids;
    Register register = null;

    final ArrayList<StudentPanel> studentPanels = new ArrayList<>();

    TeacherForm(PosgtresDB db, int teacher_id) {
        setLayout(null);
        this.teacher_id = teacher_id;
        this.db = db;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblGroups = new JLabel("Группа");
        lblGroups.setBounds(10, 10, 100, 40);
        add(lblGroups);

        try {
            Map<String, ArrayList<Object>> groups =
                    db.select("group");
            group_ids = Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class);
            group = new JComboBox<>(Arrays.copyOf(groups.get("name").toArray(), groups.get("name").size(), String[].class));
            group.setBounds(130, 10, 100, 30);
            add(group);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JLabel lblSubj = new JLabel("Предмет");
        lblSubj.setBounds(250, 10, 100, 40);
        add(lblSubj);

        try {
            Map<String, ArrayList<Object>> subjects =
                    db.select("subjects");
            subject_ids = Arrays.copyOf(subjects.get("id").toArray(), subjects.get("id").size(), Integer[].class);
            subject = new JComboBox<>(Arrays.copyOf(subjects.get("name").toArray(), subjects.get("name").size(), String[].class));
            subject.setBounds(380, 10, 100, 30);
            add(subject);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JButton btnLoad = new JButton("Load");
        btnLoad.setBounds(390, 40, 100, 30);
        add(btnLoad);
        btnLoad.addActionListener(event -> this.loadStudents());

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(390, 80, 100, 30);
        add(btnSave);
        btnSave.addActionListener(event -> this.saveMarks());

        setSize(500, 400);
        setVisible(true);


    }


    void loadStudents() {
        int groupId = group_ids[group.getSelectedIndex()];
        int subjectId = subject_ids[subject.getSelectedIndex()];
        register = new Register(db, teacher_id, groupId, subjectId);
        try {
            register.loadFromDb();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        studentPanels.clear();

        for (int i = 0; i < register.studentNames.length; i++) {
            StudentPanel panel = new StudentPanel(register.studentNames[i], register.studentHasMark[i], register.studentMarks[i]);
            panel.setBounds(10, 40 + 40 * i, 250, 30);
            add(panel);
            studentPanels.add(panel);
        }
        repaint();
    }

    void saveMarks() {
        if (this.register == null) {
            JOptionPane.showMessageDialog(this, "Не загружены студенты");
            return;
        }
        if (register.isSaved) {
            JOptionPane.showMessageDialog(this, "Нельзя менять поставленные оценки");
            return;
        }

        ErrorChecker checker = new ErrorChecker();
        ArrayList<String> errors = checker.checkTeacherForm(this);
        if (errors.size() > 0) {
            JOptionPane.showMessageDialog(this, String.join(",\n", errors), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.register.studentMarks = new Integer[studentPanels.size()];
        for (int i = 0; i < studentPanels.size(); i++) {
            this.register.studentMarks[i] = studentPanels.get(i).getMark();
        }


        try {
            this.register.saveMarks();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
        this.loadStudents();
    }
}

class StudentPanel extends JPanel {
    final JLabel lbl;
    final JTextField fields;

    StudentPanel(String studentName, boolean isReadOnly, Integer mark) {
        setLayout(null);
        lbl = new JLabel(studentName);
        lbl.setBounds(10, 10, 100, 20);
        add(lbl);

        fields = new JTextField();
        fields.setBounds(120, 10, 100, 20);
        fields.setEditable(!isReadOnly);
        if (mark != null) {
            fields.setText(mark.toString());
        }

        add(fields);
        setVisible(true);
    }

    int getMark() {
        return Integer.parseInt(fields.getText());
    }
}