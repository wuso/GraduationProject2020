package com.suda.scst.repositories;

import com.suda.scst.domain.Major;
import com.suda.scst.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface UserRepository extends Neo4jRepository<User, Long> {

    User findByUsername(@Param("username") String name);

    Collection<User> findByUsernameLike(@Param("username") String name);
}
