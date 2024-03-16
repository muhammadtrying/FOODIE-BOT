package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketProduct {
    private final UUID id = UUID.randomUUID();
    private UUID basketId;
    private UUID productId;
    private int amount;
}
