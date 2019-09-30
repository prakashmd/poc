package com.pennstack.springbootvuejs.service;


import java.util.List;
import java.util.Optional;

import com.pennstack.client.domain.Stack;


public interface StackService {
	
	Optional<Stack> findById(long id);
	
	List<Stack> findByName(String name);
	
	Stack saveStack(Stack stack);
	
	void updateStack(Stack stack);
	
	void deleteStackById(long id);

	List<Stack> findAllStacks();
	
	void deleteAllStacks();
	
	boolean isStackExist(Stack stack);
	
}
