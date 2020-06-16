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
            "_форма_контроля_ был проведен _дата_\n";

    ReportFormatter(Report report) {
        this.report = report;
    }

    String format() {
        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        return template
                .replace("_группа_", report.groupNumber + report.groupSpeciality)
                .replace("_предмет_", this.report.subjectName)
                .replace("_отличников_", (this.report.excellentPercent * 100) + "%")
                .replace("_ударников_", (this.report.strikerPercent * 100) + "%")
                .replace("_троечников_", (this.report.threesomePercent * 100) + "%")
                .replace("_двоечников_", (this.report.looserPercent * 100) + "%")
                .replace("_форма_контроля_", this.report.controlType)
                .replace("_дата_", sd.format(this.report.date));
    }
}
