package com.byxm.services;

import com.byxm.model.Video;

import java.util.List;

public interface VideoService {

    List<Video> getThree();

    List<Video> getFour();

    Video getVideoById(Integer id);

    List<Video> videoPage(int page, int size);

    List<Video> getAll();

    int getVideoCount();

    List<Video> getVideoPage(Integer page, Integer size, String title, String examine);

    int modifyVideo(Video video);

    int removeVideo(Integer id);

    int addVideo(Video video);

    List<Video> getReadyVideoPage(Integer page, Integer size);
}
