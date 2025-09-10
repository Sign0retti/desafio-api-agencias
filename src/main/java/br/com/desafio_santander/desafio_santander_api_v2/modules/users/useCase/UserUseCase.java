package br.com.desafio_santander.desafio_santander_api_v2.modules.users.useCase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.desafio_santander.desafio_santander_api_v2.modules.users.DTO.UserDTO;
import br.com.desafio_santander.desafio_santander_api_v2.modules.users.entities.UserEntity;
import br.com.desafio_santander.desafio_santander_api_v2.modules.users.repositories.UserRepository;

@Service
public class UserUseCase {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // Verifica se já existe usuário com o mesmo email
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


    //Verifica se a senha do usuário é correta com aquela que ele cadastrou anteriormente
    public UserEntity validateCredentials(UserDTO userDTO) {
        var user = userRepository.findByEmail(userDTO.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean matches = passwordEncoder.matches(userDTO.getPassword(), user.getPassword());

        System.out.println("Senha digitada: " + userDTO.getPassword());
        System.out.println("Hash no banco: " + user.getPassword());
        System.out.println("Resultado matches: " + matches);

            
    if (!matches) {
        throw new BadCredentialsException("Senha inválida");
    }

    if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Senha inválida");
    }

    return user;
}

    // Método para criar usuário
    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}