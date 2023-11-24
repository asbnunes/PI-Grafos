package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private int pos_atual;
    private boolean inicio;
    @JsonProperty("final")
    private boolean fim;
    private int[] movimentos;
}
