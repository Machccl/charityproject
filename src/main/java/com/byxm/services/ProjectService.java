package com.byxm.services;

import com.byxm.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getThree();

    Project getProjectById(Integer id);

    int endow(String proname, Double amount);

    List<Project> projectPage(int page, int size);

    List<Project> getAll();

    int getProjectCount();

    List<Project> getProjectPage(Integer page, Integer size, String title, String synopsis);

    int modifyProject(Project project);

    int removeProject(Integer id);

    int addProject(Project project);
}
