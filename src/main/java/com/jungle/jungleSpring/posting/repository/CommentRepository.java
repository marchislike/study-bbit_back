package com.jungle.jungleSpring.posting.repository;

import com.jungle.jungleSpring.posting.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
