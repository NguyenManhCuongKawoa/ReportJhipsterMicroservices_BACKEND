package com.babyboy.social.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PostEmotion.
 */
@Entity
@Table(name = "post_emotion")
public class PostEmotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @NotNull
    @Column(name = "emotion_id", nullable = false)
    private Long emotionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostEmotion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public PostEmotion userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public PostEmotion postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getEmotionId() {
        return this.emotionId;
    }

    public PostEmotion emotionId(Long emotionId) {
        this.setEmotionId(emotionId);
        return this;
    }

    public void setEmotionId(Long emotionId) {
        this.emotionId = emotionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostEmotion)) {
            return false;
        }
        return id != null && id.equals(((PostEmotion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostEmotion{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", postId=" + getPostId() +
            ", emotionId=" + getEmotionId() +
            "}";
    }
}
