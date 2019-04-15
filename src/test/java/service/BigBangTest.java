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
import java.util.Random;

import static DataVariables.Strings.*;

public class BigBangTest {
    private static Service service;
    private static String ID;

    @BeforeClass
    public static void setUp() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
        ID = String.valueOf(System.currentTimeMillis());
    }

    @Test
    public void addStudent() {
        Iterable<Student> students = service.getAllStudenti();
        int listSize = numberOfStudents(students);
        service.addStudent(new Student(ID, TEST, GROUP, EMAIL));
        Assert.assertEquals(listSize + 1, numberOfStudents(students));
    }

    @Test
    public void addTema() {
        Iterable<Tema> teme = service.getAllTeme();
        int listSize = numberOfTeme(teme);
        Tema tema1 = new Tema(ID, TEST, DEADLINE, RECEIVE);
        Tema tema2 = new Tema(ID_EXISTING_HOMEWORK, TEST, DEADLINE, RECEIVE);
        Tema tema;
        int decision = new Random().nextInt(2);
        if (decision == 0)
            tema = service.addTema(tema1);
        else
            tema = service.addTema(tema2);
        if (tema == null)
            Assert.assertEquals(listSize + 1, numberOfTeme(teme));
        else
            Assert.assertEquals(listSize, numberOfTeme(teme));
    }

    @Test(expected = ValidationException.class)
    public void addGrade() {
        service.addStudent(new Student(ID, TEST, GROUP, EMAIL));
        service.addTema(new Tema(ID, TEST, DEADLINE, RECEIVE));
        Nota nota = new Nota(ID, ID, ID, 10.0, LocalDate.of(2019, 1, 1));
        service.addNota(nota, "Bine");
    }

    @Test(expected = ValidationException.class)
    public void bigBangTest() {
        addStudent();
        addTema();
        addGrade();
    }

    private int numberOfStudents(Iterable<Student> list) {
        int s = 0;
        for (Student student : list)
            s++;
        return s;
    }

    private int numberOfTeme(Iterable<Tema> list) {
        int s = 0;
        for (Tema tema : list)
            s++;
        return s;
    }
}
