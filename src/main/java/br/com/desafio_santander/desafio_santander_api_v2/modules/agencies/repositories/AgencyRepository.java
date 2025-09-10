package br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.desafio_santander.desafio_santander_api_v2.modules.agencies.entities.AgencyEntity;


@Repository
public interface AgencyRepository extends JpaRepository<AgencyEntity,Long>{

    

}