package com.suda.scst.repositories;

import com.suda.scst.domain.Teacher;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TeacherRepository extends Neo4jRepository<Teacher, Long> {

	/*
	 StudentRepositiory 继承 Neo4jRepository类，实现增删查改
	 实现自己的接口：通过名字查询Student（可以是单个节点，也可以是一组节点List集合）
	 spring-data-neo4j 支持方法命名约定查询 findBy{Student的属性名}
	 findBy后面的属性名一定要在Student节点实体类里存在，否则会报错
	 */

    /**
     * 根据名字查询学生节点
     * @param name
     * @return
     */
    Teacher findByName(@Param("name") String name);

    Collection<Teacher> findByNameLike(@Param("name") String name);

    @Query("MATCH (a:Teacher),(b:Class) WHERE a.class_id = {class_id} AND b.name = {major} CREATE (a)-[r:BELONG_TO]->(b) RETURN r")
    void belongTo(@Param("class_id")int class_id,@Param("major")String major);

    @Query("MATCH (a:Teacher),(b:Teacher) WHERE  a.teacher_id = {teacher_id} AND a.name <> b.name CREATE (a)-[r:COLLEAGUE]->(b),(b)-[d:COLLEAGUE]->(a) RETURN r")
    void colleague(@Param("teacher_id")int teacher_id);
}

