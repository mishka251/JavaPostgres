package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

public class TeacherForm extends JFrame {

    PosgtresDB db;

    JComboBox<String> group;
    JComboBox<String> subject;
    int teacher_id;

    Integer[] group_ids;
    Integer[] subject_ids;
    Register register = null;

    TeacherForm(PosgtresDB db, int teacher_id) {
        setLayout(null);
        this.teacher_id = teacher_id;
        this.db = db;

        JLabel lblGroups = new JLabel("Группа");
        lblGroups.setBounds(10, 10, 100, 40);
        add(lblGroups);

        try {
            Map<String, ArrayList<Object>> groups =
                    db.select("group");
            group_ids = Arrays.copyOf(groups.get("id").toArray(), groups.get("id").size(), Integer[].class);
            group = new JComboBox<String>(Arrays.copyOf(groups.get("name").toArray(), groups.get("name").size(), String[].class));
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
        btnLoad.setBounds(10, 30, 100, 30);
add(btnLoad);
btnLoad.addActionListener(event->this.loadStudents());
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

for(int i =0; i<register.studentNames.length; i++){

}
    }

}

class StudentPanel extends JPanel{
    JLabel lbl;
    JTextField fields;

    StudentPanel(String studenName){
        setLayout(null);
        lbl = new JLabel(studenName);
        lbl.setBounds(10, 10, 100, 20);
        add(lbl);

        fields = new JTextField();
        fields.setBounds(120, 10, 100, 20);
        add(fields);
        setVisible(true);
    }

    int getMark(){
        return Integer.parseInt(fields.getText());
    }
}