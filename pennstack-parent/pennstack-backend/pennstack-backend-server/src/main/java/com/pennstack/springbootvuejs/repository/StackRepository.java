package com.pennstack.springbootvuejs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pennstack.client.domain.Stack;

@Repository
public interface StackRepository extends JpaRepository<Stack,Long>{


}
