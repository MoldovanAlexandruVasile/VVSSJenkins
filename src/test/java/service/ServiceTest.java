package service;

import domain.Student;
import domain.Tema;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.asserts.SoftAssert;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.util.Random;

import static DataVariables.Strings.*;

public class ServiceTest {
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

    @Test(expected = ValidationException.class)
    public void addStudentNullID() {
        service.addStudent(new Student(null, TEST, GROUP, EMAIL));
    }

    @Test(expected = ValidationException.class)
    public void addStudentEmptyID() {
        service.addStudent(new Student("", TEST, GROUP, EMAIL));
    }

    @Test(expected = ValidationException.class)
    public void addStudentNullName() {
        service.addStudent(new Student(ID, null, GROUP, EMAIL));
    }

    @Test(expected = ValidationException.class)
    public void addStudentEmptyName() {
        service.addStudent(new Student(ID, "", GROUP, EMAIL));
    }

    @Test(expected = ValidationException.class)
    public void addStudentInvalidName() {
        service.addStudent(new Student(ID, TEST + "1234", GROUP, EMAIL));
    }

    @Test(expected = ValidationException.class)
    public void addStudentInvalidGroup() {
        service.addStudent(new Student(ID, TEST, -1, EMAIL));
    }

    @Test(expected = ValidationException.class)
    public void addStudentNullEmail() {
        service.addStudent(new Student(ID, TEST, GROUP, null));
    }

    @Test(expected = ValidationException.class)
    public void addStudentEmptyEmail() {
        service.addStudent(new Student(ID, TEST, GROUP, ""));
    }

    @Test(expected = ValidationException.class)
    public void addStudentInvalidEmailFormat() {
        service.addStudent(new Student(ID, TEST, GROUP, EMAIL_INVALID_FORMAT));
    }

    @Test
    public void addStudent() {
        Iterable<Student> students = service.getAllStudenti();
        int listSize = numberOfStudents(students);
        service.addStudent(new Student(ID, TEST, GROUP, EMAIL));
        Assert.assertEquals(listSize + 1, numberOfStudents(students));
    }

    @Test
    public void getStudentByID() {
        Student student = service.findStudent(ID);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(student.getID(), ID, "The ID does not match.");
        softAssert.assertEquals(student.getNume(), TEST, "The ame does not match");
        softAssert.assertEquals(student.getGrupa(), GROUP, "The group does not match.");
        softAssert.assertEquals(student.getEmail(), EMAIL, "The email does not match.");
        softAssert.assertAll();
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithNullID() {
        service.addTema(new Tema(null, TEST, DEADLINE, RECEIVE));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithEmptyID() {
        service.addTema(new Tema("", TEST, DEADLINE, RECEIVE));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithEmptyDescription() {
        service.addTema(new Tema(ID, "", DEADLINE, RECEIVE));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithNullDescription() {
        service.addTema(new Tema(ID, null, DEADLINE, RECEIVE));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithInvalidDeadlineLow() {
        service.addTema(new Tema(ID, TEST, 0, RECEIVE));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithInvalidDeadlineUpp() {
        service.addTema(new Tema(ID, TEST, 15, RECEIVE));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithInvalidReceiveLow() {
        service.addTema(new Tema(ID, TEST, DEADLINE, 0));
    }

    @Test(expected = ValidationException.class)
    public void addTemaWithInvalidReceiveUpp() {
        service.addTema(new Tema(ID, TEST, DEADLINE, 15));
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

    @Test
    public void getTemaByID() {
        Tema tema;
        int decision = new Random().nextInt(2);
        if (decision == 0)
            tema = service.findTema(ID);
        else
            tema = service.findTema(ID_EXISTING_HOMEWORK);
        if (tema != null) {
            System.out.println(tema);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(tema.getID(), ID_EXISTING_HOMEWORK, "The ID does not match.");
            softAssert.assertEquals(tema.getDescriere(), "file repository", "The name does not match");
            softAssert.assertEquals(tema.getDeadline(), 2, "The group does not match.");
            softAssert.assertEquals(tema.getPrimire(), 1, "The email does not match.");
            softAssert.assertAll();
        } else
            Assert.assertNull(tema);
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