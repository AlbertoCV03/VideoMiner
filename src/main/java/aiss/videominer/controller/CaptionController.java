package aiss.videominer.controller;

import aiss.videominer.exception.ResourceNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Tag(name = "Caption",description = "Caption operations")
@RestController
@RequestMapping("/videominer")
public class CaptionController {
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    CaptionRepository captionRepository;

    @Autowired
    VideoController videoController;

    @Operation(
            summary = "Gets all captions from a video",
            description = "Gets all the captions from a specified video")
    @GetMapping("/videos/{videoId}/captions")
    public List<Caption> getAllCaptionsByVideoId(@PathVariable String videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video", videoId));
        return new ArrayList<>(video.getCaptions());
    }

    @Operation(
            summary = "Get all captions",
            description = "Gets all the captions from the database")
    @GetMapping("/captions")
    public List<Caption> getAllCaptions() {
        List<Video> videos = videoController.getAllVideos();
        List<Caption> captions = new ArrayList<>();

        for (Video video : videos) {
            captions.addAll(video.getCaptions());
        }
        return captions;
    }

    @Operation(
            summary = "Get a caption",
            description = "Gets the specified caption from the database")
    @GetMapping("/captions/{captionId}")
    public Caption getCaption(@PathVariable String captionId) {
        return captionRepository.findById(captionId)
                .orElseThrow(() -> new ResourceNotFoundException("Caption", captionId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Update a caption",
            description = "Updates the specified caption from the database")
    @PutMapping("/captions/{id}")
    public void update(@Valid @RequestBody Caption updatedCaption, @PathVariable String id) {
        Caption caption = captionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Caption", id));

        caption.setLink(updatedCaption.getLink());
        caption.setLanguage(updatedCaption.getLanguage());

        captionRepository.save(caption);
    }

    @Operation(
            summary = "Delete a caption",
            description = "Deletes the specified caption from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("captions/{id}")
    public void delete(@PathVariable String id) {
        if (captionRepository.existsById(id)) {
            captionRepository.deleteById(id);
        }
    }

}
