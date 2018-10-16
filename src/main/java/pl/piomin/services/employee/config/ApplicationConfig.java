package pl.piomin.services.employee.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.piomin.services.employee.Constants;

/**
 * Created by truongnguyen on 10/12/18.
 */
@Configuration
@ComponentScan(basePackages = {"pl.piomin.services.employee"})
@EnableRabbit
public class ApplicationConfig {

    @Bean
    Queue employeeCreateQueue() {
        return new Queue(Constants.MQ_EMPLOYEE_CREATE, false);
    }

    @Bean
    DirectExchange employeeCommandExchange() {
        return new DirectExchange(Constants.MQ_EMPLOYEE_EXCHANGE);
    }

    @Bean
    Binding employeeCreateBinding(Queue employeeCreateQueue, DirectExchange employeeCommandExchange) {
        return BindingBuilder.bind(employeeCreateQueue).to(employeeCommandExchange).with(Constants.MQ_EMPLOYEE_CREATE);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("rabbit");
        connectionFactory.setPassword("i7VpLzHGyGK6");
        connectionFactory.setAddresses("35.198.209.3:5672,35.198.209.3:5671,35.198.209.3:25672,35.198.209.3:4369,35.198.209.3:15672");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        System.out.println("host===" + connectionFactory.getHost() + " port===" + connectionFactory.getPort() + connectionFactory.getUsername());
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

}

