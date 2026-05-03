package aiss.videominer.controller;


import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    @Autowired
    ChannelRepository channelRepository;

    @PostMapping
    public void addChannel(@RequestBody Channel channel) {
        channelRepository.save(channel);
    }
}
