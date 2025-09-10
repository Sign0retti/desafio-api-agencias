package br.com.desafio_santander.desafio_santander_api_v2.modules.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {


    private String access_token;
    private Long expires_in;

    
}
