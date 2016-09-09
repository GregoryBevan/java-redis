package com.elgregos.java.redis.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elgregos.java.redis.entities.SimpleEntity;

public interface SimpleEntityRepository extends JpaRepository<SimpleEntity, Long> {

}
