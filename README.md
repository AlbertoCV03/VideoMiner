# VideoMiner API

REST API for retrieving, consulting and storing channel data.

## General Information

* **OpenAPI Version:** `3.1.0`
* **API Version:** `v1`
* **Base URL:** `http://localhost:8080`
---

# Table of Contents

* [Features](#features)
* [Data Models](#data-models)
* [Channel Endpoints](#channel-endpoints)
* [Video Endpoints](#video-endpoints)
* [Comment Endpoints](#comment-endpoints)
* [Caption Endpoints](#caption-endpoints)
* [Pagination and Sorting](#pagination-and-sorting)
* [Example Request](#example-request)
* [Example Response](#example-response)

---

# Features

* Retrieve channels, videos, comments and captions
* Pagination support
* Sorting support
* Filter channels by minimum number of videos
* Filter videos by date
* CRUD operations for channels, videos, comments and captions
* Nested resources support:

    * Videos by channel
    * Comments by video
    * Captions by video

---

# Data Models

## Channel

```json
{
  "id": "80",
  "name": "tv",
  "description": "random videos channel",
  "createdTime": "2023-01-01T23:36:17.306Z",
  "videos": []
}
```

| Field       | Type         | Required |
| ----------- | ------------ | -------- |
| id          | string       | ❌        |
| name        | string       | ✅        |
| description | string       | ❌        |
| createdTime | string       | ✅        |
| videos      | array(Video) | ✅        |

---

## Video

```json
{
  "id": "21856",
  "name": "Alien: Isolation live on iOS",
  "description": "HOW WILL YOU SURVIVE?",
  "releaseTime": "2023-01-09T22:32:41.126Z",
  "user": {},
  "comments": [],
  "captions": []
}
```

| Field       | Type           | Required |
| ----------- | -------------- | -------- |
| id          | string         | ❌        |
| name        | string         | ✅        |
| description | string         | ❌        |
| releaseTime | string         | ✅        |
| user        | User           | ❌        |
| comments    | array(Comment) | ❌        |
| captions    | array(Caption) | ❌        |

---

## Comment

```json
{
  "id": "3955",
  "text": "Funniest video I've ever seen",
  "createdOn": "2023-01-03T23:45:40.196Z"
}
```

| Field     | Type   | Required |
| --------- | ------ | -------- |
| id        | string | ❌        |
| text      | string | ❌        |
| createdOn | string | ❌        |

---

## Caption

```json
{
  "id": "1",
  "link": "/lazy-static/video-captions/file.vtt",
  "language": "Spanish"
}
```

| Field    | Type   | Required |
| -------- | ------ | -------- |
| id       | string | ❌        |
| link     | string | ❌        |
| language | string | ❌        |

---

## User

```json
{
  "id": 1,
  "name": "stux",
  "user_link": "https://peertube.tv/accounts/stux",
  "picture_link": "https://peertube.tv/lazy-static/avatar.png"
}
```

| Field        | Type    | Required |
| ------------ | ------- |-------- |
| id           | integer |❌        |
| name         | string  |❌        |
| user_link    | string  |❌        |
| picture_link | string  |❌        |

---

# Channel Endpoints

## Get all channels

```http
GET /videominer/channels
```

### Query Parameters

| Parameter | Type    | Description                                                      | Default |
| --------- | ------- | ---------------------------------------------------------------- | ------- |
| page      | integer | Page number                                                      | 0       |
| size      | integer | Page size                                                        | 100     |
| minVideos | integer | Minimum number of videos                                         | 0       |
| order     | string  | Sort by `id`, `name` or `createdTime` (`-` for descending order) | -       |

### Example

```http
GET /videominer/channels?page=0&size=10&minVideos=5&order=-createdTime
```

---

## Get a channel

```http
GET /videominer/channels/{id}
```

### Path Parameters

| Parameter | Type   | Description        |
| --------- | ------ | ------------------ |
| id        | string | Channel identifier |

---

## Save a channel

```http
POST /videominer/channels
```

### Request Body

```json
{
  "name": "tv",
  "description": "random videos channel",
  "createdTime": "2023-01-01T23:36:17.306Z",
  "videos": []
}
```

### Responses

| Status | Description                                           |
| ------ | ----------------------------------------------------- |
| 201    | Channel successfully saved                            |
| 500    | Internal Server Error caused by incorrect body format |

---

## Save multiple channels

```http
POST /videominer/channels/all
```

### Request Body

```json
[
  {
    "name": "science",
    "createdTime": "2024-01-01T10:00:00.000Z",
    "videos": []
  }
]
```

---

## Update a channel

```http
PUT /videominer/channels/{id}
```

---

## Delete a channel

```http
DELETE /videominer/channels/{id}
```

---

# Video Endpoints

## Get all videos

```http
GET /videominer/videos
```

### Query Parameters

| Parameter | Type    |
| --------- | ------- |
| page      | integer |
| size      | integer |
| findDate  | string  |
| year      | integer |
| month     | integer |
| day       | integer |

### Example

```http
GET /videominer/videos?year=2023&month=1&day=9
```

---

## Get a video

```http
GET /videominer/videos/{videoId}
```

---

## Get all videos from a channel

```http
GET /videominer/channels/{channelId}/videos
```

---

## Update a video

```http
PUT /videominer/videos/{id}
```

---

## Delete a video

```http
DELETE /videominer/videos/{id}
```

---

# Comment Endpoints

## Get all comments

```http
GET /videominer/comments
```

### Query Parameters

| Parameter | Type    |
| --------- | ------- |
| page      | integer |
| size      | integer |
| order     | string  |

---

## Get a comment

```http
GET /videominer/comments/{commentId}
```

---

## Get all comments from a video

```http
GET /videominer/videos/{videoId}/comments
```

---

## Update a comment

```http
PUT /videominer/comments/{id}
```

---

## Delete a comment

```http
DELETE /videominer/comments/{id}
```

---

# Caption Endpoints

## Get all captions

```http
GET /videominer/captions
```

### Query Parameters

| Parameter | Type    |
| --------- | ------- |
| page      | integer |
| size      | integer |
| order     | string  |

---

## Get a caption

```http
GET /videominer/captions/{captionId}
```

---

## Get all captions from a video

```http
GET /videominer/videos/{videoId}/captions
```

---

## Update a caption

```http
PUT /videominer/captions/{id}
```

---

## Delete a caption

```http
DELETE /videominer/captions/{id}
```

---

# Pagination and Sorting

Several endpoints support pagination and sorting.

## Pagination Example

```http
GET /videominer/channels?page=1&size=20
```

## Descending Sort Example

```http
GET /videominer/channels?order=-createdTime
```

## Ascending Sort Example

```http
GET /videominer/channels?order=name
```

---

# Example Request

```bash
curl -X GET "http://localhost:8080/videominer/channels?page=0&size=5"
```

---

# Example Response

```json
[
  {
    "id": "80",
    "name": "tv",
    "description": "random videos channel",
    "createdTime": "2023-01-01T23:36:17.306Z",
    "videos": []
  }
]
```

---

# Status Codes

| Code | Meaning               |
| ---- | --------------------- |
| 200  | OK                    |
| 201  | Created               |
| 204  | No Content            |
| 500  | Internal Server Error |

---

# Technologies

* Java
* Spring Boot
* Maven
* OpenAPI / Swagger


