package database_instruments;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class DbTableForm extends JFrame {

    final PosgtresDB db;
    // String tableName;
    final JTable table;
    final DefaultTableModel tableModel;
    final JButton btnDelete;
    final JButton btnDrop;
    final JTextField inputDelete;

    final JComboBox<String> cmbTable;
    final JComboBox<String> cmbDeleteField;


    DbTableForm(PosgtresDB db, String tableName) {
        this.db = db;
        // this.tableName = tableName;

        setLayout(null);
        setSize(700, 400);
        setVisible(true);
        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        setTitle("Text Editor. Rezyapov D.N. Вариант 5  " + sd.format(new Date()));

        JPanel panel1 = new JPanel();
        panel1.setBounds(20, 50, 660, 230);
        panel1.setVisible(true);
        add(panel1);

        JLabel lblTable = new JLabel("Table");
        lblTable.setBounds(0, 5, 50, 20);
        add(lblTable);

        cmbTable = new JComboBox<>();
        cmbTable.setBounds(60, 10, 60, 20);
        add(cmbTable);


        JLabel lblDeletedField = new JLabel("Поле для удаления");
        lblDeletedField.setBounds(130, 10, 100, 20);
        add(lblDeletedField);

        cmbDeleteField = new JComboBox<>();
        cmbDeleteField.setBounds(240, 10, 80, 20);
        add(cmbDeleteField);

        JLabel lblInput = new JLabel("Значеине удаляемого");
        lblInput.setBounds(330, 10, 100, 20);
        add(lblInput);

        inputDelete = new JTextField();
        inputDelete.setBounds(440, 10, 100, 20);
        add(inputDelete);

        btnDelete = new JButton("Удалить строк");
        btnDelete.setBounds(550, 10, 90, 20);
        add(btnDelete);

        tableModel = new DefaultTableModel();

        table = new JTable(tableModel);
        panel1.add(new JScrollPane(table));

        btnDrop = new JButton("Drop table");
        btnDrop.setBounds(550, 330, 90, 20);
        add(btnDrop);

        //this.fillTable();
        loadTables(tableName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        btnDrop.addActionListener(this::dropTable);
        btnDelete.addActionListener(this::delete);
        cmbTable.addActionListener((event) -> fillTable());
    }

    void loadTables(String tableName) {
        try {
            ArrayList<String> tables = db.getTableNames();
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbTable.getModel();
            model.removeAllElements();
            for (String table : tables) {
                model.addElement(table);
            }
            if (tableName != null) {
                model.setSelectedItem(tableName);
            }

            cmbTable.setModel(model);
            this.fillTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void dropTable(ActionEvent event) {
        String tableName = (String) cmbTable.getSelectedItem();
        int dialogResult = JOptionPane.showConfirmDialog(this, "Точно удалить таблицу?");
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                db.dropTable(tableName);
                JOptionPane.showMessageDialog(this, "OK", "Dropped", JOptionPane.INFORMATION_MESSAGE);
                loadTables(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void delete(ActionEvent event) {
        String deleteCondition = inputDelete.getText();
        String tableName = (String) cmbTable.getSelectedItem();
        String deleteField = (String) cmbDeleteField.getSelectedItem();
        if (deleteCondition == null || deleteCondition.equals("")
                || deleteField == null || deleteField.equals("")) {
            JOptionPane.showMessageDialog(this, "Введите фамилию кого удалять");
            return;
        }
        try {
            db.delete(tableName, deleteField, deleteCondition);
            JOptionPane.showMessageDialog(this, "OK", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            fillTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void fillTable() {
        try {
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }
            String tableName = (String) cmbTable.getSelectedItem();
            Map<String, ArrayList<Object>> data = db.select(tableName);
            String[] columnNames = (String[]) data.keySet().toArray();
            int rows = data.get(columnNames[0]).size();
            int columns = columnNames.length;
            Object[][] values = new Object[rows][];
            for (int i = 0; i < rows; i++) {
                values[i] = new Object[columns];
                for (int j = 0; j < columns; j++) {
                    values[i][j] = data.get(columnNames[j]).get(i);
                }
            }
            tableModel.setColumnIdentifiers(columnNames);
            // Наполнение модели данными
            for (Object[] value : values) {
                tableModel.addRow(value);
            }

            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) cmbDeleteField.getModel();
            model.removeAllElements();
            for (Object columnName : columnNames) {
                model.addElement((String) columnName);
            }
            cmbDeleteField.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
