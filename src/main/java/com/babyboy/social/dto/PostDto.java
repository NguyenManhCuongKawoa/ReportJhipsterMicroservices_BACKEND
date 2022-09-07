package com.babyboy.social.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;


public class PostDto {

    private Long id;

    private String title;
    private String description;

    @NotNull
    private String content;

    private Integer totalEmotion;
    private Integer totalShare;
    private Integer mode;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private String userId;

    private List<Long> hashtagIds;
    private List<String> images;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTotalEmotion() {
        return totalEmotion;
    }

    public void setTotalEmotion(Integer totalEmotion) {
        this.totalEmotion = totalEmotion;
    }

    public Integer getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(Integer totalShare) {
        this.totalShare = totalShare;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Long> getHashtagIds() {
        return hashtagIds;
    }

    public void setHashtagIds(List<Long> hashtagIds) {
        this.hashtagIds = hashtagIds;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return (
            "PostDto{" +
            "id=" +
            id +
            ", title='" +
            title +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", content='" +
            content +
            '\'' +
            ", totalEmotion=" +
            totalEmotion +
            ", totalShare=" +
            totalShare +
            ", mode=" +
            mode +
            ", createdAt=" +
            createdAt +
            ", updatedAt=" +
            updatedAt +
            ", userId=" +
            userId +
            ", hashtagIds=" +
            hashtagIds +
            ", images=" +
            images +
            '}'
        );
    }
}
