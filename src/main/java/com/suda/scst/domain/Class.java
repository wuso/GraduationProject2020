package com.suda.scst.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Class {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String major;
    private String teacher;
    private int grade;
    private int class_id;

//    @Relationship(type = "BELONG_TO")
//    private Major major1;

    @Relationship(type = "STUDY_IN", direction = Relationship.INCOMING)
    private Set<Student> students;


    public void addStudent(Student student){
        if(students == null) {
            students = new HashSet<>();
            }
            students.add(student);
    }

    public Class(){

    }

    public Class(String major, String name,String teacher, int grade, int class_id) {
        this.major = major;
        this.name = name;
        this.teacher = teacher;
        this.grade = grade;
        this.class_id = class_id;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

}
