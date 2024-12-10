package com.epi.coreservice.service;

import com.epi.coreservice.client.FileStorageClient;
import com.epi.coreservice.client.UserServiceClient;
import com.epi.coreservice.dto.UserDto;
import com.epi.coreservice.exc.NotFoundException;
import com.epi.coreservice.model.Comment;
import com.epi.coreservice.model.Product;
import com.epi.coreservice.repository.CommentRepository;
import com.epi.coreservice.request.comment.CommentCreateRequest;
import com.epi.coreservice.request.comment.CommentUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final UserServiceClient userServiceclient;
    private final FileStorageClient fileStorageClient;
    private final ModelMapper modelMapper;

    public Comment createComment(CommentCreateRequest request, MultipartFile file) {
        String userId = getUserById(request.getUserId()).getId();
        Product product = productService.getProductById(request.getProductId());

        String imageId = null;

        if (file != null)
            imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();

        Comment toSave = Comment.builder()
                .userId(userId)
                .product(product)
                .name(request.getName())
                .description(request.getDescription())
                .imageId(imageId)
                .build();
        return commentRepository.save(toSave);
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(String id) {
        return findCommentById(id);
    }

    public List<Comment> getCommentsByUserId(String id) {
        String userId = getUserById(id).getId();
        return commentRepository.getCommentsByUserId(userId);
    }

    public UserDto getUserById(String id) {
        return Optional.ofNullable(userServiceclient.getUserById(id).getBody())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public Comment updateCommentById(CommentUpdateRequest request, MultipartFile file) {
        Comment toUpdate = findCommentById(request.getId());
        modelMapper.map(request, toUpdate);

        if (file != null) {
            String imageId = fileStorageClient.uploadImageToFIleSystem(file).getBody();
            if (imageId != null) {
                fileStorageClient.deleteImageFromFileSystem(toUpdate.getImageId());
                toUpdate.setImageId(imageId);
            }
        }

        return commentRepository.save(toUpdate);
    }

    public void deleteCommentById(String id) {
        commentRepository.deleteById(id);
    }

    public boolean authorizeCheck(String id, String principal) {
        return getUserById(getCommentById(id).getUserId()).getUsername().equals(principal);
    }

    protected Comment findCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }
}
