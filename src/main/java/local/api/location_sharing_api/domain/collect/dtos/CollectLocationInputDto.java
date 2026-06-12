package local.api.location_sharing_api.domain.collect.dtos;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@Validated
public class CollectLocationInputDto {
    @NotNull
    private UUID id;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    @NotNull
    private String timestamp;   
}
