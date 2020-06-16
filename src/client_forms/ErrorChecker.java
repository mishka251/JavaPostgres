package client_forms;

import java.util.ArrayList;

public class ErrorChecker {

    String emptyMarkError = "нет оценки";
    String notNumberMark = "оценка введена не целым числом";
    String incorrectMarkError = "оценка не соответствует пятибальной шкале";

    public ArrayList<String> checkTeacherForm(TeacherForm form) {
        ArrayList<String> errors = new ArrayList<>();
        for (int i = 0; i < form.studentPanels.size(); i++) {
            String mark = form.studentPanels.get(i).fields.getText();
            String studentName = form.studentPanels.get(i).lbl.getText();
            if (mark == null || mark.trim().equals("")) {
                errors.add(studentName + " - " + emptyMarkError);
                continue;
            }
            try {
                int markVal = Integer.parseInt(mark, 10);
                if (!(markVal >= 2 && markVal <= 5)) {
                    errors.add(studentName + " - " + incorrectMarkError);
                }
            } catch (NumberFormatException ex) {
                errors.add(studentName + " - " + notNumberMark);
            }
        }
        return errors;
    }

    String emptyReportError = "Нет текста отчета";

    public ArrayList<String> checkDecanatForm(DecanatForm form) {
        ArrayList<String> errors = new ArrayList<>();
        String text = form.reportEditor.getText();
        if (text == null || text.trim().equals("")) {
            errors.add(emptyReportError);
        }
        return errors;
    }
}
