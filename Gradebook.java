import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Gradebook {
    private Subject subject;
    private List<Grade> grades;
    private List<Attendance> attendances;

    public Gradebook(Subject subject) {
        this.subject = subject;
        this.grades = new ArrayList<>();
        this.attendances = new ArrayList<>();
    }

    // Геттеры
    public Subject getSubject() {
        return subject;
    }

    public List<Grade> getGrades() {
        return new ArrayList<>(grades); // Защитная копия
    }

    public List<Attendance> getAttendances() {
        return new ArrayList<>(attendances); // Защитная копия
    }

    // Методы для работы с оценками
    public void addGrade(Grade grade) {
        if (grade != null && grade.getSubject().equals(subject)) {
            grades.add(grade);
        }
    }

    public boolean removeGrade(Grade grade) {
        return grades.remove(grade);
    }

    public List<Grade> getGradesForStudent(Student student) {
        return grades.stream()
                .filter(g -> g.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    // Методы для работы с посещаемостью
    public void addAttendance(Attendance attendance) {
        if (attendance != null && attendance.getSubject().equals(subject)) {
            attendances.add(attendance);
        }
    }

    public boolean removeAttendance(Attendance attendance) {
        return attendances.remove(attendance);
    }

    public List<Attendance> getAttendanceForStudent(Student student) {
        return attendances.stream()
                .filter(a -> a.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    // Методы для отчетов
    public void printGradeReport() {
        System.out.println("Ведомость по предмету: " + subject.getName());
        System.out.println("Преподаватель: " + subject.getTeacher().getName());
        System.out.println("\nОценки:");
        
        grades.forEach(g -> System.out.println(
            g.getStudent().getName() + " (" + g.getStudent().getGroup() + "): " + 
            g.getValue() + " (" + g.getType() + ", " + g.getDate() + ")"
        ));
        
        System.out.println("\nПосещаемость:");
        attendances.forEach(a -> System.out.println(
            a.getStudent().getName() + ": " + 
            (a.isPresent() ? "Присутствовал" : "Отсутствовал") + 
            " на " + a.getLessonType() + " (" + a.getDate() + ")"
        ));
    }

    // Методы для сохранения/загрузки
    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("SubjectID:" + subject.getId());
            
            writer.println("\n[Grades]");
            for (Grade g : grades) {
                writer.println(String.join(";",
                    String.valueOf(g.getStudent().getId()),
                    g.getType(),
                    String.valueOf(g.getValue()),
                    g.getDate()
                ));
            }
            
            writer.println("\n[Attendance]");
            for (Attendance a : attendances) {
                writer.println(String.join(";",
                    String.valueOf(a.getStudent().getId()),
                    a.getLessonType(),
                    String.valueOf(a.isPresent()),
                    a.getDate()
                ));
            }
        }
    }

    public static Gradebook loadFromFile(String filename, List<Student> students) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line == null || !line.startsWith("SubjectID:")) {
                throw new IOException("Invalid gradebook file format");
            }
            
            int subjectId = Integer.parseInt(line.split(":")[1]);
            // Предполагаем, что Subject уже загружен
            Subject subject = Main.subjects.stream()
                    .filter(s -> s.getId() == subjectId)
                    .findFirst()
                    .orElseThrow(() -> new IOException("Subject not found"));
            
            Gradebook gradebook = new Gradebook(subject);
            Section currentSection = Section.NONE;
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("[Grades]")) {
                    currentSection = Section.GRADES;
                    continue;
                } else if (line.equals("[Attendance]")) {
                    currentSection = Section.ATTENDANCE;
                    continue;
                } else if (line.isBlank()) {
                    continue;
                }
                
                String[] parts = line.split(";");
                if (parts.length < 4) continue;
                
                int studentId = Integer.parseInt(parts[0]);
                Student student = students.stream()
                        .filter(s -> s.getId() == studentId)
                        .findFirst()
                        .orElse(null);
                
                if (student == null) continue;
                
                if (currentSection == Section.GRADES) {
                    Grade grade = new Grade(
                        subject,
                        student,
                        parts[1], // type
                        Integer.parseInt(parts[2]), // value
                        parts[3]  // date
                    );
                    gradebook.addGrade(grade);
                } else if (currentSection == Section.ATTENDANCE) {
                    Attendance att = new Attendance(
                        subject,
                        student,
                        parts[1], // lessonType
                        Boolean.parseBoolean(parts[2]), // present
                        parts[3]  // date
                    );
                    gradebook.addAttendance(att);
                }
            }
            return gradebook;
        }
    }
    private enum Section { NONE, GRADES, ATTENDANCE }
}