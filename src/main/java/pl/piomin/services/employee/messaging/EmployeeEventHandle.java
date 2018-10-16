package pl.piomin.services.employee.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.piomin.services.employee.Constants;
import pl.piomin.services.employee.model.Employee;
import pl.piomin.services.employee.repository.EmployeeRepository;

import java.io.IOException;

/**
 * Created by truongnguyen on 10/16/18.
 */
@Component
public class EmployeeEventHandle {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @RabbitListener(queues = {Constants.MQ_EMPLOYEE_CREATE})
    public void createEmployee(String employee) throws IOException {
        System.out.print("Employee..." + employee);
        repository.save(objectMapper.readValue(employee, Employee.class));
    }
}
