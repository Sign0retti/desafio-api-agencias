package br.com.desafio_santander.desafio_santander_api_v2.modules.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class OAuth2LoginController {

    /**
     * Página de login simples que redireciona para o Authorization Server
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    /**
     * Callback que recebe o authorization code
     */
    @GetMapping("/callback")
    @ResponseBody
    public String callback(@RequestParam String code, @RequestParam(required = false) String state) {
        return "<!DOCTYPE html>" +
                "<html><head><title>OAuth2 Callback</title></head><body>" +
                "<h2>OAuth2 Authorization Code Received!</h2>" +
                "<p><strong>Authorization Code:</strong> " + code + "</p>" +
                "<p>Agora você pode trocar este código por um access token usando:</p>" +
                "<pre>POST /oauth2/token</pre>" +
                "<p><a href='/swagger-ui.html'>Ir para Swagger UI</a></p>" +
                "</body></html>";
    }

    /**
     * Página inicial que explica como usar OAuth2
     */
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<!DOCTYPE html>" +
                "<html><head><title>Desafio OAuth2</title></head><body>" +
                "<h1>Desafio Santander - OAuth2 API</h1>" +
                "<h2>Fluxo OAuth2 Authorization Code</h2>" +
                "<ol>" +
                "<li><a href='/oauth2/authorize?response_type=code&client_id=client-app&redirect_uri=http://localhost:8080/callback&scope=read write'>Iniciar fluxo OAuth2</a></li>" +
                "<li>Fazer login com suas credenciais</li>" +
                "<li>Autorizar a aplicação</li>" +
                "<li>Receber authorization code no callback</li>" +
                "<li>Trocar code por access token via POST /oauth2/token</li>" +
                "<li>Usar access token para acessar APIs protegidas</li>" +
                "</ol>" +
                "<h3>Endpoints Disponíveis:</h3>" +
                "<ul>" +
                "<li>POST /user/create - Criar usuário</li>" +
                "<li>GET /oauth2/authorize - Iniciar fluxo OAuth2</li>" +
                "<li>POST /oauth2/token - Trocar code por token</li>" +
                "<li>POST /desafio/cadastrar - Cadastrar agência (requer token)</li>" +
                "<li>GET /desafio/search - Buscar agências (requer token)</li>" +
                "</ul>" +
                "</body></html>";
    }
}
