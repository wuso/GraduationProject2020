package com.suda.scst.repositories;

import com.suda.scst.domain.Major;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MajorRepository extends Neo4jRepository<Major, Long> {

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
    Major findByName(@Param("name") String name);

    Collection<Major> findByNameLike(@Param("name") String name);

//    @Query("MATCH (m:Class)<-[r:STUDY_IN]-(a:Student) RETURN m,r,a LIMIT {limit}")
////    Collection<Student> graph(@Param("limit") int limit);
////
////    @Query("MATCH (n:Student) RETURN n ")
////    List<Student> getUserNodeList();
}
