package re1kur.mailService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectChangesEvent {

    private String objectChangesName;

    private Integer objectChangesId;

    private String changesBodyMessage;

    private LocalDateTime changesTimestamp;

}
