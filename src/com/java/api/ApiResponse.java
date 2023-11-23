package api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private int pos_atual;
    private boolean inicio;
    private boolean fim;
    private int[] movimentos;
}
