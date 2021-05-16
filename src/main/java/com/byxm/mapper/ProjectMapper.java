package com.byxm.mapper;

import com.byxm.model.Project;

import java.util.List;

public interface ProjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Project record);

    int insertSelective(Project record);

    Project selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Project record);

    int updateByPrimaryKey(Project record);

    List<Project> selectAll();

    Project selectByProname(String proname);

    int selectCount();

    List<Project> manageSelect(String title, String synopsis);
}