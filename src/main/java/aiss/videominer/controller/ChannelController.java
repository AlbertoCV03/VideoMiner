package aiss.videominer.controller;


import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    @Autowired
    ChannelRepository channelRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addChannel(@RequestBody Channel channel) {
        channelRepository.save(channel);
    }
}
