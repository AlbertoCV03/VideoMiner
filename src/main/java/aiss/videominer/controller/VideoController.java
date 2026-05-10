package aiss.videominer.controller;

import aiss.videominer.exception.ResourceNotFoundException;
import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
    public List<Video> getAllVideos(
            @Parameter(description = "Select the page to be retrieved")@RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Select the size of each retrieved page")@RequestParam(required = false, defaultValue = "100") Integer size,
            @Parameter(description = "Selects the date filtering mode applied to the video search. Supported values are: 'after', 'exact', and 'before'. This parameter requires at least a 'year' value; otherwise, the filter will be ignored because there is no reference date. If a date filter is applied without specifying this parameter, the default mode will be 'exact'")@RequestParam(required = false) String findDate,
            @Parameter(description = "Reference year used to filter videos by date")@RequestParam(required = false) Integer year,
            @Parameter(description = "Reference month used to filter videos by date. This parameter must always be used together with 'year'")@RequestParam(required = false) Integer month,
            @Parameter(description = "Reference day used to filter videos by date. This parameter must always be used together with both 'year' and 'month'")@RequestParam(required = false) Integer day) {
        Pageable pageable=PageRequest.of(page,size);
        Page<Video> videoPage=videoRepository.findAll(pageable);
        List<Video> paginados=videoPage.getContent();

        if(findDate==null && year==null) {
            return paginados;
        } else if(year!=null){
            List<Video> res=new ArrayList<>();
            LocalDate targetDate =
                    LocalDate.of(
                            year,
                            month==null?1:month,
                            day==null?1:day
                    );
            for(Video video: paginados) {
                Instant instant=Instant.parse(video.getReleaseTime());
                LocalDate date=instant.atZone(ZoneOffset.UTC).toLocalDate();
                if(findDate==null || findDate.equals("exact")) {
                    if (date.getYear()==year && month==null && day==null) {
                        res.add(video);
                    } else if (date.getYear()==year && month!=null && date.getMonthValue()==month && day==null){
                        res.add(video);
                    } else if(date.equals(targetDate)) {
                        res.add(video);
                    }
                } else if(findDate.equals("before")) {
                    if (date.getYear()<year && month==null && day==null) {
                        res.add(video);
                    } else if (date.getYear()<year && month!=null && date.getMonthValue()<month && day==null){
                        res.add(video);
                    } else if(date.isBefore(targetDate)) {
                        res.add(video);
                    }
                } else if(findDate.equals("after")) {
                    if (date.getYear()>year && month==null && day==null) {
                        res.add(video);
                    } else if (date.getYear()>year && month!=null && date.getMonthValue()>month && day==null){
                        res.add(video);
                    } else if(date.isAfter(targetDate) || date.isEqual(targetDate)) {
                        res.add(video);
                    }
                }
            }
            return res;
        } else {
            return paginados;
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
