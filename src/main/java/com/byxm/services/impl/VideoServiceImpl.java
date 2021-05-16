package com.byxm.services.impl;

import com.byxm.mapper.VideoMapper;
import com.byxm.model.Video;
import com.byxm.services.VideoService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public List<Video> getThree() {
        PageHelper.startPage(1,3," createtime desc");
        List<Video> videos = videoMapper.selectAll();
        return videos;
    }

    @Override
    public List<Video> getFour() {
        PageHelper.startPage(1,4," createtime desc");
        List<Video> videos = videoMapper.selectAll();
        return videos;
    }

    @Override
    public Video getVideoById(Integer id) {

        return videoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Video> videoPage(int page, int size) {
        PageHelper.startPage(page,size," createtime desc");
        List<Video> videos = videoMapper.selectAll();
        return videos;
    }

    @Override
    public List<Video> getAll() {
        return videoMapper.selectAll();
    }

    @Override
    public int getVideoCount() {
        return videoMapper.selectCount();
    }

    @Override
    public List<Video> getVideoPage(Integer page, Integer size, String title, String examine) {
        PageHelper.startPage(page,size);
        List<Video> videos = videoMapper.manageSelect(title,examine);
        return videos;
    }

    @Override
    public int modifyVideo(Video video) {
        return videoMapper.updateByPrimaryKeySelective(video);
    }

    @Override
    public int removeVideo(Integer id) {
        return videoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int addVideo(Video video) {
        return videoMapper.insertSelective(video);
    }

    @Override
    public List<Video> getReadyVideoPage(Integer page, Integer size) {
        return videoMapper.selectReadyVideo();
    }
}
