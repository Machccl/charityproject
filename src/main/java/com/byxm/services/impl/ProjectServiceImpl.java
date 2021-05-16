package com.byxm.services.impl;

import com.byxm.mapper.ProjectMapper;
import com.byxm.model.Donation;
import com.byxm.model.Project;
import com.byxm.util.TimeUtil;
import com.github.pagehelper.PageHelper;
import com.byxm.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<Project> getThree() {
        PageHelper.startPage(1,3," createtime desc");
        List<Project> projects = projectMapper.selectAll();
        return projects;
    }

    @Override
    public Project getProjectById(Integer id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public int endow(String proname, Double amount) {
        String updatetime = TimeUtil.getCurrentTime();
        Project project = projectMapper.selectByProname(proname);
        double now = project.getNow();
        now +=amount;
        project.setProgress((now/project.getTotal())*100);
        project.setNow(now);
        project.setUpdatetime(updatetime);
        int result = projectMapper.updateByPrimaryKeySelective(project);
        return result;
    }

    @Override
    public List<Project> projectPage(int page, int size) {
        PageHelper.startPage(page,size," createtime desc");
        List<Project> projects = projectMapper.selectAll();
        return projects;
    }

    @Override
    public List<Project> getAll() {
        return projectMapper.selectAll();
    }

    @Override
    public int getProjectCount() {
        return projectMapper.selectCount();
    }

    @Override
    public List<Project> getProjectPage(Integer page, Integer size, String title, String synopsis) {
        PageHelper.startPage(page,size);
        List<Project> projects = projectMapper.manageSelect(title,synopsis);
        return projects;
    }

    @Override
    public int modifyProject(Project project) {
        return projectMapper.updateByPrimaryKeySelective(project);
    }

    @Override
    public int removeProject(Integer id) {
        return projectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int addProject(Project project) {
        return projectMapper.insertSelective(project);
    }
}
