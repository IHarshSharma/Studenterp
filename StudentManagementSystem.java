import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentManagementSystem {
    private ArrayList<Student> students;
    private int nextId;
    private static final String DATA_FILE = "students.dat";
    private AuthService authService;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        nextId = 1;
        authService = new AuthService();
        loadStudents();
    }

    @SuppressWarnings("unchecked")
    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            students = (ArrayList<Student>) ois.readObject();
            nextId = students.size() > 0 ? students.get(students.size() - 1).getId() + 1 : 1;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing student data found, starting fresh.");
        }
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStudent(String name, int age, String grade) {
        Student student = new Student(nextId++, name, age, grade);
        students.add(student);
        saveStudents();
        System.out.println("Student added: " + student);
    }

    public void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students to display.");
        } else {
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    public void updateStudent(int id, String name, int age, String grade) {
        for (Student student : students) {
            if (student.getId() == id) {
                student.setName(name);
                student.setAge(age);
                student.setGrade(grade);
                saveStudents();
                System.out.println("Student updated: " + student);
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void deleteStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                students.remove(student);
                saveStudents();
                System.out.println("Student with ID " + id + " deleted.");
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void searchStudent(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                System.out.println(student);
                return;
            }
        }
        System.out.println("Student with name " + name + " not found.");
    }

    public void markAttendance(int id, String date) {
        for (Student student : students) {
            if (student.getId() == id) {
                student.markAttendance(date);
                saveStudents();
                System.out.println("Attendance marked for student ID " + id + " on " + date);
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    public void viewAttendance(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                System.out.println("Attendance for student ID " + id + ": " + student.getAttendance());
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        Scanner scanner = new Scanner(System.in);

        // Authentication
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!sms.authService.authenticate(username, password)) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student");
            System.out.println("6. Mark Attendance");
            System.out.println("7. View Attendance");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter grade: ");
                    String grade = scanner.nextLine();
                    sms.addStudent(name, age, grade);
                    break;
                case 2:
                    sms.viewAllStudents();
                    break;
                case 3:
                    System.out.print("Enter student ID to update: ");
                    int idToUpdate = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new age: ");
                    int newAge = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter new grade: ");
                    String newGrade = scanner.nextLine();
                    sms.updateStudent(idToUpdate, newName, newAge, newGrade);
                    break;
                case 4:
                    System.out.print("Enter student ID to delete: ");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    sms.deleteStudent(idToDelete);
                    break;
                case 5:
                    System.out.print("Enter name to search: ");
                    String searchName = scanner.nextLine();
                    sms.searchStudent(searchName);
                    break;
                case 6:
                    System.out.print("Enter student ID to mark attendance: ");
                    int idToMark = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    sms.markAttendance(idToMark, date);
                    break;
                case 7:
                    System.out.print("Enter student ID to view attendance: ");
                    int idToView = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    sms.viewAttendance(idToView);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
