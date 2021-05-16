package com.byxm.controller;

import com.byxm.model.Article;
import com.byxm.model.Project;
import com.byxm.model.Video;
import com.byxm.services.ArticleService;
import com.byxm.services.ProjectService;
import com.byxm.services.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MyController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private VideoService videoService;

    @RequestMapping({"/","/index"})
    public String index(Model model){
        List<Project> projects = projectService.getThree();
        model.addAttribute("projects",projects);
        List<Article> articles = articleService.getThree();
        model.addAttribute("articles",articles);
        List<Video> videos = videoService.getFour();
        model.addAttribute("videos",videos);
        return "index";
    }
    //捐赠项目
    @RequestMapping("/project")
    public String project(Model model,Integer id){
        Project project = projectService.getProjectById(id);
        model.addAttribute("project",project);
        List<Project> items = projectService.getThree();
        model.addAttribute("items",items);
        //获取当前项目捐赠进度
        double progress = project.getProgress();
        String jd = ((int) progress)+"%";
        model.addAttribute("progress",jd);
        //判断当前项目是否已经完成捐赠
        if(progress>=100){
            model.addAttribute("fish",true);
        }else {
            model.addAttribute("fish",false);
        }
        return "axproject";
    }

    @RequestMapping("/article")
    public String article(Model model,Integer id){
        Article article = articleService.getArticleById(id);
        model.addAttribute("article",article);
        List<Article> items = articleService.getThree();
        model.addAttribute("items",items);
        return "article";
    }

    @RequestMapping("/noPermission")
    public String noPermission(){
        return "noPermission";
    }

    @RequestMapping("/video")
    public String video(Model model,Integer id){
        Video video = videoService.getVideoById(id);
        model.addAttribute("video",video);
        List<Video> items = videoService.getThree();
        model.addAttribute("items",items);
        return "video";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        request.getSession().removeAttribute("username");
        subject.getSession().removeAttribute("user");
        return "redirect:/index";
    }

    @RequestMapping("/artlist")
    public String articleList(Model model,@RequestParam(defaultValue = "1") int p){

        Map<String,Object> msg=new HashMap<>();
        Map<String, List<Article>> arts=new HashMap<>();
        int page=(p-1)*3+1;
        for(int i=0;i<3;i++){
            List<Article> articles = articleService.articlePage(page+i,3);
            arts.put(""+i,articles);
        }

        List<Article> list=articleService.getAll();
        PageInfo<Article> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%9==0){
            totalPages=total/9;
        }else {
            totalPages=total/9+1;
        }


        msg.put("totalPages",totalPages);
        msg.put("total",total);
        msg.put("currentPage",p);


        model.addAttribute("msg",msg);
        model.addAttribute("arts",arts);

        return "artlist";
    }

    @RequestMapping("/videolist")
    public String videoList(Model model,@RequestParam(defaultValue = "1") int p){

        Map<String,Object> msg=new HashMap<>();
        Map<String, List<Video>> vs=new HashMap<>();
        int page=(p-1)*3+1;
        for(int i=0;i<3;i++){
            List<Video> videos = videoService.videoPage(page+i,4);
            vs.put(""+i,videos);
        }

        List<Video> list = videoService.getAll();
        PageInfo<Video> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%12==0){
            totalPages=total/12;
        }else {
            totalPages=total/12+1;
        }


        msg.put("totalPages",totalPages);
        msg.put("total",total);
        msg.put("currentPage",p);


        model.addAttribute("msg",msg);
        model.addAttribute("vs",vs);

        return "videolist";
    }

    @RequestMapping("/projectlist")
    public String projectlist(Model model,@RequestParam(defaultValue = "1") int p){

        Map<String,Object> msg=new HashMap<>();
        Map<String, List<Project>> pros=new HashMap<>();
        int page=(p-1)*3+1;
        for(int i=0;i<3;i++){
            List<Project> projects = projectService.projectPage(page+i,3);
            pros.put(""+i,projects);
        }

        List<Project> list=projectService.getAll();
        PageInfo<Project> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%9==0){
            totalPages=total/9;
        }else {
            totalPages=total/9+1;
        }


        msg.put("totalPages",totalPages);
        msg.put("total",total);
        msg.put("currentPage",p);


        model.addAttribute("msg",msg);
        model.addAttribute("pros",pros);

        return "axlist";
    }

    @RequestMapping("/yfdj")
    public String yfdj(){

        return "yfdj";
    }


}
