package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

public class LoginForm extends JFrame {
    final JTextField loginField;
    final JTextField passwordField;

    final JButton btnLogin;

    final PosgtresDB db;

    public LoginForm(PosgtresDB db) {
        this.db = db;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        setSize(120, 210);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(10, 10, 100, 20);
        add(lblLogin);

        loginField = new JTextField();
        loginField.setBounds(10, 40, 100, 20);
        add(loginField);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(10, 70, 100, 20);
        add(lblPassword);

        passwordField = new JTextField();
        passwordField.setBounds(10, 100, 100, 20);
        add(passwordField);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(20, 130, 80, 20);
        add(btnLogin);

        btnLogin.addActionListener(this::login);
        setVisible(true);
    }

    void login(ActionEvent event) {
        try {
            Map<String, ArrayList<Object>> result = db.selectWhere("USER", "login='" + loginField.getText() + "'");
            ArrayList<Object> password = result.get("password");
            if (password.size() == 0) {
                JOptionPane.showMessageDialog(this, "No user with this login");
                return;
            }

            if (!passwordField.getText().equals(password.get(0))) {
                JOptionPane.showMessageDialog(this, "Wrong password");
                return;
            }
            int id = (int) result.get("id").get(0);
            JOptionPane.showMessageDialog(this, "Ok");
            if ("teacher".equals(result.get("position").get(0))) {
                new TeacherForm(db, id);
            } else if ("decanat".equals(result.get("position").get(0))) {
                new DecanatForm(db, id);
            } else {
                JOptionPane.showMessageDialog(this, "Неопределенный тип сотрудика");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
