package com.dt.netflixclonebackend.service.dto;

import java.time.LocalDate;
import java.util.List;

public class TMDBMovieDTO {

    private String title;
    private String overview;
    private LocalDate release_date;
    private List<GenreDTO> genreDTOs;

    public TMDBMovieDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public LocalDate getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDate release_date) {
        this.release_date = release_date;
    }

    public List<GenreDTO> getGenreDTOs() {
        return genreDTOs;
    }

    public void setGenreDTOs(List<GenreDTO> genreDTOs) {
        this.genreDTOs = genreDTOs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((overview == null) ? 0 : overview.hashCode());
        result = prime * result + ((release_date == null) ? 0 : release_date.hashCode());
        result = prime * result + ((genreDTOs == null) ? 0 : genreDTOs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TMDBMovieDTO other = (TMDBMovieDTO) obj;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (overview == null) {
            if (other.overview != null)
                return false;
        } else if (!overview.equals(other.overview))
            return false;
        if (release_date == null) {
            if (other.release_date != null)
                return false;
        } else if (!release_date.equals(other.release_date))
            return false;
        if (genreDTOs == null) {
            if (other.genreDTOs != null)
                return false;
        } else if (!genreDTOs.equals(other.genreDTOs))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TMDBMovieDTO [title=" + title + ", overview=" + overview + ", release_date=" + release_date
                + ", genreDTOs=" + genreDTOs + "]";
    }

}
