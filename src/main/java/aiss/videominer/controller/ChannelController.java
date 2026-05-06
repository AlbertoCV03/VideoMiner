package aiss.videominer.controller;


import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer/channels")
public class ChannelController {
    @Autowired
    ChannelRepository channelRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Channel addChannel(@RequestBody Channel channel) {
        return channelRepository.save(channel);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Channel> getAllChannels(){
        return channelRepository.findAll();
    }

    @GetMapping("/{id}")
    public Channel getChannel(@PathVariable String id) {
        Optional<Channel> channel = channelRepository.findById(id);
        return channel.get();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Channel updatedChannel, @PathVariable long id){
        Optional<Channel> channel_data = channelRepository.findById(id);
        Channel channel = channel_data.get();

        channel.setDescription(updatedChannel.getDescription());
        channel.setName(updatedChannel.getName());
        channel.setCreatedTime(updatedChannel.getCreatedTime());
        channel.setVideos(updatedChannel.getVideos());

        channelRepository.save(channel);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        if(channelRepository.existsById(id)){
            channelRepository.deleteById(id);
        }
    }
}
