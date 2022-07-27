package com.babyboy.social.repository;

import com.babyboy.social.domain.UserHashtag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserHashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {}
