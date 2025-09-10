package br.com.desafio_santander.desafio_santander_api_v2.modules.users.DTO;

import br.com.desafio_santander.desafio_santander_api_v2.modules.users.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String name;
    private String email;
    private String password;

    public static UserDTO fromEntity(UserEntity userEntity) {

        UserDTO userDTO = new UserDTO();

        userDTO.setId(userEntity.getId());
        userDTO.setName(userEntity.getName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        
        
        return userDTO;
    }

}
