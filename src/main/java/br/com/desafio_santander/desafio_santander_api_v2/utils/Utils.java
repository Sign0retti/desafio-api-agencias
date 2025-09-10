package br.com.desafio_santander.desafio_santander_api_v2.utils;

public final class Utils {
    
public static double toRad(double deg) {
    return Math.PI * deg / 180.00;
}

public static double calcDistance(double originX, double originY, 
                                 double destinationX, double destinationY) {
    return Math.sqrt(Math.pow(destinationX - originX, 2) + 
                    Math.pow(destinationY - originY, 2)); // ← CORRIGIDO!
}

// Exemplo de uso:
// double distancia = calcularDistancia(usuario.getX(), usuario.getY(), 
//                                     agencia1.getX(), agencia1.getY());

}
