package com.babyboy.social.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PostHashtag.
 */
@Entity
@Table(name = "post_hashtag")
public class PostHashtag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @NotNull
    @Column(name = "hashtag_id", nullable = false)
    private Long hashtagId;

    public PostHashtag() {}

    public PostHashtag(Long hashtagId, Long postId) {
        this.hashtagId = hashtagId;
        this.postId = postId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostHashtag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return this.postId;
    }

    public PostHashtag postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getHashtagId() {
        return this.hashtagId;
    }

    public PostHashtag hashtagId(Long hashtagId) {
        this.setHashtagId(hashtagId);
        return this;
    }

    public void setHashtagId(Long hashtagId) {
        this.hashtagId = hashtagId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostHashtag)) {
            return false;
        }
        return id != null && id.equals(((PostHashtag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostHashtag{" +
            "id=" + getId() +
            ", postId=" + getPostId() +
            ", hashtagId=" + getHashtagId() +
            "}";
    }
}
