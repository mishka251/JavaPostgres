package client_forms;

import database_instruments.PosgtresDB;

import javax.swing.*;

public class StudentsRatingForm extends JFrame {

    PosgtresDB db;
    int user_id;

    StudentsRatingForm(PosgtresDB db, int user_id){
        this.db=db;
        this.user_id=user_id;


    }
}
