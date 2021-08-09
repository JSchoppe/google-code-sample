package com.google;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/** A class used to represent a video. */
class Video {

  // Video attributes
  private final String title;
  private final String videoId;
  private final List<String> tags;

  Video(String title, String videoId, List<String> tags)
  {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle()
  {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId()
  {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags()
  {
    return tags;
  }

  /** Returns a string containing all meta-data of the video. */
  String getAllDetails()
  {
    // Automate the assembly of this format.
    return String.format("%s (%s) [%s]",
            getTitle(),
            getVideoId(),
            getTags().stream().collect(
                    Collectors.joining(" ", "", "")));
  }
}
