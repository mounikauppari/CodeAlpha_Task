package codealpha;
import java.util.*;
public class Student {

	  String name;
	    double grade;

	    Student(String name, double grade) {
	        this.name = name;
	        this.grade = grade;
	    }
	}

	 class StudentGradeManager {
	    private ArrayList<Student> students;
	    private Scanner scanner;

	    public StudentGradeManager() {
	        students = new ArrayList<>();
	        scanner = new Scanner(System.in);
	    }

	    public void run() {
	        System.out.println("=== Student Grade Manager ===");

	        boolean running = true;
	        while (running) {
	            System.out.println("\n1. Add Student");
	            System.out.println("2. Show Summary Report");
	            System.out.println("3. Exit");
	            System.out.print("Choose an option: ");

	            int choice = scanner.nextInt();
	            scanner.nextLine();  // consume newline

	            switch (choice) {
	                case 1:
	                    addStudent();
	                    break;
	                case 2:
	                    showSummary();
	                    break;
	                case 3:
	                    running = false;
	                    System.out.println("Exiting program.");
	                    break;
	                default:
	                    System.out.println("Invalid choice! Try again.");
	            }
	        }

	        scanner.close();
	    }

	    private void addStudent() {
	        System.out.print("Enter student name: ");
	        String name = scanner.nextLine();

	        System.out.print("Enter student grade (0-100): ");
	        double grade = scanner.nextDouble();

	        if (grade < 0 || grade > 100) {
	            System.out.println("Invalid grade. Must be between 0 and 100.");
	            return;
	        }

	        students.add(new Student(name, grade));
	        System.out.println("Student added successfully.");
	    }

	    private void showSummary() {
	        if (students.isEmpty()) {
	            System.out.println("No student data available.");
	            return;
	        }

	        double sum = 0;
	        double highest = Double.MIN_VALUE;
	        double lowest = Double.MAX_VALUE;
	        String topStudent = "";
	        String lowStudent = "";

	        System.out.println("\n--- Student Grades ---");
	        for (Student s : students) {
	            System.out.println(s.name + ": " + s.grade);
	            sum += s.grade;

	            if (s.grade > highest) {
	                highest = s.grade;
	                topStudent = s.name;
	            }

	            if (s.grade < lowest) {
	                lowest = s.grade;
	                lowStudent = s.name;
	            }
	        }

	        double average = sum / students.size();

	        System.out.println("\n--- Summary Report ---");
	        System.out.printf("Average Grade: %.2f\n", average);
	        System.out.printf("Highest Grade: %.2f (%s)\n", highest, topStudent);
	        System.out.printf("Lowest Grade: %.2f (%s)\n", lowest, lowStudent);
	    }

	    public static void main(String[] args) {
	        StudentGradeManager manager = new StudentGradeManager();
	        manager.run();
	    }

	
}
