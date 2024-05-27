package com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisopdio;
    private Double evaluacion;
    private LocalDate fechaLanzamiento;
    @ManyToOne
    private Serie serie;


    public Episodio() {
    }

    public Episodio(Integer numero, DatosEpisodio d) {
        this.temporada = numero;
        this.titulo = d.titulo();
        this.numeroEpisopdio = d.numeroEpisodio();
        try {
            this.evaluacion = Double.valueOf(d.evaluacion());
        } catch (NumberFormatException e) {
            this.evaluacion = 0.0;
        }
        try {
            this.fechaLanzamiento = LocalDate.parse(d.fechaLanzamiento());
        } catch (DateTimeParseException e) {
            this.fechaLanzamiento = null;
        }


    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisopdio() {
        return numeroEpisopdio;
    }

    public void setNumeroEpisopdio(Integer numeroEpisopdio) {
        this.numeroEpisopdio = numeroEpisopdio;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                        ", titulo='" + titulo + '\'' +
                        ", numeroEpisopdio=" + numeroEpisopdio +
                        ", evaluaciones=" + evaluacion +
                        ", fechaLanzamiento=" + fechaLanzamiento;
    }
}
