package local.api.location_sharing_api.application;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import local.api.location_sharing_api.domain.collect.dtos.CollectLocationInputDto;

@ExtendWith(MockitoExtension.class)
public class LocationSharingServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private LocationSharingService locationSharingService;

    @Test
    public void shouldBroadcastLocationUpdate() {
        // Given
        UUID id = UUID.randomUUID();
        CollectLocationInputDto input = new CollectLocationInputDto();
        input.setId(id);
        input.setLatitude("-23.5505");
        input.setLongitude("-46.6333");
        input.setTimestamp("2026-06-12T10:00:00Z");

        // When
        locationSharingService.processLocationUpdate(input);

        // Then
        String expectedDestination = "/topic/location/" + id;
        verify(messagingTemplate).convertAndSend(eq(expectedDestination), eq(input));
    }
}
