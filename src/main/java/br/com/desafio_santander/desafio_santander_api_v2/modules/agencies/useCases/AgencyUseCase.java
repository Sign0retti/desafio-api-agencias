package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.useCases;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO.AgencyDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.entities.AgencyEntity;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.repositories.AgencyRepository;

@Service
public class AgencyUseCase {
    

    @Autowired
    AgencyRepository agencyRepository;

    public boolean isValid(AgencyDTO agencyDTO){

        if(agencyDTO == null){
            return false;
        }

        if(agencyDTO.getName() == null || agencyDTO.getName() == ""){
            return false;
        }

        if(agencyDTO.getPosX() == null || agencyDTO.getPosX().isNaN()){
            return false;
        }

        if(agencyDTO.getPosY() == null || agencyDTO.getPosY().isNaN()){
            return false;
        }

        return true;

    }


    public AgencyDTO create(AgencyDTO agencyDTO){

        AgencyEntity agencyEntity = new AgencyEntity();
        
        agencyEntity.setName(agencyDTO.getName());
        agencyEntity.setPosX(agencyDTO.getPosX());
        agencyEntity.setPosY(agencyDTO.getPosY());

        agencyRepository.save(agencyEntity);

        return new AgencyDTO().bind(agencyEntity);


    }


   public ArrayList<AgencyDTO> readAll() {
    ArrayList<AgencyDTO> listDTO = new ArrayList<AgencyDTO>();
    
    List<AgencyEntity> list = agencyRepository.findAll();
    
    for (int i = 0; i < list.size(); i++) {
        AgencyDTO agency = new AgencyDTO().bind(list.get(i));
        listDTO.add(agency);
    }
    
    return listDTO;
}

    


}
