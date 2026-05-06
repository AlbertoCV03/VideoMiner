package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class CaptionController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CaptionRepository captionRepository;

    @Autowired
    VideoController videoController;

    @GetMapping("/videos/{videoId}/captions")
    public List<Caption> getAllCaptionsByVideoId(@PathVariable long videoId){
        Optional<Video> video = videoRepository.findById(videoId);
        return new ArrayList<>(video.get().getCaptions());
    }

    @GetMapping("/captions")
    public List<Caption> getAllCaptions(){
        List<Video> videos = videoController.getAllVideos();
        List<Caption> captions = new ArrayList<>();

        for(Video video: videos){
            captions.addAll(video.getCaptions());
        }
        return captions;
    }

    @GetMapping("/captions/{captionId}")
    public Caption getCaption(@PathVariable long captionId){
        Optional<Caption> caption = captionRepository.findById(captionId);
        return caption.get();
    }

}
