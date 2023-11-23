package api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StartMazeRequest {
    private String id;
    private String labirinto;
}
