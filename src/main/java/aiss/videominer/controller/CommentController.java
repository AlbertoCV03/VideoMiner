package aiss.videominer.controller;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.exception.ResourceNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Tag(name = "Comment",description = "Comment operations")
@RestController
@RequestMapping("/videominer")
public class CommentController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    VideoController videoController;

    @Operation(
            summary = "Gets all comments from a video",
            description = "Gets all the comments from a specified video")
    @GetMapping("/videos/{videoId}/comments")
    public List<Comment> getAllCommentsByVideoId(@PathVariable String videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException(videoId));
        return new ArrayList<>(video.getComments());
    }

    @Operation(
            summary = "Get all comments",
            description = "Gets all the comments from the database")
    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        List<Video> videos = videoController.getAllVideos();
        List<Comment> comments = new ArrayList<>();

        for (Video video : videos) {
            comments.addAll(video.getComments());
        }
        return comments;
    }

    @Operation(
            summary = "Get a comment",
            description = "Gets the specified comment from the database")
    @GetMapping("/comments/{commentId}")
    public Comment getComment(@PathVariable String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", commentId));
    }

    @Operation(
            summary = "Update a comment",
            description = "Updates the specified comment from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/comments/{id}")
    public void update(@Valid @RequestBody Comment updatedComment, @PathVariable String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", id));

        comment.setText(updatedComment.getText());
        comment.setCreatedOn(updatedComment.getCreatedOn());

        commentRepository.save(comment);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Deletes the specified comment from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("comments/{id}")
    public void delete(@PathVariable String id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        }
    }

}
