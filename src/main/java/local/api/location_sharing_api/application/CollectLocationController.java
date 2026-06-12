package local.api.location_sharing_api.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import local.api.location_sharing_api.domain.collect.dtos.CollectLocationInputDto;
import local.api.location_sharing_api.domain.collect.dtos.CollectLocationOutputDto;


@RestController
@RequestMapping("/collect-location")
public class CollectLocationController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.amqp.exchange.direction}")
    private String exchangeName;

    @Value("${app.amqp.queue.name}")
    private String routingKey;
    
    @PostMapping
    public ResponseEntity<CollectLocationOutputDto> sendLocationToQueue(@RequestBody CollectLocationInputDto entity) {
        
        rabbitTemplate.convertAndSend(exchangeName, routingKey, entity);
        
        CollectLocationOutputDto output = new CollectLocationOutputDto(entity, 
            "Location received successfully and sent for processing."
        );

        return ResponseEntity.ok(output);
        
    }
    
}
