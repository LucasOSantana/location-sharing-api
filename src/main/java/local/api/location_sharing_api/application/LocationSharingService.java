package local.api.location_sharing_api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import local.api.location_sharing_api.domain.collect.dtos.CollectLocationInputDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LocationSharingService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void processLocationUpdate(CollectLocationInputDto locationData) {
        log.info("Processing location update for ID: {} [Lat: {}, Long: {}]", 
            locationData.getId(), locationData.getLatitude(), locationData.getLongitude());
        
        String destination = "/topic/location/" + locationData.getId();
        messagingTemplate.convertAndSend(destination, locationData);
        
        log.info("Broadcasted location update to: {}", destination);
        
        // Future expansion: Save to Redis/Database here
    }
}
