package com.dt.netflixclonebackend.service.dto;

import java.time.LocalDate;
import java.util.List;

public class ContentMovieDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private String imageUrl;
    private List<GenreDTO> genreDTOs;

    public ContentMovieDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<GenreDTO> getGenreDTOs() {
        return genreDTOs;
    }

    public void setGenreDTOs(List<GenreDTO> genreDTOs) {
        this.genreDTOs = genreDTOs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
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
        ContentMovieDTO other = (ContentMovieDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (releaseDate == null) {
            if (other.releaseDate != null)
                return false;
        } else if (!releaseDate.equals(other.releaseDate))
            return false;
        if (imageUrl == null) {
            if (other.imageUrl != null)
                return false;
        } else if (!imageUrl.equals(other.imageUrl))
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
        return "ContentMovieDTO [id=" + id + ", title=" + title + ", description=" + description + ", releaseDate="
                + releaseDate + ", imageUrl=" + imageUrl + ", genreDTOs=" + genreDTOs + "]";
    }

}
