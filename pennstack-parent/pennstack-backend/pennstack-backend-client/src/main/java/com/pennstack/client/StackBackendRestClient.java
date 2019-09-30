/**
 * 
 */
package com.pennstack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pennstack.client.domain.Stack;

/**
 * @author pmunusamyduraikan
 *
 */
@FeignClient(name = "backend-service")
public interface StackBackendRestClient {

	@RequestMapping(value = "/api/stack/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStack(@PathVariable("id") long id);
	
	@RequestMapping(value = "/api/stacks", method = RequestMethod.GET)
	public ResponseEntity<?> findAllStacks();

	@RequestMapping(value = "/api/stack/", method = RequestMethod.POST)
	public ResponseEntity<?> createStack(@RequestBody Stack stack);

	@RequestMapping(value = "/api/stack/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStack(@PathVariable("id") long id, @RequestBody Stack stack);

	@RequestMapping(value = "/api/stack/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStack(@PathVariable("id") long id);

	@RequestMapping(value = "/api/stacks/", method = RequestMethod.DELETE)
	public ResponseEntity<Stack> deleteAllStacks();
}
