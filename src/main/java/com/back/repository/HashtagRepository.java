package com.back.repository;

import com.back.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Set<Hashtag> findByHashtagNameIn(Set<String> hashtagNames);
}
