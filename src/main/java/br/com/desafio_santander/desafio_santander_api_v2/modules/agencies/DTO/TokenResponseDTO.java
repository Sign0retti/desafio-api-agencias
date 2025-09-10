package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class TokenResponseDTO {
    private String access_token;
    private String token_type;
    private long expires_in;

}

