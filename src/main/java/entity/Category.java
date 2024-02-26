package entity;

import enums.CategoryStatus;
import interfaces.NameFetcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Category implements NameFetcher {
    private String name;
    private final UUID id = UUID.randomUUID();
    private Integer restaurantId;
    private CategoryStatus categoryStatus;

    @Override
    public String getTitle() {
        return name;
    }
}
