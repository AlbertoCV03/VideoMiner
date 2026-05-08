package aiss.videominer.controller;

import aiss.videominer.exception.ResourceNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Channel",description = "Channel operations")
@RestController
@RequestMapping("/videominer/channels")
public class ChannelController {
    @Autowired
    ChannelRepository channelRepository;

    @Operation(
            summary = "Save a channel",
            description = "Save a channel object into the database")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Channel successfully saved",
                    content = { @Content(schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json") })})
    public Channel addChannel(@RequestBody Channel channel) {
        return channelRepository.save(channel);
    }

    @Operation(
            summary = "Get all channels",
            description = "Get all the channels from the database")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Channel> getAllChannels(@RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer size,
                                        @RequestParam(required = false) Integer minVideos) {
        if(page==null && size==null){
            if(minVideos!=null){
                size=Integer.MAX_VALUE;
                Pageable pageable= PageRequest.ofSize(size);
                Page<Channel> pageChannel= channelRepository.findByVideoCountGreaterThan(minVideos,pageable);
                return pageChannel.getContent();
            }
            return channelRepository.findAll();
        }else if(page==null){
            Pageable pageable= PageRequest.ofSize(size);
            if(minVideos!=null){
                Page<Channel> pageChannel =channelRepository.findByVideoCountGreaterThan(minVideos,pageable);
                return pageChannel.getContent();
            }
            Page<Channel> pageChannel =channelRepository.findAll(pageable);

            return pageChannel.getContent();
        }else if(size==null){
            size=10;
            Pageable pageable= PageRequest.of(page,size);
            if(minVideos!=null){
                Page<Channel> pageChannel =channelRepository.findByVideoCountGreaterThan(minVideos,pageable);
                return pageChannel.getContent();
            }
            Page<Channel> pageChannel =channelRepository.findAll(pageable);
            return pageChannel.getContent();
        }else{
            Pageable pageable= PageRequest.of(page,size);
            if(minVideos!=null){
                Page<Channel> pageChannel =channelRepository.findByVideoCountGreaterThan(minVideos,pageable);
                return pageChannel.getContent();
            }
            Page<Channel> pageChannel =channelRepository.findAll(pageable);

            return pageChannel.getContent();
        }

    }

    @Operation(
            summary = "Get a channel",
            description = "Get the specified channel from the database")
    @GetMapping("/{id}")
    public Channel getChannel(@PathVariable String id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", id));
    }

    @Operation(
            summary = "Update a channel",
            description = "Update the specified channel from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody Channel updatedChannel, @PathVariable String id){
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", id));

        channel.setDescription(updatedChannel.getDescription());
        channel.setName(updatedChannel.getName());
        channel.setCreatedTime(updatedChannel.getCreatedTime());
        channel.setVideos(updatedChannel.getVideos());

        channelRepository.save(channel);
    }

    @Operation(
            summary = "Delete a channel",
            description = "Delete the specified channel from the database")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if (channelRepository.existsById(id)) {
            channelRepository.deleteById(id);
        }
    }
}
