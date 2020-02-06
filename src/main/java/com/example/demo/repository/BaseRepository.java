package com.example.demo.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<E> extends JpaRepository<E, Long> {

    List<E> findAll(Specification<E> specification);
    Optional<E> findOne(Specification<E> specification);
}
