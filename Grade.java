public class Grade {
    private Subject subject;
    private Student student;
    private String type;
    private int value;
    private String date;

    public Grade(Subject subject, Student student, String type, int value, String date) {
        this.subject = subject;
        this.student = student;
        this.type = type;
        this.value = value;
        this.date = date;
    }

    // Геттеры
    public Subject getSubject() { return subject; }
    public Student getStudent() { return student; }
    public String getType() { return type; }
    public int getValue() { return value; }
    public String getDate() { return date; }
}