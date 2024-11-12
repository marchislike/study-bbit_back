package com.jungle.studybbitback.posting.repository;

import com.jungle.studybbitback.posting.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
