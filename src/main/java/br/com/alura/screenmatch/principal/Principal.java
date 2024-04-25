package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private final String ADRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void showMenu() {
        System.out.println("Digite o nome da série para a busca:");
        var busca = scanner.nextLine();
        var seriesName = busca.replace(" ", "+");
        var json = consumoApi.obterDados(ADRESS + seriesName + API_KEY);
        var dadosSerie = converteDados.obterDados(json, DadosSerie.class);

        List<DadosTemporada> seasons = new ArrayList<>();
        for(int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ADRESS + seriesName + "&season=" + i + API_KEY);
            var dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
            seasons.add(dadosTemporada);
        }

//        seasons.forEach(System.out::println);

//        seasons.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = seasons.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList()); // poderia usar o toList(), porém ele me retornaria uma lista imutável

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = seasons.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .toList();

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = scanner.nextInt();
        scanner.nextLine();

        LocalDate searchDate = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        episodios.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeason()
                        + " Episódio: " + e.getTitle()
                        + " Data Lançamento: " + e.getReleaseDate().format(formatter)
                ));
    }
}
