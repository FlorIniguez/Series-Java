package com.alura.screenmatch.principal;

import com.alura.screenmatch.model.*;
import com.alura.screenmatch.repository.SerieRepository;
import com.alura.screenmatch.service.ConsumoAPI;
import com.alura.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ff6119f0";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }


    public void mostrarMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Motrar series buscadas
                    4 - Buscar series por tìtulo
                    5 - Top 5 mejores series
                    6 - Buscar series por categoria
                    7 - Serie por num de temporadas y puntaje
                    8 - Buscar espisodios por titulo
                    9 - Top 5 episodios por serie
                                        
                    0 - salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }


    //conexion para traer datos serie
    private DatosSerie getDatosSerie() {
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar: ");
        //BUSCA LOS DATOS GENERALES DE LAS SERIES
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    //conexion para buscar temporadas
    public void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }

    }

    private void buscarSerieWeb() {
        //traigo los datos de la serie
        DatosSerie datos = getDatosSerie();
        //paso los datos que recibi de la api en una nueva instancia de serie
        Serie serie = new Serie(datos);
        //guardo la serie en el repositorio
        repositorio.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombre de la serie que desea buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);
        if (serieBuscada.isPresent()) {
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }

    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " Evaluaciòn: " + s.getEvaluacion()));

    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escriba el género/categoria de la serie que desea buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero);
        seriesPorCategoria.forEach(System.out::println);

    }

    private void buscarSeriesPorTemporadaYEvaluacion() {
        System.out.println("Ingrese el numero de temporadas a filtrar");
        var totalTemporadas = teclado.nextInt();
        System.out.println("Ingrese la evaluación");
        var evaluacion = teclado.nextDouble();
        List<Serie> seriesFiltradas = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        seriesFiltradas.forEach(s ->
                System.out.println(s.getTitulo() + " - evaluacion; " + s.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

    }

    private void buscarTop5Episodios() {
        //para ver cual serie va a querer ver
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.topEpisodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
            //no tengo que tratar el else porq ya lo trata el buscarSeriesPorTitulo
        }

    }


}
