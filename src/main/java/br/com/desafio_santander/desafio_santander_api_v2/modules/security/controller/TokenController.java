package br.com.desafio_santander.desafio_santander_api_v2.modules.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtEncoder jwtEncoder;

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> generateToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret) {
        
        try {
            // Verificar se é o grant type correto
            if (!"password".equals(grantType)) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "unsupported_grant_type");
                error.put("error_description", "Grant type deve ser 'password'");
                return ResponseEntity.badRequest().body(error);
            }

            // Verificar credenciais do cliente
            if (!"desafio-client".equals(clientId) || !"desafio-secret".equals(clientSecret)) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "invalid_client");
                error.put("error_description", "Credenciais do cliente inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Autenticar o usuário
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // Gerar JWT token
            Instant now = Instant.now();
            Instant expiry = now.plus(2, ChronoUnit.HOURS);

            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(authentication.getName())
                .claim("scope", "read write")
                .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

            // Resposta com o token
            Map<String, Object> response = new HashMap<>();
            response.put("access_token", token);
            response.put("token_type", "Bearer");
            response.put("expires_in", 7200); // 2 horas em segundos
            response.put("scope", "read write");

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "invalid_grant");
            error.put("error_description", "Credenciais de usuário inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "server_error");
            error.put("error_description", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
