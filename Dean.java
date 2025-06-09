import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Dean {
    private int id;
    private String name;

    public Dean(int id, String name) {
        this.id = id;
        this.name = name;  // Исправлено: добавлено сохранение имени
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    // Методы работы со студентами
    public void expelStudent(Student student) {
        student.expel();
    }

    public void allowForExams(Student student, boolean allow) {
        student.setExamAllowed(allow);  // Исправлено: использование параметра allow
    }

    // Методы создания сущностей
    public Student createStudent(int id, String name, String group) {
        Student student = new Student(id, name, group);
        Main.students.add(student);
        return student;
    }

    public Teacher createTeacher(int id, String name) {
        Teacher teacher = new Teacher(id, name);
        Main.teachers.add(teacher);
        return teacher;
    }

    public Subject createSubject(int id, String name, Teacher teacher) {
        Subject subject = new Subject(id, name, teacher);
        Main.subjects.add(subject);
        return subject;
    }

    // Методы для работы с оценками
    public List<Grade> viewGradesByGroup(String group, Subject subject) {
        List<Grade> grades = new ArrayList<>();
        for (Student student : Main.students) {
            if (student.getGroup().equals(group)) {
                grades.addAll(subject.getGradebook().getGradesForStudent(student));
            }
        }
        return grades;
    }

    // Методы для работы с ведомостями
    public void printGradebookForSubject(Subject subject) {
        if (subject.getGradebook() == null) {
            System.out.println("Ведомость для предмета " + subject.getName() + " не найдена!");
            return;
        }

        System.out.println("\n=== Ведомость по предмету: " + subject.getName() + " ===");
        System.out.println("Преподаватель: " + subject.getTeacher().getName());
        System.out.println("\nСписок оценок:");

        List<Grade> allGrades = subject.getGradebook().getGrades();
        if (allGrades.isEmpty()) {
            System.out.println("Оценки отсутствуют");
        } else {
            for (Grade grade : allGrades) {
                System.out.printf("%s (Группа: %s): %d (%s, %s)\n",
                    grade.getStudent().getName(),
                    grade.getStudent().getGroup(),
                    grade.getValue(),
                    grade.getType(),
                    grade.getDate());
            }
        }

        System.out.println("\nСтатистика:");
        System.out.println("Всего оценок: " + allGrades.size());
        System.out.println("Средний балл: " + calculateAverageGrade(allGrades));
    }

    public void exportGradebookToFile(Subject subject, String filename) {
        try {
            if (subject.getGradebook() != null) {
                subject.getGradebook().saveToFile(filename);
                System.out.println("Ведомость успешно экспортирована в файл: " + filename);
            } else {
                System.out.println("Ошибка: ведомость для предмета не создана");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при экспорте ведомости: " + e.getMessage());
        }
    }

    // Вспомогательные методы
    private double calculateAverageGrade(List<Grade> grades) {
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (Grade grade : grades) {
            sum += grade.getValue();
        }
        return sum / grades.size();
    }
    // Метод для генерации отчета по группе
    public void generateGroupReport(String groupName) {
        System.out.println("\n=== Отчет по группе " + groupName + " ===");
        
        List<Student> groupStudents = Main.students.stream().filter(s -> s.getGroup().equals(groupName)).toList();
        if (groupStudents.isEmpty()) {
            System.out.println("В группе нет студентов");
            return;
        }

        for (Student student : groupStudents) {
            System.out.println("\nСтудент: " + student.getName());
            System.out.println("Статус: " + (student.isExpelled() ? "Отчислен" : "Активен"));
            System.out.println("Допуск к экзаменам: " + (student.isExamAllowed() ? "Да" : "Нет"));
            
            System.out.println("\nПредметы и оценки:");
            for (Subject subject : Main.subjects) {
                List<Grade> grades = subject.getGradebook().getGradesForStudent(student);
                if (!grades.isEmpty()) {
                    System.out.println("- " + subject.getName() + ":");
                    grades.forEach(g -> System.out.printf("  %s: %d (%s)\n", 
                        g.getType(), g.getValue(), g.getDate()));
                }
            }
        }
    }
}