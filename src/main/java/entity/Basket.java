package entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class Basket {
    private final UUID id = UUID.randomUUID();
    private Long chatId_asUserId;
}
