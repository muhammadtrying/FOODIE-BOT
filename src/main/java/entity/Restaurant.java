package entity;

import interfaces.NameFetcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Restaurant implements NameFetcher {
    private String name;
    private String phoneNumber;
    private static Integer idGenerator = 10;
    private final Integer id = idGenerator++;
    private final String photoURL;

    @Override
    public String getTitle() {
        return name;
    }
}
