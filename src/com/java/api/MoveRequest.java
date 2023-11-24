package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MoveRequest {
    @JsonProperty("id")
    private String id;
    @JsonProperty("labirinto")
    private String labirinto;
    @JsonProperty("nova_posicao")
    private int nova_posicao;

}

