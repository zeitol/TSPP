public class Attendance {
    private Subject subject;
    private Student student;
    private String lessonType;
    private boolean present;
    private String date;

    public Attendance(Subject subject, Student student, String lessonType, 
                     boolean present, String date) {
        this.subject = subject;
        this.student = student;
        this.lessonType = lessonType;
        this.present = present;
        this.date = date;
    }

    // Геттеры
    public Subject getSubject() { return subject; }
    public Student getStudent() { return student; }
    public String getLessonType() { return lessonType; }
    public boolean isPresent() { return present; }
    public String getDate() { return date; }
}