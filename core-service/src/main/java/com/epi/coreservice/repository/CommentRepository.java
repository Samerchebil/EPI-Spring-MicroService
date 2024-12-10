package com.epi.coreservice.repository;

import com.epi.coreservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> getCommentsByUserId(String id);
}
