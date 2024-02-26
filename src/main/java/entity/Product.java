package entity;

import enums.ProductStatus;
import interfaces.NameFetcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class Product implements NameFetcher {
    private final UUID id = UUID.randomUUID();
    private String name;
    private Integer retailPrice;
    private Integer originalPrice;
    private UUID categoryId;
    private ProductStatus productStatus;
    private String photoUrl;
    private String description;

    @Override
    public String getTitle() {
        return name;
    }
}
