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
    final int teacherId;
    final int groupId;
    final int subjectId;

    final PosgtresDB db;


    String teacherFio;
    String groupNumber;
    String groupSpeciality;
    String subjectName;

    Integer[] studentMarks;
    Integer[] studentIds;
    String[] studentNames;
    Boolean[] studentHasMark;
    boolean isSaved;

    public Register(PosgtresDB db, int teacherId, int groupId, int subjectId) {
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.subjectId = subjectId;

        this.db = db;
    }

    public void loadFromDb() throws SQLException {
        Map<String, ArrayList<Object>> studentsTable = db.selectWhere("student", "surname", "ASC", "group_id=" + groupId);
        studentNames = Arrays.copyOf(studentsTable.get("name").toArray(), studentsTable.get("name").size(), String[].class);
        studentIds = Arrays.copyOf(studentsTable.get("id").toArray(), studentsTable.get("id").size(), Integer[].class);

        Map<String, ArrayList<Object>> groupTable = db.selectWhere("group", "id=" + groupId);
        groupNumber = (String) groupTable.get("number").get(0);
        groupSpeciality = (String) groupTable.get("speciality").get(0);

        Map<String, ArrayList<Object>> teacherTable = db.selectWhere("user", "id=" + teacherId);
        teacherFio = (String) teacherTable.get("surname").get(0) +
                teacherTable.get("name").get(0) +
                teacherTable.get("patronymic").get(0);

        Map<String, ArrayList<Object>> subjectTable = db.selectWhere("subjects", "id=" + subjectId);
        subjectName = (String) subjectTable.get("name").get(0);

        Map<String, ArrayList<Object>> register =
                db.selectWhere("register", "teacher_id=" + teacherId+" AND subject_id="+subjectId+" AND group_id="+groupId);

        isSaved=register.get("id").size()!=0;
        studentMarks = new Integer[studentNames.length];
        studentHasMark = new Boolean[studentNames.length];
        for(int i=0; i<this.studentIds.length; i++){
            studentMarks[i]=null;
            studentHasMark[i]=false;
        }
        if(isSaved){
            int registerId = (int)register.get("id").get(0);

            for(int i=0; i<this.studentIds.length; i++){
                int studId=this.studentIds[i];
                Map<String, ArrayList<Object>> subjectMarkTable =
                        db.selectWhere("student_mark_in_register", "student_id="+studId+" AND register_id="+registerId);

                if(subjectMarkTable.get("mark").size()==0){
                    continue;
                }

                studentMarks[i]=(Integer)subjectMarkTable.get("mark").get(0);
                studentHasMark[i]=true;
            }
        }
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
                        now,
                });

        for (int i = 0; i < studentIds.length; i++) {
            db.insert("student_mark_in_register", new String[]{"student_id", "register_id", "mark"},
                    new Object[]{studentIds[i], id, studentMarks[i]});
        }
        isSaved=true;
        for(int i=0; i<this.studentIds.length; i++){
            studentHasMark[i]=true;
        }
    }

}
