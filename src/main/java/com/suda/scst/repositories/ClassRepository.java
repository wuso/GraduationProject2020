package com.suda.scst.repositories;

import com.suda.scst.domain.Class;
import com.suda.scst.domain.Major;
import com.suda.scst.domain.Student;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource(collectionResourceRel = "classes", path = "classes")
public interface ClassRepository extends Neo4jRepository<Class, Long> {
    Class findByMajor(@Param("major") String major);

    Class findByName(@Param("name") String name);

    Collection<Class> findByMajorLike(@Param("major") String major);

    @Query("MATCH (a:Class),(b:Major) WHERE a.class_id = {class_id} AND b.name = {major} CREATE (a)-[r:BELONG_TO]->(b) RETURN r")
    void belongTo(@Param("class_id")int class_id,@Param("major")String major);

//    @Query("MATCH (m:Class)<-[r:STUDY_IN]-(a:Student) RETURN m,r,a LIMIT {limit}")
//    Collection<Class> graph(@Param("limit") int limit);
}
