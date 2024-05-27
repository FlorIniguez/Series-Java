package com.alura.screenmatch.service;

import com.alura.screenmatch.dto.SerieDTO;
import com.alura.screenmatch.model.Serie;
import com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    public List<SerieDTO> obtenerTodasLasSeries() {
        return convierteDatos(serieRepository.findAll());
    }

    public List<SerieDTO> obtenerTop5() {
        return convierteDatos(serieRepository.findTop5ByOrderByEvaluacionDesc());

    }

    public List<SerieDTO> obtenerLanzamientosMasRecientes(){
        return convierteDatos(serieRepository.lanzamientosMasRecientes());
    }

    public SerieDTO obtenerPorId(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(s.getId(),s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(),
                    s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis());
        }
        return null;
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie) {
        return serie.stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitulo(), s.getTotalTemporadas(), s.getEvaluacion(),
                        s.getPoster(), s.getGenero(), s.getActores(), s.getSinopsis()))
                .collect(Collectors.toList());
    }


}
