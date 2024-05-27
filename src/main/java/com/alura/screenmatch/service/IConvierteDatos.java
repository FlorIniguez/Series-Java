package com.alura.screenmatch.service;

public interface IConvierteDatos {

    //las T dicen que el tipo de dato va a ser generico
   <T> T obtenerDatos(String json, Class<T> clase);

}
