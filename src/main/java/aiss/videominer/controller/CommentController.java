package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class CommentController {
    @autowired
    VideoRepository videoRepository;

    @autowired
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
