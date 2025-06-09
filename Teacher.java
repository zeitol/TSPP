import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
public class Teacher {
    private int id;
    private String name;
    private boolean dismissed;
    private List<Subject> subjects;
    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
        this.dismissed = false;
        this.subjects = new ArrayList<>();
    }
    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isDismissed() { return dismissed; }
    public List<Subject> getSubjects() { return subjects; }
    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDismissed(boolean dismissed) { this.dismissed = dismissed; }
    // Методы работы
    public void gradeStudent(Student student, Subject subject, String gradeType, int value) {
        Grade grade = new Grade(subject, student, gradeType, value, LocalDate.now().toString());
        student.addGrade(grade);
        subject.getGradebook().addGrade(grade);
    }
    public void markAttendance(Student student, Subject subject, String lessonType, boolean present) {
        Attendance att = new Attendance(subject, student, lessonType, present, LocalDate.now().toString());
        student.addAttendance(att);
        subject.getGradebook().addAttendance(att); 
    }
    public List<Grade> viewPerformance(Subject subject) {
        List<Grade> performance = new ArrayList<>();
        for (Student student : Main.students) {
            performance.addAll(student.getGradesBySubject(subject));
        }
        return performance;
    }
    public void dismiss() { this.dismissed = true; }
}