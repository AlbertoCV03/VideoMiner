package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class CommentController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/videos/{videoId}/comments")
    public List<Comment> getAllComments(@PathVariable long videoId){
        Optional<Video> video = videoRepository.findById(videoId);
        return new ArrayList<>(video.get().getComments());
    }

    @GetMapping("/comments/{commentId}")
    public Comment getComment(@PathVariable long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.get();
    }

}
