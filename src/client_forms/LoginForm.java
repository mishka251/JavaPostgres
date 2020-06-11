package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

public class LoginForm extends JFrame {
    JTextField loginField;
    JTextField passwordField;

    JButton btnLogin;

    PosgtresDB db;

    public LoginForm(PosgtresDB db) {
        this.db = db;
        setLayout(null);

        setSize(120, 160);

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


        setVisible(true);
    }

    void login(ActionEvent event) {
        try {
            Map<String, ArrayList<Object>> result = db.selectWhere("USERS", "login=" + loginField.getText());
            ArrayList<Object> password = result.get("password");
            if (password.size() == 0) {
                JOptionPane.showMessageDialog(this, "No user with this login");
                return;
            }
            
            if (!passwordField.getText().equals(password.get(0))) {
                JOptionPane.showMessageDialog(this, "Wrong password");
                return;
            }

            JOptionPane.showMessageDialog(this, "Ok");
            //TODO open form for user type
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}