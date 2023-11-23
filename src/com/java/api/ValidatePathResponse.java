package api;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatePathResponse {
    private boolean caminho_valido;
    private int quantidade_movimentos;

}
