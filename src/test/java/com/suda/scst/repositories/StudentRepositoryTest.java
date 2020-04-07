package com.suda.scst.repositories;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Student;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author pdtyreus
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Before
    public void setUp() {
        //Student student = new Student("The Matrix", 1, "male", 1999, "com");
        //studentRepository.save(student);

//        Class clazz = new Class("className");
//        student.addClass(clazz);

        //studentRepository.save(student);
    }

    /**
     * Test of findByTitle method, of class MovieRepository.
     */
    @Test
    public void testFindByTitle() {

        String title = "The Matrix";
        Student result = studentRepository.findByName(title);
        assertNotNull(result);
        assertEquals(1999, result.getBirth());
    }

    /**
     * Test of findByTitleContaining method, of class MovieRepository.
     */
    @Test
    public void testFindByTitleContaining() {
        String title = "*Matrix*";
        Collection<Student> result = studentRepository.findByNameLike(title);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}