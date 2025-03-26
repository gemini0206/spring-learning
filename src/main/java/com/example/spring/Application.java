package com.example.spring;

import com.example.spring.dao.StudentDAO;
import com.example.spring.entity.Student;
import com.example.spring.grpc.client.GrpcClientConfig;
import com.example.spring.grpc.client.GrpcClientProperties;
import com.example.spring.grpc.server.GrpcServerProperties;
import com.example.spring.grpc.server.Runner;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.services.HealthStatusManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(scanBasePackages = {
		"com.example.spring",
		"com.example.util" 
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {
		return runner -> {
//			createStudent(studentDAO);
//			readStudent(studentDAO);
//			queryForStudents(studentDAO);
//			queryForStudentsByLastName(studentDAO);
//			updateStudent(studentDAO);
//			deleteStudent(studentDAO);
//			deleteAllStudent(studentDAO);
		};
	}

	@Bean
	public HealthStatusManager healthStatusManager() {
		return new HealthStatusManager();
	}

	@Bean
	public GrpcClientProperties grpcClientProperties() {
		return new GrpcClientProperties();
	}

	@Bean
	public GrpcClientConfig grpcClientConfig() {
		return new GrpcClientConfig();
	}

	private void deleteAllStudent(StudentDAO studentDAO) {
		System.out.println("Deleting all students");
		int numRowsDeleted = studentDAO.deleteAll();
		System.out.println("Deleted " + numRowsDeleted + " students");
	}

	private void deleteStudent(StudentDAO studentDAO) {
		int studentId = 2;
		System.out.println("Deleting student id: " + studentId);
		studentDAO.delete(studentId);
		System.out.println("Deleted student: " + studentId);
	}

	private void updateStudent(StudentDAO studentDAO) {
		int studentId = 1;
		System.out.println("Getting student with id " + studentId);


		System.out.println("Updating student...");
		Student theStudent = studentDAO.findById(studentId);
		theStudent.setFirstName("Scooby");
		studentDAO.update(theStudent);

		System.out.println("Updated student");
	}

	private void queryForStudentsByLastName(StudentDAO studentDAO) {
		List<Student> students = studentDAO.findByLastName("Duck");

		for (Student student : students) {
			System.out.println(student);
		}
	}

	private void queryForStudents(StudentDAO studentDAO) {
		List<Student> students = studentDAO.findAll();

		for (Student student : students) {
			System.out.println(student);
		}
	}

	private void readStudent(StudentDAO studentDAO) {
		System.out.println("Creating student...");
		Student tempStudent = new Student("Daffy", "Duck", "daffy@luv2code.com");

		System.out.println("Saving student...");
		studentDAO.save(tempStudent);

		int theId = tempStudent.getId();
		System.out.println("Done. Generated id: " + theId);

		Student theStudent = studentDAO.findById(theId);
		System.out.println("Found student: " + theStudent);
	}

	private void createStudent(StudentDAO studentDAO) {
		System.out.println("Creating student...");
		Student tempStudent = new Student("Paul", "Doe", "paul@luv2code.com");

		System.out.println("Saving student...");
		studentDAO.save(tempStudent);

		System.out.println("Done. Generated id: " + tempStudent.getId());
	}



}
