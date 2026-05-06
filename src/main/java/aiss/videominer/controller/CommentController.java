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

    @Autowired
    VideoController videoController;

    @GetMapping("/videos/{videoId}/comments")
    public List<Comment> getAllCommentsByVideoId(@PathVariable long videoId){
        Optional<Video> video = videoRepository.findById(videoId);
        return new ArrayList<>(video.get().getComments());
    }

    @GetMapping("/comments")
    public List<Comment> getAllComments(){
        List<Video> videos = videoController.getAllVideos();
        List<Comment> comments = new ArrayList<>();

        for(Video video: videos){
            comments.addAll(video.getComments());
        }
        return comments;
    }


    @GetMapping("/comments/{commentId}")
    public Comment getComment(@PathVariable long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.get();
    }

}
