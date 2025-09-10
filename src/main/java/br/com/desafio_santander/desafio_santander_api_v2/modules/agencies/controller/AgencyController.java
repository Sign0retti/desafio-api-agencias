package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO.AgencyCalculatedDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.DTO.AgencyDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.useCases.AgencyUseCase;
import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.service.AgencyCacheService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/desafio")
public class AgencyController {

    @Autowired
    private AgencyUseCase agencyUseCase;
    
    @Autowired
    private AgencyCacheService agencyCacheService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> create(@Valid @RequestBody AgencyDTO body){

        try{

            //Verifica se os campos obrigatórios para criação da Agência foram preenchidos corretamente
            if(!agencyUseCase.isValid(body)){
                return ResponseEntity.badRequest().body("Confira os campos obrigatórios para criação de uma Agência");
            }

            AgencyDTO result = agencyUseCase.create(body);

            // Invalida o cache quando uma nova agência é criada
            agencyCacheService.invalidateCacheOnNewAgency();

            return ResponseEntity.ok().body(result);

        }catch(Exception e){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }

    }
    
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam Double posX, @RequestParam Double posY) {

        try{
            // Usa o serviço de cache que implementa as regras de invalidação
            List<AgencyCalculatedDTO> calculatedList = agencyCacheService.searchAgencies(posX, posY);
            
            return ResponseEntity.ok().body(calculatedList);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
