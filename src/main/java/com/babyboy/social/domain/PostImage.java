package com.babyboy.social.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PostImage.
 */
@Entity
@Table(name = "post_image")
public class PostImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    public PostImage() {}

    public PostImage(String image, Long postId) {
        this.image = image;
        this.postId = postId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PostImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return this.postId;
    }

    public PostImage postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getImage() {
        return this.image;
    }

    public PostImage image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostImage)) {
            return false;
        }
        return id != null && id.equals(((PostImage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostImage{" +
            "id=" + getId() +
            ", postId=" + getPostId() +
            ", image='" + getImage() + "'" +
            "}";
    }
}
