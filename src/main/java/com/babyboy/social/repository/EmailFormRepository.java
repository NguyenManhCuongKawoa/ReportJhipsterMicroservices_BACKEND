package com.babyboy.social.repository;

import com.babyboy.social.domain.EmailForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailFormRepository extends JpaRepository<EmailForm, Long> {}
