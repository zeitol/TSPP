public class Subject {
    private int id;
    private String name;
    private Teacher teacher;
    private Gradebook gradebook;
    public Subject(int id, String name, Teacher teacher) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.gradebook = new Gradebook(this);
    }
    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public Teacher getTeacher() { return teacher; }
    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }
    public Gradebook getGradebook() { return gradebook; }
}