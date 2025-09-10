package br.com.desafio_santander.desafio_santander_api_v2.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio_santander.desafio_santander_api_v2.modules.users.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    
    Optional<UserEntity>findByEmail(String name);

}
