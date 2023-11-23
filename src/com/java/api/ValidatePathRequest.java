package api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidatePathRequest {
    private String id;
    private String labirinto;
    private int[] todos_movimentos;
}

