import java.io.*;
import java.util.*;

public class Main {
    static List<Student> students = new ArrayList<>();
    static List<Teacher> teachers = new ArrayList<>();
    static List<Subject> subjects = new ArrayList<>();
    static Dean dean;
    static int nextStudentId = 1;
    static int nextTeacherId = 1;
    static int nextSubjectId = 1;
    
    private static final String STUDENTS_FILE = "students.txt";
    private static final String TEACHERS_FILE = "teachers.txt";
    private static final String SUBJECTS_FILE = "subjects.txt";
    private static final String GRADES_FILE = "grades.txt";
    private static final String ATTENDANCE_FILE = "attendance.txt";
    public static void main(String[] args){
        initializeData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("\f");
            System.out.println("\n=== УЧЕБНАЯ СИСТЕМА ===");
            System.out.println("1. Режим декана");
            System.out.println("2. Режим преподавателя");
            System.out.println("3. Режим студента");
            System.out.println("4. Сохранить все данные");
            System.out.println("5. Загрузить данные");
            System.out.println("0. Выход");
            System.out.print("Выберите режим: ");

            int choice = readIntInput(scanner);
            scanner.nextLine(); // Очистка буфера

            switch (choice) {
                case 1:
                    deanMenu(scanner);
                    break;
                case 2:
                    teacherMenu(scanner);
                    break;
                case 3:
                    studentMenu(scanner);
                    break;
                case 4:
                    saveAllData();
                    System.out.println("Данные сохранены!");
                    break;
                case 5:
                    loadAllData();
                    System.out.println("Данные загружены!");
                    break;
                case 0:
                    saveAllData();
                    System.out.println("Работа системы завершена.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ошибка! Введите число от 0 до 5");
            }
        }
    }

    // Инициализация начальных данных
    private static void initializeData() {
        dean = new Dean(1, "Иванов И.И.");

        /* // Тестовые данные
        Teacher mathTeacher = dean.createTeacher(nextTeacherId++, "Петрова А.С.");
        Subject math = dean.createSubject(nextSubjectId++, "Математика", mathTeacher);
        
        Student student1 = dean.createStudent(nextStudentId++, "Сидоров А.А.", "Группа 101");
        Student student2 = dean.createStudent(nextStudentId++, "Кузнецова Е.В.", "Группа 102"); */
    } 

    // Меню декана
    private static void deanMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== РЕЖИМ ДЕКАНА ===");
            System.out.println("1. Создать студента");
            System.out.println("2. Отчислить студента");
            System.out.println("3. Управление допуском к экзаменам");
            System.out.println("4. Создать преподавателя");
            System.out.println("5. Создать предмет");
            System.out.println("6. Просмотр ведомости");
            System.out.println("7. Экспорт ведомости");
            System.out.println("8. Статистика по группе");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput(scanner);
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите ФИО студента: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите группу: ");
                    String group = scanner.nextLine();
                    Student student = dean.createStudent(nextStudentId++, name, group);
                    System.out.println("Создан студент ID " + student.getId());
                    break;
                    
                case 2:
                    System.out.println("Список студентов:");
                    students.forEach(s->System.out.printf("%d: %s (Группа: %s) %s%s\n", s.getId(), s.getName(), s.getGroup(), s.isExpelled() ? "Отчислен" : "", s.isExamAllowed() ? "Допущен" : "Не допущен"));
                    System.out.print("Введите ID студента: ");
                    int id = readIntInput(scanner);
                    Student studentToExpel = findStudentById(id);
                    if (studentToExpel != null) {
                        dean.expelStudent(studentToExpel);
                        System.out.println("Студент отчислен");
                    } else {
                        System.out.println("Студент не найден!");
                    }
                    break;
                    
                case 3:
                    System.out.println("Список студентов:");
                    students.forEach(s->System.out.printf("%d: %s (Группа: %s) %s%s\n", s.getId(), s.getName(), s.getGroup(), s.isExpelled() ? "Отчислен" : "", s.isExamAllowed() ? "Допущен" : "Не допущен"));
                    System.out.print("Введите ID студента: ");
                    id = readIntInput(scanner);
                    Student studentForExam = findStudentById(id);
                    if (studentForExam != null) {
                        System.out.print("Допустить (true/false): ");
                        boolean allow = scanner.nextBoolean();
                        scanner.nextLine();
                        dean.allowForExams(studentForExam, allow);
                        System.out.println("Статус обновлен");
                    } else {
                        System.out.println("Студент не найден!");
                    }
                    break;
                    
                case 4:
                    System.out.print("Введите ФИО преподавателя: ");
                    String teacherName = scanner.nextLine();
                    Teacher teacher = dean.createTeacher(nextTeacherId++, teacherName);
                    System.out.println("Создан преподаватель ID " + teacher.getId());
                    break;
                    
                case 5:
                    System.out.println("Список преподавателей:");
                    teachers.forEach(t->System.out.printf("%d: %s%s\n", t.getId(), t.getName(), t.isDismissed() ? "(Уволен)" : ""));
                    System.out.print("Введите название предмета: ");
                    String subjectName = scanner.nextLine();
                    System.out.print("Введите ID преподавателя: ");
                    id = readIntInput(scanner);
                    scanner.nextLine();
                    Teacher subjectTeacher = findTeacherById(id);
                    if (subjectTeacher != null) {
                        Subject subject = dean.createSubject(nextSubjectId++, subjectName, subjectTeacher);
                        System.out.println("Создан предмет ID " + subject.getId());
                    } else {
                        System.out.println("Преподаватель не найден!");
                    }
                    break;
                    
                case 6:
                    System.out.print("Введите ID предмета: ");
                    id = readIntInput(scanner);
                    scanner.nextLine();
                    Subject subject = findSubjectById(id);
                    if (subject != null) {
                        dean.printGradebookForSubject(subject);
                    } else {
                        System.out.println("Предмет не найден!");
                    }
                    break;
                    
                case 7:
                    System.out.print("Введите ID предмета: ");
                    id = readIntInput(scanner);
                    scanner.nextLine();
                    subject = findSubjectById(id);
                    if (subject != null) {
                        System.out.print("Введите имя файла: ");
                        String filename = scanner.nextLine();
                        dean.exportGradebookToFile(subject, filename);
                    } else {
                        System.out.println("Предмет не найден!");
                    }
                    break;
                    
                case 8:
                    System.out.print("Введите название группы: ");
                    String groupName = scanner.nextLine();
                    dean.generateGroupReport(groupName);
                    break;
                    
                case 0:
                    return;
                    
                default:
                    System.out.println("Ошибка! Введите число от 0 до 8");
            }
        }
    }

    // Меню преподавателя
    private static void teacherMenu(Scanner scanner) {
        System.out.println("Список преподавателей:");
        teachers.forEach(t->System.out.printf("%d: %s%s\n", t.getId(), t.getName(), t.isDismissed() ? "(Уволен)" : ""));
        System.out.print("\nВведите ID преподавателя: ");
        int id = readIntInput(scanner);
        scanner.nextLine();
        
        Teacher teacher = findTeacherById(id);
        if (teacher == null) {
            System.out.println("Преподаватель не найден!");
            return;
        }

        while (true) {
            System.out.println("\n=== РЕЖИМ ПРЕПОДАВАТЕЛЯ ===");
            System.out.println("1. Выставить оценку");
            System.out.println("2. Отметить посещаемость");
            System.out.println("3. Просмотреть успеваемость");
            System.out.println("4. Мои предметы");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput(scanner);
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Введите ID студента: ");
                    int studentId = readIntInput(scanner);
                    scanner.nextLine();
                    Student student = findStudentById(studentId);
                    if (student == null) {
                        System.out.println("Студент не найден!");
                        break;
                    }

                    System.out.print("Введите ID предмета: ");
                    int subjectId = readIntInput(scanner);
                    scanner.nextLine();
                    Subject subject = findSubjectById(subjectId);
                    if (subject == null || !subject.getTeacher().equals(teacher)) {
                        System.out.println("Предмет не найден или не ваш!");
                        break;
                    }

                    System.out.print("Тип оценки (экзамен/зачет/тест): ");
                    String gradeType = scanner.nextLine();
                    System.out.print("Оценка (число): ");
                    int gradeValue = readIntInput(scanner);
                    scanner.nextLine();

                    teacher.gradeStudent(student, subject, gradeType, gradeValue);
                    System.out.println("Оценка выставлена!");
                    break;

                case 2:
                    System.out.print("Введите ID студента: ");
                    studentId = readIntInput(scanner);
                    scanner.nextLine();
                    student = findStudentById(studentId);
                    if (student == null) {
                        System.out.println("Студент не найден!");
                        break;
                    }

                    System.out.print("Введите ID предмета: ");
                    subjectId = readIntInput(scanner);
                    scanner.nextLine();
                    subject = findSubjectById(subjectId);
                    if (subject == null || !subject.getTeacher().equals(teacher)) {
                        System.out.println("Предмет не найден или не ваш!");
                        break;
                    }

                    System.out.print("Тип занятия (лекция/практика): ");
                    String lessonType = scanner.nextLine();
                    System.out.print("Присутствовал (true/false): ");
                    boolean present = scanner.nextBoolean();
                    scanner.nextLine();

                    teacher.markAttendance(student, subject, lessonType, present);
                    System.out.println("Посещаемость отмечена!");
                    break;

                case 3:
                    System.out.print("Введите ID предмета: ");
                    subjectId = readIntInput(scanner);
                    scanner.nextLine();
                    subject = findSubjectById(subjectId);
                    if (subject == null || !subject.getTeacher().equals(teacher)) {
                        System.out.println("Предмет не найден или не ваш!");
                        break;
                    }

                    System.out.println("\nУспеваемость по " + subject.getName() + ":");
                    List<Grade> grades = teacher.viewPerformance(subject);
                    if (grades.isEmpty()) {
                        System.out.println("Оценок нет");
                    } else {
                        grades.forEach(g -> System.out.println(
                            g.getStudent().getName() + ": " + g.getValue() + " (" + g.getType() + ")"
                        ));
                    }
                    break;

                case 4:
                    System.out.println("\nВаши предметы:");
                    boolean hasSubjects = false;
                    for (Subject s : subjects) {
                        if (s.getTeacher().equals(teacher)) {
                            System.out.println(s.getId() + ": " + s.getName());
                            hasSubjects = true;
                        }
                    }
                    if (!hasSubjects) {
                        System.out.println("У вас нет предметов");
                    }
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Ошибка! Введите число от 0 до 4");
            }
        }
    }

    // Меню студента
    private static void studentMenu(Scanner scanner) {
        System.out.println("Список студентов:");
        students.forEach(s->System.out.printf("%d: %s (Группа: %s) %s%s\n", s.getId(), s.getName(), s.getGroup(), s.isExpelled() ? "Отчислен" : "", s.isExamAllowed() ? "Допущен" : "Не допущен"));
        System.out.print("\nВведите ID студента: ");
        int id = readIntInput(scanner);
        scanner.nextLine();
        
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Студент не найден!");
            return;
        }

        while (true) {
            System.out.println("\n=== РЕЖИМ СТУДЕНТА ===");
            System.out.println("1. Мои оценки");
            System.out.println("2. Моя посещаемость");
            System.out.println("3. Мои задолженности");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            int choice = readIntInput(scanner);
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nВаши оценки:");
                    boolean hasGrades = false;
                    for (Subject subject : subjects) {
                        List<Grade> grades = subject.getGradebook().getGradesForStudent(student);
                        if (!grades.isEmpty()) {
                            System.out.println(subject.getName() + ":");
                            grades.forEach(g -> System.out.println(
                                "  " + g.getType() + ": " + g.getValue() + " (" + g.getDate() + ")"
                            ));
                            hasGrades = true;
                        }
                    }
                    if (!hasGrades) {
                        System.out.println("Оценок нет");
                    }
                    break;

                case 2:
                    System.out.println("\nВаша посещаемость:");
                    boolean hasAttendance = false;
                    for (Subject subject : subjects) {
                        List<Attendance> attendance = subject.getGradebook().getAttendanceForStudent(student);
                        if (!attendance.isEmpty()) {
                            System.out.println(subject.getName() + ":");
                            attendance.forEach(a -> System.out.println(
                                "  " + a.getLessonType() + ": " + 
                                (a.isPresent() ? "Присутствовал" : "Отсутствовал") + 
                                " (" + a.getDate() + ")"
                            ));
                            hasAttendance = true;
                        }
                    }
                    if (!hasAttendance) {
                        System.out.println("Записей о посещаемости нет");
                    }
                    break;

                case 3:
                    System.out.println("\nВаши задолженности:");
                    boolean hasDebts = false;
                    for (Subject subject : subjects) {
                        List<Grade> grades = subject.getGradebook().getGradesForStudent(student);
                        long failedGrades = grades.stream()
                            .filter(g -> g.getValue() < 4) // Оценки ниже 4 считаем задолженностями
                            .count();
                        
                        if (failedGrades > 0) {
                            System.out.println(subject.getName() + ": " + failedGrades + " задолженностей");
                            hasDebts = true;
                        }
                    }
                    if (!hasDebts) {
                        System.out.println("Задолженностей нет");
                    }
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Ошибка! Введите число от 0 до 3");
            }
        }
    }

    // Вспомогательные методы
    private static int readIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Ошибка! Введите число: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static Student findStudentById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public static Teacher findTeacherById(int id) {
        for (Teacher t : teachers) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public static Subject findSubjectById(int id) {
        for (Subject s : subjects) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }
    // Методы сохранения/загрузки данных
    private static void saveAllData() {
        saveStudents();
        saveTeachers();
        saveSubjects();
        saveGrades();
        saveAttendance();
    }
    private static void loadAllData() {
        students = loadStudents();
        teachers = loadTeachers();
        subjects = loadSubjects();
        loadGrades();
        loadAttendance();
    }
    private static void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student s : students) {
                writer.println(String.join(";",
                    String.valueOf(s.getId()),
                    s.getName(),
                    s.getGroup(),
                    String.valueOf(s.isExpelled()),
                    String.valueOf(s.isExamAllowed())
                ));
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения студентов!");
        }
    }
    private static List<Student> loadStudents() {
        List<Student> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    Student s = new Student(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2]
                    );
                    s.setExpelled(Boolean.parseBoolean(parts[3]));
                    s.setExamAllowed(Boolean.parseBoolean(parts[4]));
                    loaded.add(s);
                    nextStudentId = Math.max(nextStudentId, s.getId() + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Файл студентов не найден, будет создан новый");
        }
        return loaded;
    }
    private static void saveTeachers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEACHERS_FILE))) {
            for (Teacher t : teachers) {
                // Формат: id;name;dismissed;isDean
                writer.println(String.join(";",String.valueOf(t.getId()),t.getName(),String.valueOf(t.isDismissed()),String.valueOf(t.equals(dean))));
        }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения преподавателей: " + e.getMessage());
        }
    }
    private static List<Teacher> loadTeachers() {
        List<Teacher> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    boolean dismissed = Boolean.parseBoolean(parts[2]);
                    boolean isDean = Boolean.parseBoolean(parts[3]);
                    Teacher teacher;
                    if (isDean) {
                        // Создаем декана через конструктор Teacher (базовый класс)
                        teacher = new Teacher(id, name);
                        teacher.setDismissed(dismissed);
                        // Создаем отдельный объект декана и копируем данные
                        dean = new Dean(id, name);
                    } else {
                        teacher = new Teacher(id, name);
                        teacher.setDismissed(dismissed);
                    }
                    loaded.add(teacher);  
                    // Обновляем счетчик ID
                    nextTeacherId = Math.max(nextTeacherId, id + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Файл преподавателей не найден, будет создан новый");
        }
        return loaded;
    }
    private static void saveSubjects() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SUBJECTS_FILE))) {
            for (Subject s : subjects) {
                // Формат: id;name;teacherId
                writer.println(String.join(";",String.valueOf(s.getId()),s.getName(),String.valueOf(s.getTeacher().getId())));
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения предметов: " + e.getMessage());
        }
    }
    private static List<Subject> loadSubjects() {
        List<Subject> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SUBJECTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int teacherId = Integer.parseInt(parts[2]);
                    Teacher teacher = findTeacherById(teacherId);
                    if (teacher != null) {
                        Subject subject = new Subject(id, name, teacher);
                        loaded.add(subject);
                        // Обновляем счетчик ID
                        nextSubjectId = Math.max(nextSubjectId, id + 1);
                        // Добавляем предмет преподавателю
                        teacher.getSubjects().add(subject);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Файл предметов не найден, будет создан новый");
        }
        return loaded;
    }
    private static void saveGrades() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(GRADES_FILE))) {
            for (Subject subject : subjects) {
                for (Grade grade : subject.getGradebook().getGrades()) {
                    writer.println(String.join(";",String.valueOf(subject.getId()),String.valueOf(grade.getStudent().getId()),grade.getType(),String.valueOf(grade.getValue()),grade.getDate()));
            }
        }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения оценок: " + e.getMessage());
        }
    }
    private static void loadGrades() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GRADES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    int subjectId = Integer.parseInt(parts[0]);
                    int studentId = Integer.parseInt(parts[1]);
                    String type = parts[2];
                    int value = Integer.parseInt(parts[3]);
                    String date = parts[4];
                    Subject subject = findSubjectById(subjectId);
                    Student student = findStudentById(studentId);
                    if (subject != null && student != null) {
                        Grade grade = new Grade(subject, student, type, value, date);
                        subject.getGradebook().addGrade(grade);
                        student.addGrade(grade);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Файл оценок не найден, будет создан новый");
        }
    }
    private static void saveAttendance() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (Subject subject : subjects) {
                for (Attendance att : subject.getGradebook().getAttendances()) {
                    writer.println(String.join(";",String.valueOf(subject.getId()),String.valueOf(att.getStudent().getId()),att.getLessonType(),String.valueOf(att.isPresent()),att.getDate()));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка сохранения посещаемости: " + e.getMessage());
        }
    }

    private static void loadAttendance() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    int subjectId = Integer.parseInt(parts[0]);
                    int studentId = Integer.parseInt(parts[1]);
                    String lessonType = parts[2];
                    boolean present = Boolean.parseBoolean(parts[3]);
                    String date = parts[4];
                    Subject subject = findSubjectById(subjectId);
                    Student student = findStudentById(studentId);
                    if (subject != null && student != null) {
                        Attendance att = new Attendance(subject, student, lessonType, present, date);
                        subject.getGradebook().addAttendance(att);
                        student.addAttendance(att);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Файл посещаемости не найден, будет создан новый");
        }
    }
}