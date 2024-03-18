package com.dt.netflixclonebackend.service.dto;

import java.time.LocalDate;
import java.util.List;

public class ContentMovieDTO {

    private String title;
    private String overview;
    private LocalDate release_date;
    private List<Long> genre_ids;

    public ContentMovieDTO() {
    }

    public ContentMovieDTO(String title, String overview, LocalDate release_date, List<Long> genre_ids) {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
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

    public List<Long> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Long> genre_ids) {
        this.genre_ids = genre_ids;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((overview == null) ? 0 : overview.hashCode());
        result = prime * result + ((release_date == null) ? 0 : release_date.hashCode());
        result = prime * result + ((genre_ids == null) ? 0 : genre_ids.hashCode());
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
        ContentMovieDTO other = (ContentMovieDTO) obj;
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
        if (genre_ids == null) {
            if (other.genre_ids != null)
                return false;
        } else if (!genre_ids.equals(other.genre_ids))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ContentMovieDTO [title=" + title + ", overview=" + overview + ", release_date=" + release_date
                + ", genre_ids=" + genre_ids + "]";
    }

}
