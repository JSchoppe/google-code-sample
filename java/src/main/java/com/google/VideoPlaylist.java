package com.google;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** A class used to represent a Playlist */
class VideoPlaylist
{
    public final String name;
    public final List<String> videoIDs;

    public VideoPlaylist(String name)
    {
        videoIDs = new ArrayList<String>();
        this.name = name;
    }
}
