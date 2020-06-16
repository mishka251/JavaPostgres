package database_instruments;

import client_forms.LoginForm;

public class Main {
    public static void main(String[] args) {
        PosgtresDB db = new PosgtresDB();
        db.connect();
        new LoginForm(db);
    }
}
