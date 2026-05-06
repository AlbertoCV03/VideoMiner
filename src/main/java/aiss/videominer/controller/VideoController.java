package aiss.videominer.controller;

import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class VideoController {
    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    VideoRepository videoRepository;

    @GetMapping("/channels/{channelId}/videos")
    public List<Video> getAllVideosByChannelId(@PathVariable String channelId){
        Optional<Channel> channel = channelRepository.findById(channelId);
        return new ArrayList<>(channel.get().getVideos());
    }

    @GetMapping("/videos")
    public List<Video> getAllVideos(){
        List<Channel> channels= channelRepository.findAll();
        List<Video> videos = new ArrayList<>();

        for(Channel channel: channels){
            videos.addAll(channel.getVideos());
        }
        return videos;
    }

    @GetMapping("/videos/{videoId}")
    public Video getVideo(@PathVariable long videoId){
        Optional<Video> video = videoRepository.findById(videoId);
        return video.get();
    }

}
