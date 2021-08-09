package com.google;

import java.util.*;

public class VideoPlayer
{
  // Utilized Objects
  private final Random random;
  private final VideoLibrary videoLibrary;
  // Player State
  private final Map<String, VideoPlaylist> playlists;
  private final Map<String, String> flags;
  private Video selectedVideo;
  private boolean videoIsSelected;
  private boolean videoIsPlaying;

  public VideoPlayer()
  {
    this.random = new Random();
    this.videoLibrary = new VideoLibrary();
    this.playlists = new TreeMap<>();
    this.flags = new HashMap<>();
    this.videoIsSelected = false;
    this.videoIsPlaying = false;
  }

  /** Displays the total video count in the video library. */
  public void numberOfVideos()
  {
    System.out.printf("%s videos in the library%n",
            videoLibrary.getVideos().size());
  }

  /** Displays details for all videos in the video library. */
  public void showAllVideos()
  {
    final List<Video> videos = videoLibrary.getVideos();
    videos.sort((lhs, rhs) -> String.CASE_INSENSITIVE_ORDER.compare(
            lhs.getTitle(), rhs.getTitle()));
    System.out.println("Here's a list of all available videos:");
    for (Video video : videos)
    {
      String id = video.getVideoId();
      System.out.printf("%s%s%n", video.getAllDetails(),
              flags.containsKey(id) ? String.format(" - FLAGGED (reason: %s)", flags.get(id)) : "");
    }
  }

  /** Begins playing the requested video. */
  public void playVideo(String videoId)
  {
    final Video videoToPlay = videoLibrary.getVideo(videoId);
    if (videoToPlay == null)
      System.out.println("Cannot play video: Video does not exist");
    else if (flags.containsKey(videoId))
      System.out.printf("Cannot play video: Video is currently flagged (reason: %s)%n", flags.get(videoId));
    else
    {
      if (videoIsSelected)
        System.out.printf("Stopping video: %s%n", selectedVideo.getTitle());
      System.out.printf("Playing video: %s%n", videoToPlay.getTitle());
      videoIsSelected = true;
      videoIsPlaying = true;
      selectedVideo = videoToPlay;
    }
  }

  /** Stops the currently playing video. */
  public void stopVideo()
  {
    if (!videoIsSelected)
      System.out.println("Cannot stop video: No video is currently playing");
    else
    {
      System.out.printf("Stopping video: %s%n", selectedVideo.getTitle());
      videoIsSelected = false;
      videoIsPlaying = false;
    }
  }

  /** Plays a video randomly from the video library. */
  public void playRandomVideo()
  {
    final List<Video> videos = videoLibrary.getVideos();
    videos.removeIf((Video video) -> flags.containsKey(video.getVideoId()));
    if (videos.size() == 0)
      System.out.println("No videos available");
    else
      playVideo(videos.get(random.nextInt(videos.size())).getVideoId());
  }

  /** Pauses the current video. */
  public void pauseVideo()
  {
    if (!videoIsSelected)
      System.out.println("Cannot pause video: No video is currently playing");
    else if (!videoIsPlaying)
      System.out.printf("Video already paused: %s%n", selectedVideo.getTitle());
    else
    {
      System.out.printf("Pausing video: %s%n", selectedVideo.getTitle());
      videoIsPlaying = false;
    }
  }

  /** Continues the current video. */
  public void continueVideo()
  {
    if (!videoIsSelected)
      System.out.println("Cannot continue video: No video is currently playing");
    else if (videoIsPlaying)
      System.out.println("Cannot continue video: Video is not paused");
    else
    {
      System.out.printf("Continuing video: %s%n", selectedVideo.getTitle());
      videoIsPlaying = true;
    }
  }

  /** Displays details and pause state for the current playing video. */
  public void showPlaying()
  {
    if (!videoIsSelected)
      System.out.println("No video is currently playing");
    else
      System.out.printf("Currently playing: %s%s%n",
              selectedVideo.getAllDetails(),
              videoIsPlaying ? "" : " - PAUSED");
  }

  /** Creates a new playlist with the given name. */
  public void createPlaylist(String playlistName)
  {
    final String name = playlistName.toLowerCase(Locale.ROOT);
    if (playlists.containsKey(name))
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    else
    {
      System.out.printf("Successfully created new playlist: %s%n", playlistName);
      playlists.put(name, new VideoPlaylist(playlistName));
    }
  }

  /** Adds a video to the playlist. */
  public void addVideoToPlaylist(String playlistName, String videoId)
  {
    final VideoPlaylist foundPlaylist = playlists.get(
            playlistName.toLowerCase(Locale.ROOT));
    if (foundPlaylist == null)
      System.out.printf("Cannot add video to %s: Playlist does not exist%n", playlistName);
    else
    {
      final Video foundVideo = videoLibrary.getVideo(videoId);
      if (foundVideo == null)
        System.out.printf("Cannot add video to %s: Video does not exist%n", playlistName);
      else if (flags.containsKey(videoId))
        System.out.printf("Cannot add video to %s: Video is currently flagged (reason: %s)%n",
                playlistName, flags.get(videoId));
      else if (foundPlaylist.videoIDs.contains(videoId))
        System.out.printf("Cannot add video to %s: Video already added%n", playlistName);
      else
      {
        System.out.printf("Added video to %s: %s%n", playlistName, foundVideo.getTitle());
        foundPlaylist.videoIDs.add(videoId);
      }
    }
  }

  /** Shows all playlists currently in the player. */
  public void showAllPlaylists()
  {
    if (playlists.size() == 0)
      System.out.println("No playlists exist yet");
    else
    {
      System.out.println("Showing all playlists:");
      for (Map.Entry<String, VideoPlaylist> kvp : playlists.entrySet())
        System.out.println(kvp.getValue().name);
    }
  }

  /** Shows all the videos in a playlist. */
  public void showPlaylist(String playlistName)
  {
    final VideoPlaylist playlist = playlists.get(playlistName.toLowerCase(Locale.ROOT));
    if (playlist == null)
      System.out.printf("Cannot show playlist %s: Playlist does not exist%n", playlistName);
    else
    {
      System.out.printf("Showing playlist: %s%n", playlistName);
      if (playlist.videoIDs.size() == 0)
        System.out.println("No videos here yet");
      else
        for (String videoID : playlist.videoIDs)
          System.out.printf("%s%s%n", videoLibrary.getVideo(videoID).getAllDetails(),
                  flags.containsKey(videoID) ? String.format(" - FLAGGED (reason: %s)", flags.get(videoID)) : "");
    }
  }

  /** Removes a video from a playlist. */
  public void removeFromPlaylist(String playlistName, String videoId)
  {
    final VideoPlaylist playlist = playlists.get(playlistName.toLowerCase(Locale.ROOT));
    if (playlist == null)
      System.out.printf("Cannot remove video from %s: Playlist does not exist%n", playlistName);
    else
    {
      final Video video = videoLibrary.getVideo(videoId);
      if (video == null)
        System.out.printf("Cannot remove video from %s: Video does not exist%n", playlistName);
      else if (!playlist.videoIDs.contains(videoId))
        System.out.printf("Cannot remove video from %s: Video is not in playlist%n", playlistName);
      else
      {
        System.out.printf("Removed video from %s: %s%n", playlistName, video.getTitle());
        playlist.videoIDs.remove(videoId);
      }
    }
  }

  /** Removes all videos from a playlist. */
  public void clearPlaylist(String playlistName)
  {
    final VideoPlaylist playlist = playlists.get(playlistName.toLowerCase(Locale.ROOT));
    if (playlist == null)
      System.out.printf("Cannot clear playlist %s: Playlist does not exist%n", playlistName);
    else
    {
      System.out.printf("Successfully removed all videos from %s%n", playlistName);
      playlist.videoIDs.clear();
    }
  }

  /** Removes a playlist from the video player. */
  public void deletePlaylist(String playlistName)
  {
    final String name = playlistName.toLowerCase(Locale.ROOT);
    final VideoPlaylist playlist = playlists.get(name);
    if (playlist == null)
      System.out.printf("Cannot delete playlist %s: Playlist does not exist%n", playlistName);
    else
    {
      System.out.printf("Deleted playlist: %s%n", playlistName);
      playlists.remove(name);
    }
  }

  public void searchVideos(String searchTerm)
  {
    searchTerm = searchTerm.toLowerCase(Locale.ROOT);
    final List<Video> foundVideos = new ArrayList<>();
    for (Video video : videoLibrary.getVideos())
      if (video.getTitle().toLowerCase(Locale.ROOT).contains(searchTerm)
              && !flags.containsKey(video.getVideoId()))
        foundVideos.add(video);
    if (foundVideos.size() == 0)
      System.out.printf("No search results for %s%n", searchTerm);
    else
      SelectVideoFromResults(foundVideos, searchTerm);
  }

  public void searchVideosWithTag(String videoTag)
  {
    videoTag = videoTag.toLowerCase(Locale.ROOT);
    final List<Video> foundVideos = new ArrayList<>();
    for (Video video : videoLibrary.getVideos())
    {
      if (!flags.containsKey(video.getVideoId()))
      {
        for (String tag : video.getTags())
        {
          if (tag.equals(videoTag))
          {
            foundVideos.add(video);
            break;
          }
        }
      }
    }
    if (foundVideos.size() == 0)
      System.out.printf("No search results for %s%n", videoTag);
    else
      SelectVideoFromResults(foundVideos, videoTag);
  }

  private void SelectVideoFromResults(List<Video> videos, String resultLabel)
  {
    videos.sort((lhs, rhs) -> String.CASE_INSENSITIVE_ORDER.compare(
            lhs.getTitle(), rhs.getTitle()));
    System.out.printf("Here are the results for %s:%n", resultLabel);
    for (int i = 0; i < videos.size(); i++)
      System.out.printf("%s) %s%n", i + 1, videos.get(i).getAllDetails());
    System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
    System.out.println("If your answer is not a valid number, we will assume it's a no.");
    final String input = new Scanner(System.in).nextLine();
    try
    {
      final int option = Integer.parseInt(input);
      if (option > 0 && option <= videos.size())
        playVideo(videos.get(option - 1).getVideoId());
    }
    catch(Exception ignored) { }
  }

  public void flagVideo(String videoId)
  {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason)
  {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null)
      System.out.println("Cannot flag video: Video does not exist");
    else if (flags.containsKey(videoId))
      System.out.println("Cannot flag video: Video is already flagged");
    else
    {
      flags.put(videoId, reason);
      if (videoIsSelected && selectedVideo == video)
        stopVideo();
      System.out.printf("Successfully flagged video: %s (reason: %s)%n", video.getTitle(), reason);
    }
  }

  public void allowVideo(String videoId)
  {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null)
      System.out.println("Cannot remove flag from video: Video does not exist");
    else if (!flags.containsKey(videoId))
      System.out.println("Cannot remove flag from video: Video is not flagged");
    else
    {
      System.out.printf("Successfully removed flag from video: %s%n", video.getTitle());
      flags.remove(videoId);
    }
  }
}
