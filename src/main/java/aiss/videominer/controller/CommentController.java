package aiss.videominer.controller;

import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public List<Comment> getAllCommentsByVideoId(@PathVariable String videoId){
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
    public Comment getComment(@PathVariable String commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        return comment.get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/comments/{id}")
    public void update(@Valid @RequestBody Comment updatedComment, @PathVariable String id){
        Optional<Comment> comment_data = commentRepository.findById(id);
        Comment comment = comment_data.get();

        comment.setText(updatedComment.getText());
        comment.setCreatedOn(updatedComment.getCreatedOn());

        commentRepository.save(comment);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("comments/{id}")
    public void delete(@PathVariable String id){
        if(commentRepository.existsById(id)){
            commentRepository.deleteById(id);
        }
    }

}
