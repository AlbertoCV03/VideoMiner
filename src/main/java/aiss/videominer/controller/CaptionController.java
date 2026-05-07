package aiss.videominer.controller;

import aiss.videominer.exception.ResourceNotFoundException;
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
    public List<Caption> getAllCaptionsByVideoId(@PathVariable String videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video", videoId));
        return new ArrayList<>(video.getCaptions());
    }

    @GetMapping("/captions")
    public List<Caption> getAllCaptions() {
        return captionRepository.findAll();
    }

    @GetMapping("/captions/{captionId}")
    public Caption getCaption(@PathVariable String captionId) {
        return captionRepository.findById(captionId)
                .orElseThrow(() -> new ResourceNotFoundException("Caption", captionId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/captions/{id}")
    public void update(@Valid @RequestBody Caption updatedCaption, @PathVariable String id) {
        Caption caption = captionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caption", id));

        caption.setLink(updatedCaption.getLink());
        caption.setLanguage(updatedCaption.getLanguage());

        captionRepository.save(caption);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("captions/{id}")
    public void delete(@PathVariable String id) {
        if (captionRepository.existsById(id)) {
            captionRepository.deleteById(id);
        }
    }

}
