package com.alura.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {
    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        //inicializa la variable
        HttpResponse<String> response = null;
        try {
            // en el bloque try, se asigna el valor real cuando se recibe la respuesta de la solicitud a la API
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        String json = response.body();
        return json;

    }

}
