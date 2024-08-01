package ma.ram.employeeapp;

import ma.ram.employeeapp.domain.Contact;
import ma.ram.employeeapp.repositories.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeAppApplication  {



    public static void main(String[] args) {
        SpringApplication.run(EmployeeAppApplication.class, args);
    }





}
