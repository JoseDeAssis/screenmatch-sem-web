package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {

    private Integer season;
    private String title;
    private Integer episodeNumber;
    private Double rating;
    private LocalDate releaseDate;

    public Episodio(Integer season, DadosEpisodio dadosEpisodio) {
        this.season = season;
        this.title = dadosEpisodio.titulo();
        this.episodeNumber = dadosEpisodio.numero();
        try {
            this.rating = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException e) {
            this.rating = -1.0;
        }
        try {
            this.releaseDate = LocalDate.parse(dadosEpisodio.releaseDate());
        } catch (DateTimeParseException e) {
            this.releaseDate = null;
        }
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "season=" + season +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", rating=" + rating +
                ", releaseDate=" + releaseDate;
    }
}
