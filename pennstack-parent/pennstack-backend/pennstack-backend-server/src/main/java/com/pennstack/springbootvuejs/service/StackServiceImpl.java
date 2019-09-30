package com.pennstack.springbootvuejs.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pennstack.client.domain.Stack;
import com.pennstack.springbootvuejs.repository.StackRepository;

@Service("stackService")
public class StackServiceImpl implements StackService {

	@Autowired
	private StackRepository stackRepository;

	@Override
	public Optional<Stack> findById(long id) {
		// TODO Auto-generated method stub
		return stackRepository.findById(id);
	}

	@Override
	public List<Stack> findByName(String name) {
		// TODO Auto-generated method stub
		// return stackRepository.findbyName(name);
		return null;
	}

	@Override
	public Stack saveStack(Stack stack) {
		return stackRepository.save(stack);

	}

	@Override
	public void updateStack(Stack stack) {
//TODO
		// Modify
		Optional<Stack> stackFromDb = stackRepository.findById(stack.getId());

		if (stackFromDb != null && stackFromDb.isPresent()) {
			Stack stackObj = stackFromDb.get();
			stackObj.setCategory(stackObj.getCategory());
			stackObj.setLicenceType(stackObj.getLicenceType());
			stackObj.setName(stack.getName());

			stackRepository.save(stackObj);

		}
	}

	@Override
	public void deleteStackById(long id) {
		stackRepository.deleteById(id);
	}

	@Override
	public List<Stack> findAllStacks() {
		// TODO Auto-generated method stub
		return stackRepository.findAll();
	}

	@Override
	public void deleteAllStacks() {
		stackRepository.deleteAll();

	}

	@Override
	public boolean isStackExist(Stack stack) {
		Optional<Stack> stackFromDb = stackRepository.findById(stack.getId());
		boolean isStackExist = false;
		if (stackFromDb != null && stackFromDb.isPresent()) {
			isStackExist = true;
		}
		return isStackExist;
	}

}
