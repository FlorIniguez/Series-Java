package com.alura.screenmatch.repository;

import com.alura.screenmatch.model.Categoria;
import com.alura.screenmatch.model.Episodio;
import com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
   Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

   List<Serie> findTop5ByOrderByEvaluacionDesc();

   List<Serie> findByGenero(Categoria categoria);

   //List<Serie> findByTotalTemporadasLessThnEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);
   @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND  s.evaluacion >= :evaluacion")
   List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas, Double evaluacion);

   //join porque relaciona 2 tablas
   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
   List<Episodio> episodiosPorNombre(String nombreEpisodio);

   @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
   List<Episodio> topEpisodios(Serie serie);

   //jpql agrupar esas series donde el orden de la fecha de lanzamiento
   @Query("SELECT s FROM Serie s " + "JOIN s.episodios e " + "GROUP BY s " + "ORDER BY MAX(e.fechaLanzamiento) DESC LIMIT 5")
   List<Serie> lanzamientosMasRecientes();

}
