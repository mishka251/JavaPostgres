package client_forms;

import database_instruments.PosgtresDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Ведомость
 */
public class Register {
    int teacherId;
    int groupId;
    int subjectId;

    PosgtresDB db;


    String teacherFio;
    String groupNumber;
    String groupSpeciality;
    String subjectName;

    Integer[] studentMarks;
    Integer[] studentIds;
    String[] studentNames;

    public Register(PosgtresDB db, int teacherId, int groupId, int subjectId) {
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.subjectId = subjectId;

        this.db = db;
    }

    public void loadFromDb() throws SQLException {
        Map<String, ArrayList<Object>> studentsTable = db.selectWhere("student", "surname", "ASC", "group_id=" + groupId);
        studentNames = Arrays.copyOf( studentsTable.get("name").toArray(), studentsTable.get("name").size(), String[].class);
        studentIds = Arrays.copyOf( studentsTable.get("id").toArray(), studentsTable.get("id").size(), Integer[].class);

        Map<String, ArrayList<Object>> groupTable = db.selectWhere("group", "id=" + groupId);
        groupNumber = (String) groupTable.get("number").get(0);
        groupSpeciality = (String) groupTable.get("speciality").get(0);

        Map<String, ArrayList<Object>> teacherTable = db.selectWhere("user", "id=" + teacherId);
        teacherFio = (String) teacherTable.get("surname").get(0) +
                (String) teacherTable.get("name").get(0) +
                (String) teacherTable.get("patronymic").get(0);

        Map<String, ArrayList<Object>> subjectTable = db.selectWhere("subjects", "id=" + teacherId);
        subjectName = (String) subjectTable.get("name").get(0);

    }

    public void saveMarks() throws SQLException {
        String controlType = "examen";
        Date now = new Date();

        long id = db.insert("register",
                new String[]{
                        "subject_id",
                        "control_type",
                        "group_id",
                        "teacher_id",
                        "date"
                },
                new Object[]{
                        subjectId,
                        controlType,
                        groupId,
                        teacherId,
                        teacherId,
                        now,
                });

        for (int i = 0; i < studentIds.length; i++) {
            db.insert("student_mark_in_register", new String[]{"student_id", "register_id", "mark"},
                    new Object[]{studentIds[i], id, studentMarks[i]});
        }
    }

}
