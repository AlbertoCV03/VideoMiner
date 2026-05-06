package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.VideoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class CaptionController {
    @autowired
    VideoRepository videoRepository;

    @autowired
    CaptionRepository captionRepository;

    @GetMapping("/videos/{videoId}/captions")
    public List<Caption> getAllCaptions(@PathVariable long videoId){
        Optional<Video> video = videoRepository.findById(videoId);
        return new ArrayList<>(video.get().getCaptions());
    }

    @GetMapping("/captions/{captionId}")
    public Caption getCaption(@PathVariable long captionId){
        Optional<Caption> caption = captionRepository.findById(captionId);
        return caption.get();
    }

}
