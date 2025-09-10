package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO;


import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.entities.AgencyEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgencyDTO {

    private Long id;
    private String name;
    private Double posX;
    private Double posY;

    public AgencyDTO bind(AgencyEntity entity){

        AgencyDTO DTO = new AgencyDTO();

        DTO.id = entity.getId();
        DTO.name = entity.getName();
        DTO.posX = entity.getPosX();
        DTO.posY = entity.getPosY();

        return DTO;
    }

    
}
