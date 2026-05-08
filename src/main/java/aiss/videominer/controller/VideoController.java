package aiss.videominer.controller;

import aiss.videominer.exception.ResourceNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
@Tag(name = "Video",description = "Video operations")
@RestController
@RequestMapping("/videominer")
public class VideoController {
    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    VideoRepository videoRepository;

    @Operation(
            summary = "Get all videos from a channel",
            description = "Get all the videos from a specified channel")
    @GetMapping("/channels/{channelId}/videos")
    public List<Video> getAllVideosByChannelId(@PathVariable String channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", channelId));
        return new ArrayList<>(channel.getVideos());
    }

    @Operation(
            summary = "Get all videos",
            description = "Get all the videos from the database")
    @GetMapping("/videos")
    public List<Video> getAllVideos(@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer size) {
        if(page==null && size==null){
            return videoRepository.findAll();
        }else if(page==null){
            Pageable pageable= PageRequest.ofSize(size);
            Page<Video> pageVideo =videoRepository.findAll(pageable);

            return pageVideo.getContent();
        }else if(size==null){
            size=10;
            Pageable pageable= PageRequest.of(page,size);
            Page<Video> pageVideo =videoRepository.findAll(pageable);

            return pageVideo.getContent();
        }else{
            Pageable pageable= PageRequest.of(page,size);
            Page<Video> pageVideo =videoRepository.findAll(pageable);

            return pageVideo.getContent();
        }
    }

    @Operation(
            summary = "Get a video",
            description = "Get the specified video from the database")
    @GetMapping("/videos/{videoId}")
    public Video getVideo(@PathVariable String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video", videoId));
    }

    @Operation(
            summary = "Update a video",
            description = "Update the specified video from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/videos/{id}")
    public void update(@Valid @RequestBody Video updatedVideo, @PathVariable String id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException(id));

        video.setName(updatedVideo.getName());
        video.setDescription(updatedVideo.getDescription());
        video.setReleaseTime(updatedVideo.getReleaseTime());
        video.setAuthor(updatedVideo.getAuthor());
        video.setCaptions(updatedVideo.getCaptions());
        video.setComments(updatedVideo.getComments());

        videoRepository.save(video);
    }

    @Operation(
            summary = "Delete a video",
            description = "Delete the specified video from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/videos/{id}")
    public void delete(@PathVariable String id) {
        if (videoRepository.existsById(id)) {
            videoRepository.deleteById(id);
        }
    }

}
