import java.util.ArrayList;
import java.util.List;
public class Student {
    private int id;
    private String name;
    private String group;
    private boolean expelled;
    private boolean examAllowed;
    private List<Grade> grades;
    private List<Attendance> attendances;
    public Student(int id, String name, String group) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.expelled = false;
        this.examAllowed = false;
        this.grades = new ArrayList<>();
        this.attendances = new ArrayList<>();
    }
        public List<Grade> getGradesForSubject(Subject subject) {
        return subject.getGradebook().getGradesForStudent(this);
    }
        public List<Attendance> getAttendanceForSubject(Subject subject) {
        return subject.getGradebook().getAttendanceForStudent(this);
    }
    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getGroup() { return group; }
    public boolean isExpelled() { return expelled; }
    public boolean isExamAllowed() { return examAllowed; }
    public List<Grade> getGrades() { return grades; }
    public List<Attendance> getAttendances() { return attendances; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGroup(String group) { this.group = group; }
    public void setExpelled(boolean expelled) { this.expelled = expelled; }
    public void setExamAllowed(boolean examAllowed) { this.examAllowed = examAllowed; }

    // Методы для работы с оценками и посещаемостью
    public List<Grade> getGradesBySubject(Subject subject) {
        List<Grade> subjectGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getSubject().equals(subject)) {
                subjectGrades.add(grade);
            }
        }
        return subjectGrades;
    }

    public List<Attendance> getAttendanceBySubject(Subject subject) {
        List<Attendance> subjectAttendance = new ArrayList<>();
        for (Attendance att : attendances) {
            if (att.getSubject().equals(subject)) {
                subjectAttendance.add(att);
            }
        }
        return subjectAttendance;
    }

    public void addGrade(Grade grade) { grades.add(grade); }
    public void addAttendance(Attendance att) { attendances.add(att); }
    
    public void expel() { this.expelled = true; }
}