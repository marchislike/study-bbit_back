package com.jungle.studybbitback.domain.posting.repository;

import com.jungle.studybbitback.domain.posting.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
