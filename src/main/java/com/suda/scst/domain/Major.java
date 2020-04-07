package com.suda.scst.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Major {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int major_id;
    private String faculty;
    private String research;


    @Relationship(type = "BELONG_TO", direction = Relationship.INCOMING)
    private Set<Class> classes;

    public void addClass(Class clazz){
        if(classes == null) {
            classes = new HashSet<>();
        }
        classes.add(clazz);
    }

    public  Major(){
    }

    public Major(String name, int major_id, String faculty, String research) {
        this.name = name;
        this.major_id = major_id;
        this.faculty = faculty;
        this.research = research;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMajor_id() {
        return major_id;
    }

    public void setMajor_id(int major_id) {
        this.major_id = major_id;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

}
