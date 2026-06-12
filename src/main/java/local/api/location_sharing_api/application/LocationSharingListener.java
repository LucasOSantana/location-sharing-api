package local.api.location_sharing_api.application;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import local.api.location_sharing_api.domain.collect.dtos.CollectLocationInputDto;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LocationSharingListener {
    
    @Autowired
    private LocationSharingService locationSharingService;

    @RabbitListener(queues = "${app.amqp.queue.name}")
    public void listenLocationSharing(CollectLocationInputDto locationData) {
        log.info("Received message from queue for ID: {}", locationData.getId());
        locationSharingService.processLocationUpdate(locationData);
    }
}
