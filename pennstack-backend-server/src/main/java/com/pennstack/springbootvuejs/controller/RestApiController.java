package com.pennstack.springbootvuejs.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pennnstack.springbootvuejs.util.CustomErrorType;
import com.pennstack.client.StackBackendRestClient;
import com.pennstack.client.domain.Stack;
import com.pennstack.springbootvuejs.service.StackService;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class RestApiController implements StackBackendRestClient {

	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	StackService stackService; // Service which will do all data retrieval/manipulation work

	// -------------------Retrieve All
	// Stackss--------------------------------------------

	public ResponseEntity<List<Stack>> findAllStacks() {
		List<Stack> stacks = stackService.findAllStacks();
		if (stacks.isEmpty()) {
			return new ResponseEntity<>(stacks, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(stacks, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Stacks------------------------------------------

	public ResponseEntity<?> getStack(@PathVariable("id") long id) {
		logger.info("Fetching Stacks with id {}", id);
		Optional<Stack> stack = stackService.findById(id);
		if (stack == null) {
			logger.error("stack with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Stacks with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(stack, HttpStatus.OK);
	}

	// ------------------- Update a Stacks
	// ------------------------------------------------

	@RequestMapping(value = "/stack/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStack(@PathVariable("id") long id, @RequestBody Stack stack) {
		logger.info("Updating Stack with id {}", id);

		stackService.updateStack(stack);
		return new ResponseEntity<>(stack, HttpStatus.OK);
	}

	// ------------------- Delete a Stack-----------------------------------------

	public ResponseEntity<?> deleteStack(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting stack with id {}", id);

		Optional<Stack> stack = stackService.findById(id);
		if (stack == null) {
			logger.error("Unable to stack. Stack with id {} not found.", id);
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. stack with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		stackService.deleteStackById(id);
		return new ResponseEntity<Stack>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All Stacks-----------------------------

	public ResponseEntity<Stack> deleteAllStacks() {
		logger.info("Deleting All Stacks");

		stackService.deleteAllStacks();
		return new ResponseEntity<Stack>(HttpStatus.NO_CONTENT);
	}

	@Override
	public ResponseEntity<?> createStack(Stack stack) {
		logger.info("Creating Stacks : {}", stack);

		if (stackService.isStackExist(stack)) {
			logger.error("Unable to create. A Stack with name {} already exist", stack.getName());
			return new ResponseEntity<>(
					new CustomErrorType("Unable to create. A Stack with name " + stack.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		stackService.saveStack(stack);

		return new ResponseEntity<>(stack, HttpStatus.CREATED);
	}


}