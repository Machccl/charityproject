package com.byxm.mapper;

import com.byxm.model.Video;

import java.util.List;

public interface VideoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    List<Video> selectAll();

    int selectCount();

    List<Video> manageSelect(String title, String examine);

    List<Video> selectReadyVideo();
}