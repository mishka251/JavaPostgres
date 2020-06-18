package client_forms;

import java.text.SimpleDateFormat;

public class ReportFormatter {
    final Report report;

    final static String template = "Отчёт по группе _группа_\n" +
            "по предмету _предмет_\n" +
            "Отличников - _отличников_\n" +
            "Ударников - _ударников_\n" +
            "Троешников - _троечников_\n" +
            "Двоечников - _двоечников_\n" +
            "_студенты_"+
            "_форма_контроля_ был проведен _дата_\n";

    ReportFormatter(Report report) {
        this.report = report;
    }

    String format() {
        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        StringBuilder students = new StringBuilder();
        for(int i=0; i<report.studentNames.length; i++){
            students.append(report.studentNames[i]);
            students.append(" - ");
            students.append(report.studentMarks[i]);
            students.append("\n");
        }
        return template
                .replace("_группа_", report.groupNumber + report.groupSpeciality)
                .replace("_предмет_", report.subjectName)
                .replace("_отличников_", (report.excellentPercent * 100) + "%")
                .replace("_ударников_", (report.strikerPercent * 100) + "%")
                .replace("_троечников_", (report.threesomePercent * 100) + "%")
                .replace("_двоечников_", (report.looserPercent * 100) + "%")
                .replace("_форма_контроля_", report.controlType)
                .replace("_дата_", sd.format(report.date))
                .replace("_студенты_", students);
    }
}
