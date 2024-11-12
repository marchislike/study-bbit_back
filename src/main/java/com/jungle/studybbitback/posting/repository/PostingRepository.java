package com.jungle.studybbitback.posting.repository;

import com.jungle.studybbitback.posting.entity.Posting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    @EntityGraph(attributePaths = {"comments"})
    Optional<Posting> findById(Long id);
}
