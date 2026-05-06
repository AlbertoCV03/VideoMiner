package aiss.videominer.controller;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
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
public class CaptionController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CaptionRepository captionRepository;

    @Autowired
    VideoController videoController;

    @GetMapping("/videos/{videoId}/captions")
    public List<Caption> getAllCaptionsByVideoId(@PathVariable String videoId){
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/captions/{id}")
    public void update(@Valid @RequestBody Caption updatedCaption, @PathVariable long id){
        Optional<Caption> caption_data = captionRepository.findById(id);
        Caption caption = caption_data.get();

        caption.setLink(updatedCaption.getLink());
        caption.setLanguage(updatedCaption.getLanguage());

        captionRepository.save(caption);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("captions/{id}")
    public void delete(@PathVariable long id){
        if(captionRepository.existsById(id)){
            captionRepository.deleteById(id);
        }
    }

}
