package aiss.videominer.controller;


import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
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
    public Channel getChannel(@PathVariable long id) {
        Optional<Channel> channel = channelRepository.findById(id);
        return channel.get();
    }
}
