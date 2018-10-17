package pl.piomin.services.employee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piomin.services.employee.Constants;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

import java.util.List;

@RestController
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
    EmployeeRepository repository;

	@PostMapping("/")
	public ResponseEntity<?> add(@RequestBody Employee employee) throws JsonProcessingException {
		LOGGER.info("Employee add: {}", employee);
		String json = objectMapper.writeValueAsString(employee);
		rabbitTemplate.convertAndSend(Constants.MQ_EMPLOYEE_EXCHANGE, Constants.MQ_EMPLOYEE_CREATE, json);
		return new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public Employee findById(@PathVariable("id") String id) {
		LOGGER.info("Employee find: id={}", id);
		return repository.findById(id).get();
	}
	
	@GetMapping("/")
	public Iterable<Employee> findAll() {
		LOGGER.info("Employee find");
		return repository.findAll();
	}
	
	@GetMapping("/department/{departmentId}")
	public List<Employee> findByDepartment(@PathVariable("departmentId") Long departmentId) {
		LOGGER.info("Employee find: departmentId={}", departmentId);
		return repository.findByDepartmentId(departmentId);
	}
	
	@GetMapping("/organization/{organizationId}")
	public List<Employee> findByOrganization(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Employee find: organizationId={}", organizationId);
		return repository.findByOrganizationId(organizationId);
	}
	
}
