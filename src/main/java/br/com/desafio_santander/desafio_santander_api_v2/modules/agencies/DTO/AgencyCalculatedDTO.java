package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO;

import lombok.Data;

@Data
public class AgencyCalculatedDTO {
    private Long id;
    private String name;
    private Double posX;
    private Double posY;
    private Double distance;

    public AgencyCalculatedDTO(AgencyDTO agencyDTO, Double distance){
        this.id = agencyDTO.getId();
        this.name = agencyDTO.getName();
        this.posX = agencyDTO.getPosX();
        this.posY = agencyDTO.getPosY();
        this.distance = distance;
        
    }

    



    
}
