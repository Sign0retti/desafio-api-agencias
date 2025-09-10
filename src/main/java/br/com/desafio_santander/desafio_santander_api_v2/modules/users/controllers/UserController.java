package br.com.desafio_santander.desafio_santander_api_v2.modules.users.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio_santander.desafio_santander_api_v2.modules.users.DTO.UserDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.users.entities.UserEntity;
import br.com.desafio_santander.desafio_santander_api_v2.modules.users.useCase.UserUseCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping("/create")
public ResponseEntity<Object> createUser(@RequestBody UserDTO userDTO) {
        try {
            
            // Verifica se já existe usuário com mesmo e-mail
            if (userUseCase.userExists(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um usuário com este e-mail!");
            }
    
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());


        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userEntity.setPassword(encodedPassword);
        UserEntity savedUser = userUseCase.createUser(userEntity);
        
        
        UserDTO result = UserDTO.fromEntity(savedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
        
        }catch(Exception e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar usuário: " + e.getMessage());

        }
    }
}