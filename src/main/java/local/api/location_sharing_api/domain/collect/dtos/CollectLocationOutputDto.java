package local.api.location_sharing_api.domain.collect.dtos;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Validated
@NoArgsConstructor
public class CollectLocationOutputDto {
    @NotNull
    private UUID id;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    @NotNull
    private String timestamp;
    
    private String message;

    public CollectLocationOutputDto(CollectLocationInputDto input, String message) {
        this.id = input.getId();
        this.latitude = input.getLatitude();
        this.longitude = input.getLongitude();
        this.timestamp = input.getTimestamp();
        this.message = message;
    }
}
