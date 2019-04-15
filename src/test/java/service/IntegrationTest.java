package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.time.LocalDate;

import static DataVariables.Strings.*;

public class IntegrationTest {
    private static Service service;

    @BeforeClass
    public static void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @Test
    public void addStudent() {
        String ID = String.valueOf(System.currentTimeMillis());
        Student student = new Student(ID, TEST, GROUP, EMAIL);
        service.addStudent(student);
        Assert.assertEquals(service.findStudent(ID), student);
    }

    @Test
    public void addTema() {
        addStudent();
        String ID = String.valueOf(System.currentTimeMillis());
        Tema tema = new Tema(ID, TEST, DEADLINE, RECEIVE);
        service.addTema(tema);
        Assert.assertEquals(service.findTema(ID), tema);
    }

    @Test(expected = ValidationException.class)
    public void addGrade() {
        String ID = String.valueOf(System.currentTimeMillis());
        addStudent();
        addTema();
        Nota nota = new Nota(ID, ID, ID, 10.0, LocalDate.of(2019, 1, 1));
        service.addNota(nota, "Bine");
    }
}
