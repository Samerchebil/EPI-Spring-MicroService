package com.epi.coreservice.controller;

import com.epi.coreservice.dto.CommentDto;
import com.epi.coreservice.request.comment.CommentCreateRequest;
import com.epi.coreservice.request.comment.CommentUpdateRequest;
import com.epi.coreservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/core-service/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestPart CommentCreateRequest request,
                                                    @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(commentService.createComment(request, file), CommentDto.class));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CommentDto>> getAll() {
        return ResponseEntity.ok(commentService.getAll().stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class)).toList());
    }

    @GetMapping("/getCommentById/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(commentService.getCommentById(id), CommentDto.class));
    }

    @GetMapping("/getCommentsByUserId/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByUserId(@PathVariable String id) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(id).stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class)).toList());
    }

    @PutMapping("/update")
    public ResponseEntity<CommentDto> updateCommentById(@Valid @RequestBody CommentUpdateRequest request,
                                                      @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(commentService.updateCommentById(request, file), CommentDto.class));
    }

    @DeleteMapping("/deleteCommentById/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable String id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok().build();
    }
}
