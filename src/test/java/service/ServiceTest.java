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

import java.time.LocalDate;

public class ServiceTest {

    private static final String filenameStudent = "fisiere//Studenti.xml";
    private static final String filenameTema = "fisiere//Teme.xml";
    private static final String filenameNota = "fisiere//Note.xml";
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
        System.out.println(listSize);
        service.addStudent(new Student(String.valueOf(System.currentTimeMillis()), "Test", 935, "test@scs.ubbcluj.ro"));
        Assert.assertEquals(listSize + 1, numberOfStudents(students));
    }

    @Test
    public void addTema() {
        Iterable<Tema> teme = service.getAllTeme();
        int listSize = numberOfTeme(teme);
        service.addTema(new Tema(ID, "Test", 14, 2));
        Assert.assertEquals(listSize + 1, numberOfTeme(teme));
    }

//    @Test
//    public void addToTemaONota() {
//        Iterable<Nota> note = service.getAllNote();
//        int listSize = numberOfNote(note);
//        service.addNota(new Nota(String.valueOf(System.currentTimeMillis()), "100", ID, 10,
//                LocalDate.of(2019, 2,27)), "Well done");
//        Assert.assertEquals(listSize + 1, numberOfNote(note));
//    }

    private int numberOfStudents(Iterable<Student> list) {
        int s = 0;
        for (Student student : list)
            s++;
        return s;
    }

    private int numberOfTeme(Iterable<Tema> list) {
        int s = 0;
        for (Tema tema : list) {
            s++;
            System.out.println(tema);
        }
        return s;
    }

    private int numberOfNote(Iterable<Nota> list) {
        int s = 0;
        for (Nota nota : list)
            s++;
        return s;
    }
}
