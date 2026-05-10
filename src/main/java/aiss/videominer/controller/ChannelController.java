package aiss.videominer.controller;

import aiss.videominer.exception.ResourceNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.ChannelRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Tag(name = "Channel",description = "Channel operations")
@Validated
@RestController
@RequestMapping("/videominer/channels")
public class ChannelController {
    @Autowired
    ChannelRepository channelRepository;

    @Operation(
            summary = "Store a channel",
            description = "Store a channel into the database following the data model")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Channel successfully stored",
                    content = { @Content(
                            schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name="Body example",
                                    summary = "Successful response",
                                    description = "This example provides the representation of a valid body",
                                    value = """
                                            {
                                                "id": "80",
                                                "name": "tv",
                                                "description": "canal de videos random",
                                                "createdTime": "2023-01-01T23:36:17.306Z",
                                                "videos": [
                                                    {
                                                        "id": "21856",
                                                        "name": "Alien: Isolation live on iOS - 1/9/2023, 10:17:06 PM",
                                                        "description": "HOW WILL YOU SURVIVE? Discover the true meaning of fear in Alien: Isolation, a survival horror set in an atmosphere of constant dread and mortal danger.",
                                                        "releaseTime": "2023-01-09T22:32:41.126Z",
                                                        "user": {
                                                            "id": 1,
                                                            "name": "stux",
                                                            "user_link": "https://peertube.tv/accounts/stux",
                                                            "picture_link": "https://peertube.tv/lazy-static/avatars/f8463b3f-2e2e-4f36-8bd3-fa9c0ef4f463.png"
                                                        },
                                                        "comments": [
                                                            {
                                                                "id": "3955",
                                                                "text": "Funniest video I've ever seen",
                                                                "createdOn": "2023-01-03T23:45:40.196Z"
                                                            }
                                                        ],
                                                        "captions": [
                                                            {
                                                                "id": "1",
                                                                "link": "/lazy-static/video-captions/68efe0a2-8ed8-4a6d-831a-ab8f5fc8f7fc-cy.vtt",
                                                                "language": "Spanish"
                                                            }
                                                        ]
                                                    },
                                                    {
                                                        "id": "21855",
                                                        "name": "Alien: Isolation live on iOS - 1/9/2023, 9:51:45 PM",
                                                        "description": "HOW WILL YOU SURVIVE? Discover the true meaning of fear in Alien: Isolation, a survival horror set in an atmosphere of constant dread and mortal danger.",
                                                        "releaseTime": "2023-01-09T22:21:23.816Z",
                                                        "user": {
                                                            "id": 2,
                                                            "name": "stux",
                                                            "user_link": "https://peertube.tv/accounts/stux",
                                                            "picture_link": "https://peertube.tv/lazy-static/avatars/f8463b3f-2e2e-4f36-8bd3-fa9c0ef4f463.png"
                                                        },
                                                        "comments": [],
                                                        "captions": [
                                                            {
                                                                "id": "2",
                                                                "link": "/lazy-static/video-captions/b62fe117-7ae9-4214-975c-33558d1d71eb-cy.vtt",
                                                                "language": "Welsh"
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                            """
                            ))}),
            @ApiResponse(
                    responseCode = "500",
                    description="❌ **Internal Server Error. Caused by invalid body format**",
                    content = { @Content(schema = @Schema()) })
    })
    public Channel addChannel(@Valid@RequestBody Channel channel) {
        return channelRepository.save(channel);
    }

    @Operation(
            summary = "Get all channels",
            description = "Retrieves all the channels from the database. By default it retrieves 100 elements, however, additional parameters are provided in the examples below")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    //Due to pagination being done first, the sorting and filtering will only be correct at a page level
    //Values like id or createdTime being string means the Sort.By will sort them as such. Changing them to Integer and DateTime will make sorting easier.
    //To properly sort based on the expected type each name suggest it has to be done like this (It's only going to be done here due to time constrains):
    public List<Channel> getAllChannels(
            @Parameter(description = "Select the page to be retrieved")@Min(0)@RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Select the size of each retrieved page")@Min(1)@RequestParam(defaultValue = "100") Integer size,
            @Parameter(description = "Minimum number of videos a channel must have to be retrieved")@Min(0)@RequestParam(defaultValue = "0") Integer minVideos,
            @Parameter(description = "Sort the results either by id,name or createdTime. Use '-' before the property name to sort in descending order")@RequestParam(required = false) String order) {
        Pageable pageable=PageRequest.of(page,size);
        Page<Channel> channelPage=channelRepository.findByVideoCountGreaterThan(minVideos,pageable);

        List<Channel> channels = new ArrayList<>(channelPage.getContent());
        if (order != null) {
            boolean descending = order.startsWith("-");
            Comparator<Channel> comparator = getChannelComparator(order, descending);

            if (comparator != null) {
                if (descending) {
                    comparator = comparator.reversed();
                }

                channels.sort(comparator);
            }
        }

        return channels;

    }

    private static @Nullable Comparator<Channel> getChannelComparator(String order, boolean descending) {
        String field = descending ? order.substring(1) : order;
        Comparator<Channel> comparator = switch (field) {
            case "id" -> Comparator.comparing(
                    c -> Integer.parseInt(c.getId())
            );
            case "createdTime" -> Comparator.comparing(
                    c -> java.time.Instant.parse(c.getCreatedTime())
            );
            case "name" -> Comparator.comparing(Channel::getName);
            default -> null;
        };
        return comparator;
    }

    @Operation(
            summary = "Get a channel",
            description = "Retrieves the specified channel from the database")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Channel successfully retrieved",
                    content = { @Content(
                            schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name="Body example",
                                    summary = "Successful response",
                                    description = "This example provides the representation of a expected response",
                                    value = """
                                            {
                                                "id": "80",
                                                "name": "tv",
                                                "description": "canal de videos random",
                                                "createdTime": "2023-01-01T23:36:17.306Z",
                                                "videos": [
                                                    {
                                                        "id": "21856",
                                                        "name": "Alien: Isolation live on iOS - 1/9/2023, 10:17:06 PM",
                                                        "description": "HOW WILL YOU SURVIVE? Discover the true meaning of fear in Alien: Isolation, a survival horror set in an atmosphere of constant dread and mortal danger.",
                                                        "releaseTime": "2023-01-09T22:32:41.126Z",
                                                        "user": {
                                                            "id": 1,
                                                            "name": "stux",
                                                            "user_link": "https://peertube.tv/accounts/stux",
                                                            "picture_link": "https://peertube.tv/lazy-static/avatars/f8463b3f-2e2e-4f36-8bd3-fa9c0ef4f463.png"
                                                        },
                                                        "comments": [
                                                            {
                                                                "id": "3955",
                                                                "text": "Funniest video I've ever seen",
                                                                "createdOn": "2023-01-03T23:45:40.196Z"
                                                            }
                                                        ],
                                                        "captions": [
                                                            {
                                                                "id": "1",
                                                                "link": "/lazy-static/video-captions/68efe0a2-8ed8-4a6d-831a-ab8f5fc8f7fc-cy.vtt",
                                                                "language": "Spanish"
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                            """
                            ))}),
            @ApiResponse(
                    responseCode = "404",
                    description="❌ **Channel not found**",
                    content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public Channel getChannel(
            @Parameter(description = "Select the id from the channel to be retrieved")@PathVariable String id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Channel", id));
    }

    @Operation(
            summary = "Update a channel",
            description = "Update the specified channel from the database")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204 No Content",
                    description = "Channel successfully updated"
                    ),
            @ApiResponse(
                    responseCode = "404",
                    description="❌ **Channel not found**",
                    content = { @Content(schema = @Schema()) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(
            @Valid @RequestBody Channel updatedChannel,
            @Parameter(description = "The id of the channel to be updated")@PathVariable String id){
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204 No Content",
                    description = "Channel successfully deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description="❌ **Channel not found**",
                    content = { @Content(schema = @Schema()) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "The id of the channel to be deleted")@PathVariable String id){
        if (channelRepository.existsById(id)) {
            channelRepository.deleteById(id);
        }
    }

    @Operation(
            summary = "Store multiple channels",
            description = "Stores multiple channels at once in the database")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Channels successfully stored",
                    content = { @Content(
                            schema = @Schema(implementation = Channel.class),
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name="Body example",
                                    summary = "Successful response",
                                    description = "This example provides the representation of a valid body",
                                    value = """
                                            [
                                            {
                                                "id": "80",
                                                "name": "tv",
                                                "description": "canal de videos random",
                                                "createdTime": "2023-01-01T23:36:17.306Z",
                                                "videos": [
                                                    {
                                                        "id": "21856",
                                                        "name": "Alien: Isolation live on iOS - 1/9/2023, 10:17:06 PM",
                                                        "description": "HOW WILL YOU SURVIVE? Discover the true meaning of fear in Alien: Isolation, a survival horror set in an atmosphere of constant dread and mortal danger.",
                                                        "releaseTime": "2023-01-09T22:32:41.126Z",
                                                        "user": {
                                                            "id": 1,
                                                            "name": "stux",
                                                            "user_link": "https://peertube.tv/accounts/stux",
                                                            "picture_link": "https://peertube.tv/lazy-static/avatars/f8463b3f-2e2e-4f36-8bd3-fa9c0ef4f463.png"
                                                        },
                                                        "comments": [
                                                            {
                                                                "id": "3955",
                                                                "text": "Funniest video I've ever seen",
                                                                "createdOn": "2023-01-03T23:45:40.196Z"
                                                            }
                                                        ],
                                                        "captions": [
                                                            {
                                                                "id": "1",
                                                                "link": "/lazy-static/video-captions/68efe0a2-8ed8-4a6d-831a-ab8f5fc8f7fc-cy.vtt",
                                                                "language": "Spanish"
                                                            }
                                                        ]
                                                    },
                                                    {
                                                        "id": "21855",
                                                        "name": "Alien: Isolation live on iOS - 1/9/2023, 9:51:45 PM",
                                                        "description": "HOW WILL YOU SURVIVE? Discover the true meaning of fear in Alien: Isolation, a survival horror set in an atmosphere of constant dread and mortal danger.",
                                                        "releaseTime": "2023-01-09T22:21:23.816Z",
                                                        "user": {
                                                            "id": 2,
                                                            "name": "stux",
                                                            "user_link": "https://peertube.tv/accounts/stux",
                                                            "picture_link": "https://peertube.tv/lazy-static/avatars/f8463b3f-2e2e-4f36-8bd3-fa9c0ef4f463.png"
                                                        },
                                                        "comments": [],
                                                        "captions": [
                                                            {
                                                                "id": "2",
                                                                "link": "/lazy-static/video-captions/b62fe117-7ae9-4214-975c-33558d1d71eb-cy.vtt",
                                                                "language": "Welsh"
                                                            }
                                                        ]
                                                    }
                                                ]
                                            },
                                            {
                                              "id": "81",
                                              "name": "gaming",
                                              "description": "clips de videojuegos",
                                              "createdTime": "2023-02-01T09:00:00.000Z",
                                              "videos": [
                                                {
                                                  "id": "31001",
                                                  "name": "Speedrun Fail Compilation",
                                                  "description": "Los mejores fails de speedrun del mes.",
                                                  "releaseTime": "2026-02-03T14:00:00.000Z",
                                                  "comments": [
                                                    {
                                                      "id": "5101",
                                                      "text": "Me he reído muchísimo",
                                                      "createdOn": "2026-05-03T15:00:00.000Z"
                                                    }
                                                  ],
                                                  "captions": [
                                                    {
                                                      "id": "1",
                                                      "link": "/lazy-static/video-captions/speedrun-es.vtt",
                                                      "language": "Spanish"
                                                    }
                                                  ],
                                                  "user": {
                                                    "name": "gamerpro",
                                                    "user_link": "https://peertube.tv/accounts/gamerpro",
                                                    "picture_link": "https://peertube.tv/lazy-static/avatars/gamer.png"
                                                  }
                                                }
                                              ]
                                            }
                                            ]
                                            """
                            ))}),
            @ApiResponse(
                    responseCode = "500",
                    description="❌ **Internal Server Error. Caused by invalid body format**",
                    content = { @Content(schema = @Schema()) })
    })
    @PostMapping("/all")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Channel> addAllChannels(@Valid@RequestBody List<Channel> channels) {
        return channelRepository.saveAll(channels);
    }
}
