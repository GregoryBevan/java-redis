package com.elgregos.java.redis.entities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elgregos.java.redis.entities.CompositeKeyEntity;

public interface CompositeKeyEntityRepository extends JpaRepository<CompositeKeyEntity, Long> {

}
