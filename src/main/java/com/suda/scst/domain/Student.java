package com.suda.scst.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Odinnnnn
 */
@NodeEntity
public class Student {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String gender;
    private Long student_id;
    private int birth;
    private String clazz;

    //忽略类中不存在的字段
    //@JsonIgnoreProperties
//    @Relationship(type = "STUDY_IN", direction = Relationship.OUTGOING)
//    private Class clazz;


    public Student() {
    }

    public Student(String name, Long student_id, String gender, int birth, String clazz) {
        this.name = name;
        this.student_id = student_id;
        this.gender = gender;
        this.birth = birth;
        this.clazz = clazz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public Long getStudent_id() {
        return student_id;
}

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

//    public List<Class> getClassList() {
//        return classList;
//    }
//
//    public void setClassList(List<Class> classList) {
//        this.classList = classList;
//    }
//
//    public void addClass(Class clazz) {
//        this.classList.add(clazz);
//    }
}