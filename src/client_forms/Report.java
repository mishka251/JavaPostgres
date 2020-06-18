package client_forms;

import database_instruments.PosgtresDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class Report {
    final PosgtresDB db;

    int groupId;
    int subjectId;

    Integer[] studentIds;
    String[] studentNames;
    double[] studentMarks;

    double excellentPercent;
    double strikerPercent;
    double threesomePercent;
    double looserPercent;

    String groupNumber;
    String groupSpeciality;
    String subjectName;

    final int registerId;
    Date date;
    String controlType;


    Report(PosgtresDB db, int registerId) throws SQLException {
        this.db = db;
        this.registerId = registerId;
        Map<String, ArrayList<Object>> registerTable = db.selectWhere("register", "id=" + registerId);


        this.groupId = (Integer) registerTable.get("group_id").get(0);
        this.subjectId = (Integer) registerTable.get("subject_id").get(0);
        controlType = (String) registerTable.get("control_type").get(0);
        date = new Date((Long) registerTable.get("date").get(0));// (Date) registerTable.get("date").get(0);


        Map<String, ArrayList<Object>> studentsTable = db.selectWhere("student", "surname", "ASC", "group_id=" + groupId);
        studentNames = Arrays.copyOf(studentsTable.get("name").toArray(), studentsTable.get("name").size(), String[].class);
        studentIds = Arrays.copyOf(studentsTable.get("id").toArray(), studentsTable.get("id").size(), Integer[].class);

        Map<String, ArrayList<Object>> groupTable = db.selectWhere("group", "id=" + groupId);
        groupNumber = (String) groupTable.get("number").get(0);
        groupSpeciality = (String) groupTable.get("speciality").get(0);

        Map<String, ArrayList<Object>> subjectTable = db.selectWhere("subjects", "id=" + subjectId);
        subjectName = (String) subjectTable.get("name").get(0);
    }


    void calculateStatistic() throws SQLException {
        looserPercent = 0;
        threesomePercent = 0;
        strikerPercent = 0;
        excellentPercent = 0;

        studentMarks = new double[studentNames.length];

        for (int i = 0; i < studentNames.length; i++) {
            Map<String, ArrayList<Object>> studentMarks = db.selectWhere("student_mark_in_register",
                    "student_id=" + studentIds[i] + " AND register_id=" + registerId);
            double mid = 0;
            for (int j = 0; j < studentMarks.get("mark").size(); j++) {
                mid += (int) studentMarks.get("mark").get(j);
            }
            mid /= studentMarks.get("mark").size();
            if (mid > 4.7) {
                excellentPercent++;
            } else if (mid >= 4) {
                strikerPercent++;
            } else if (mid >= 3) {
                threesomePercent++;
            } else {
                looserPercent++;
            }
            this.studentMarks[i] = mid;
        }
        excellentPercent /= studentNames.length;
        strikerPercent /= studentNames.length;
        threesomePercent /= studentNames.length;
        looserPercent /= studentNames.length;

        for (int i = 0; i < this.studentNames.length; i++) {
            for (int j = i + 1; j < studentNames.length; j++) {
                if (studentMarks[j] > studentMarks[i]) {
                    double tmp = studentMarks[i];
                    studentMarks[i] = studentMarks[j];
                    studentMarks[j] = tmp;

                    int tmp2 = studentIds[i];
                    studentIds[i] = studentIds[j];
                    studentIds[j] = tmp2;

                    String tmp3 = studentNames[i];
                    studentNames[i] = studentNames[j];
                    studentNames[j] = tmp3;
                }
            }
        }
    }

    void saveToDb() throws SQLException {
        db.insert("report",
                new String[]{
                        "subject_id",
                        "group_id",
                        "excellent_percent",
                        "strikers_percent",
                        "threesome_percent",
                        "loosers_percent"
                },
                new Object[]{
                        subjectId,
                        groupId,
                        excellentPercent * 100,
                        strikerPercent * 100,
                        threesomePercent * 100,
                        looserPercent * 100
                });
    }

}
