package local.api.location_sharing_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AmqpConfig {
    
    @Bean
    public Queue locationQueue(
        @Value("${app.amqp.queue.name}") String queueName,
        @Value("${app.amqp.exchange.direction}") String exchangeDirection
    ) {
        return QueueBuilder
            .durable(queueName)
            .withArgument("x-dead-letter-exchange", exchangeDirection)
            .withArgument("x-dead-letter-routing-key", queueName + ".dlq")   
            .build();
    }

    @Bean
    public DirectExchange directExchange(
        @Value("${app.amqp.exchange.direction}") String exchangeName
    ) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(
        Queue locationQueue,
        DirectExchange directExchange
    ) {
        return BindingBuilder.bind(locationQueue)
            .to(directExchange)
            .with(locationQueue.getName());
    }

    @Bean
    public Queue locationDlq(
        @Value("${app.amqp.queue.name}") String queueName
    ) {
        return QueueBuilder.durable(queueName + ".dlq").build();
    }

    @Bean
    public Binding dlqBinding(Queue locationDlq, DirectExchange directExchange) {
        return BindingBuilder.bind(locationDlq)
            .to(directExchange)
            .with(locationDlq.getName());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
