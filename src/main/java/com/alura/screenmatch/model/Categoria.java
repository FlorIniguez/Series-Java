package com.alura.screenmatch.model;

public enum Categoria {
    ACCION("Action","Acción"),
    ROMANCE("Romance","Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama","Drama"),
    CRIMEN("Crime","Crimen");

    private String categoriaOMdb;
    private String categoriaEspanol;

    Categoria(String categoriaOMdb, String categoriaEspanol){
        this.categoriaOMdb = categoriaOMdb;
        this.categoriaEspanol = categoriaEspanol;
    }

        public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOMdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    //verifica si el texto es igual o puede igualarse a alguna de las opciones
    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
