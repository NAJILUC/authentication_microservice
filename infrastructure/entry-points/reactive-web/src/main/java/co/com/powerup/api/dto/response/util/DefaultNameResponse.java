package co.com.powerup.api.dto.response.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DefaultNameResponse {

    private Long id;
    private String name;

    public DefaultNameResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}