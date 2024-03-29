package com.dt.netflixclonebackend.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.dt.netflixclonebackend.domain.enums.ContentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Content implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long contentId;

    @NotNull
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @NotNull
    @Column(columnDefinition = "longtext")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    private double duration;

    private String filePath;

    @ManyToMany
    private List<Genre> genres;

    @ManyToOne
    @JoinColumn(name = "watchlist_id")
    private Watchlist watchlist;

    public Content() {
    }

    public Content(@NotNull Long contentId, @NotNull String title, @NotNull ContentType contentType,
            @NotNull String description, @NotNull LocalDate releaseDate, double duration, String filePath,
            List<Genre> genres, Watchlist watchlist) {
        this.contentId = contentId;
        this.title = title;
        this.contentType = contentType;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.filePath = filePath;
        this.genres = genres;
        this.watchlist = watchlist;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Watchlist getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((contentId == null) ? 0 : contentId.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
        long temp;
        temp = Double.doubleToLongBits(duration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
        result = prime * result + ((genres == null) ? 0 : genres.hashCode());
        result = prime * result + ((watchlist == null) ? 0 : watchlist.hashCode());
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
        Content other = (Content) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (contentId == null) {
            if (other.contentId != null)
                return false;
        } else if (!contentId.equals(other.contentId))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (contentType != other.contentType)
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
        if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
            return false;
        if (filePath == null) {
            if (other.filePath != null)
                return false;
        } else if (!filePath.equals(other.filePath))
            return false;
        if (genres == null) {
            if (other.genres != null)
                return false;
        } else if (!genres.equals(other.genres))
            return false;
        if (watchlist == null) {
            if (other.watchlist != null)
                return false;
        } else if (!watchlist.equals(other.watchlist))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Content [id=" + id + ", contentId=" + contentId + ", title=" + title + ", contentType=" + contentType
                + ", description=" + description + ", releaseDate=" + releaseDate + ", duration=" + duration
                + ", filePath=" + filePath + ", genres=" + genres + ", watchlist=" + watchlist + "]";
    }

}
