package com.byxm.controller;

import com.byxm.model.*;
import com.byxm.services.*;
import com.byxm.util.TimeUtil;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class ManageController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private VideoService videoService;

    //后台管理页面
    @RequestMapping("/manage/main")
    public String mt(Model model){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        model.addAttribute("user",user);
        return "manage/manage";
    }
    //后台管理后台默认页面
    @RequestMapping("/manage/welcome")
    public String welcome(Model model){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        int articleCount = articleService.getArticleCount();
        int userCount = userService.getUserCount();
        int projectCount = projectService.getProjectCount();
        int orderCount = orderService.getOrderCount();
        int donationCount = donationService.getDonationCount();
        int videoCount = videoService.getVideoCount();
        model.addAttribute("time",new Date());
        model.addAttribute("article",articleCount);
        model.addAttribute("userCount",userCount);
        model.addAttribute("project",projectCount);
        model.addAttribute("order",orderCount);
        model.addAttribute("donation",donationCount);
        model.addAttribute("video",videoCount);
        model.addAttribute("user",user);
        return "manage/welcome";
    }
    //返回echart页面
    @RequestMapping("/manage/echart")
    public String echart(){
        return "manage/echart";
    }

    //进行echart初始化操作
    @RequestMapping("/manage/echartInit")
    @ResponseBody
    public Object echart(Integer id){

        int articleCount = articleService.getArticleCount();
        int userCount = userService.getUserCount();
        int projectCount = projectService.getProjectCount();
        int orderCount = orderService.getOrderCount();
        int donationCount = donationService.getDonationCount();
        int videoCount = videoService.getVideoCount();

        if(id!=null&&id==1){
            List<Integer> list = new ArrayList<>();
            list.add(articleCount);
            list.add(userCount);
            list.add(orderCount);
            list.add(projectCount);
            list.add(videoCount);
            list.add(donationCount);
            return list;
        }
        //返回折线图及柱状图的数据，返回形式：[10,3,2,3,14,6]

        if(id!=null&&id==2){
            List<Map<String,Object>> list = new ArrayList<>();
            Map m1= new HashMap();
            m1.put("value",articleCount);
            m1.put("name","文章");
            list.add(m1);
            Map m2= new HashMap();
            m2.put("value",userCount);
            m2.put("name","用户");
            list.add(m2);
            Map m3= new HashMap();
            m3.put("value",orderCount);
            m3.put("name","以废代捐");
            list.add(m3);
            Map m4= new HashMap();
            m4.put("value",projectCount);
            m4.put("name","捐赠项目");
            list.add(m4);
            Map m5= new HashMap();
            m5.put("value",videoCount);
            m5.put("name","视频短片");
            list.add(m5);
            Map m6= new HashMap();
            m6.put("value",donationCount);
            m6.put("name","捐赠次数");
            list.add(m6);
            return list;
        }
        //返回饼图数据,形式为：[{"name":"文章","value":10},{"name":"用户","value":3},{"name":"以废代捐","value":2}...]

        return "false";
    }



    //文章管理

    //文章管理页面
    @RequestMapping("/manage/articlemanage")
    public String articlemanage(Model model){
        return "manage/article/articlemanage";
    }
    //以json形式返回文章列表
    @RequestMapping(value = "/manage/articlepage",method = RequestMethod.POST)
    @ResponseBody
    public Object articlepage(Integer page,Integer size,String title,String examine){
        Map<String,Object> map=new HashMap<>();
        List<Article> list = articleService.getArticlePage(page,size,title,examine);
        PageInfo<Article> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }
    //文章内容的修改页面
    @RequestMapping("/manage/checkart")
    public String checkart(Model model,Integer id){
        if (id!=null){
            Article article = articleService.getArticleById(id);
            model.addAttribute("article",article);
        }
        return "manage/article/checkart";
    }
    //修改文章内容
    @RequestMapping("/manage/changeArtContent")
    @ResponseBody
    public Object changeArtContent(Integer id,String content){
        if (id!=null&&content!=null){
            Article article = new Article();
            article.setId(id);
            article.setContent(content);
            article.setExamine("ready");
            article.setUpdatetime(TimeUtil.getCurrentTime());
            int result = articleService.modifyArticle(article);
            if (result>0){
                return "true";
            }
            return "false";
        }
        return "false";
    }

    //文章编辑页面
    @RequestMapping("/manage/articleedit")
    public String articleedit(Model model,Integer id){
        Article article = articleService.getArticleById(id);
        model.addAttribute("article",article);

        return "manage/article/articleedit";
    }
    //修改文章标题等信息
    @RequestMapping("/manage/articleInfo")
    @ResponseBody
    public Object articleInfo(Integer id,String title,String imgurl){
        Article article = new Article();
        article.setId(id);
        if(title!=null){
            article.setTitle(title);
        }
        if(imgurl!=null){
            article.setImgurl(imgurl);
        }
        article.setExamine("ready");
        article.setUpdatetime(TimeUtil.getCurrentTime());
        int result = articleService.modifyArticle(article);
        if(result>0){
            return "true";  //修改成功
        }
        return "false";  //修改失败
    }
    //文章删除
    @RequestMapping("/manage/artcielDel")
    @ResponseBody
    public Object articleDel(Integer id){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if(user.getPower().equals("2")){
            int result = articleService.removeArticle(id);
            if(result>0){
                return "true";
            }
            return "发生错误";
        }else {
            return "删除失败：权限不足";
        }

    }

    //添加文章
    @RequestMapping("/manage/articleadd")
    public String articleadd(){

        return "manage/article/articleadd";
    }
    @RequestMapping("/manage/doArticleAdd")
    @ResponseBody
    public Object doArticleAdd(String title,String imgurl,String content){
        Article article = new Article();
        article.setTitle(title);
        article.setImgurl(imgurl);
        article.setContent(content);
        article.setExamine("ready");
        article.setCreatetime(TimeUtil.getCurrentTime());
        article.setUpdatetime(TimeUtil.getCurrentTime());
        int result = articleService.addArticle(article);
        if(result>0){
            return "true";
        }
        return "false";
    }




    //视频管理

    //视频管理页面
    @RequestMapping("/manage/videomanage")
    public String videomanage(Model model){
        return "manage/video/videomanage";
    }
    //以json形式返回文章列表
    @RequestMapping(value = "/manage/videopage",method = RequestMethod.POST)
    @ResponseBody
    public Object videopage(Integer page,Integer size,String title,String examine){
        Map<String,Object> map=new HashMap<>();
        List<Video> list = videoService.getVideoPage(page,size,title,examine);
        PageInfo<Video> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }

    //视频编辑页面
    @RequestMapping("/manage/videoedit")
    public String videoedit(Model model,Integer id){
        Video video = videoService.getVideoById(id);
        model.addAttribute("video",video);

        return "manage/video/videoedit";
    }
    //修改文章标题等信息
    @RequestMapping("/manage/videoInfo")
    @ResponseBody
    public Object videoInfo(Integer id,String title,String imgurl,String vid){
        Video video = new Video();
        video.setId(id);
        if(title!=null){
            video.setTitle(title);
        }
        if(imgurl!=null){
            video.setImgurl(imgurl);
        }
        if(vid!=null){
            video.setVid(vid);
        }
        video.setExamine("ready");
        int result = videoService.modifyVideo(video);
        if(result>0){
            return "true";  //修改成功
        }
        return "false";  //修改失败
    }
    //视频删除
    @RequestMapping("/manage/videoDel")
    @ResponseBody
    public Object videoDel(Integer id){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if(user.getPower().equals("2")){
            int result = videoService.removeVideo(id);
            if(result>0){
                return "true";
            }
            return "发生错误";
        }else {
            return "删除失败：权限不足";
        }

    }
    //视频内容页面
    @RequestMapping("/manage/checkvideo")
    public String checkvideo(Model model,Integer id){
        Video video = videoService.getVideoById(id);
        model.addAttribute("video",video);
        return "manage/video/checkvideo";
    }
    //添加视频
    @RequestMapping("/manage/videoadd")
    public String videoadd(){

        return "manage/video/videoadd";
    }
    @RequestMapping("/manage/doVideoAdd")
    @ResponseBody
    public Object doVideoAdd(String title,String imgurl,String vid){
        Video video = new Video();
        video.setTitle(title);
        video.setImgurl(imgurl);
        video.setVid(vid);
        video.setExamine("ready");
        video.setCreatetime(TimeUtil.getCurrentTime());
        int result = videoService.addVideo(video);
        if(result>0){
            return "true";
        }
        return "false";
    }


    //订单管理
    @RequestMapping("manage/ordermanage")
    public String ordermanage(){
        return "manage/order/ordermanage";
    }

    @RequestMapping("manage/orderPage")
    @ResponseBody
    public Object orderPage(Integer page,Integer size,String no,String people,String status){
        Map<String,Object> map=new HashMap<>();
        List<Order> list = orderService.getOrderPage(page,size,no,people,status);
        PageInfo<Order> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }
    //修改订单状态
    @RequestMapping("manage/orderEdit")
    public String orderEdit(Model model,Integer id){
        Order order = orderService.getOrderById(id);
        model.addAttribute("order",order);
        return "manage/order/orderedit";
    }
    @RequestMapping("manage/orderInfo")
    @ResponseBody
    public Object orderInfo(Integer id,String status){
        Order order = new Order();
        order.setId(id);
        if(status!=null){
            order.setStatus(status);
            order.setUpdatetime(TimeUtil.getCurrentTime());
            int result = orderService.modifyOrder(order);
            if(result>0){
                return "true";
            }
        }
        return "false";
    }
    //删除订单
    @RequestMapping("manage/orderDel")
    @ResponseBody
    public Object orderDel(Integer id){
        int result = orderService.removeOrderById(id);
        if(result>0){
            return "true";
        }
        return "false";
    }








    @RequestMapping("/smanage/usermanage")
    public String usermanage(){
        return "manage/user/usermanage";
    }

    @RequestMapping(value = "/smanage/userpage",method = RequestMethod.POST)
    @ResponseBody
    public Object userpage(Integer page,Integer size,String username,String power){
        Map<String,Object> map=new HashMap<>();
        List<User> list = userService.getUserPage(page,size,username,power);
        PageInfo<User> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",total);
        map.put("currentPage",page);
        map.put("list",list);

        return map;
    }

    @RequestMapping("/smanage/useredit")
    public String useredit(Model model,Integer id){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return "manage/user/useredit";
    }

    @RequestMapping("/smanage/changepower")
    @ResponseBody
    public Object changepower(Integer id,String power){
        User user = userService.getUserById(id);
        if(power!=null){
            user.setPower(power);
            user.setUpdatetime(TimeUtil.getCurrentTime());
            int result = userService.modifyUser(user);
            if(result>0){
                return "true";
            }
        }
        return "false";
    }

    @RequestMapping("/smanage/deluser")
    @ResponseBody
    public Object deluser(Integer id){
        int result = userService.removeUserByid(id);
        if(result>0){
            return "true";
        }
        return "false";
    }



    //项目管理

    //项目管理页面s
    @RequestMapping("/smanage/promanage")
    public String promanage(Model model){
        return "manage/project/promanage";
    }
    //以json形式返回项目列表
    @RequestMapping(value = "/smanage/proPage",method = RequestMethod.POST)
    @ResponseBody
    public Object propage(Integer page,Integer size,String title,String synopsis){
        Map<String,Object> map=new HashMap<>();
        List<Project> list = projectService.getProjectPage(page,size,title,synopsis);
        PageInfo<Project> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }
    //项目内容的修改页面
    @RequestMapping("/smanage/checkPro")
    public String checkPro(Model model,Integer id){
        if (id!=null){
            Project project = projectService.getProjectById(id);
            model.addAttribute("project",project);
        }
        return "manage/project/checkPro";
    }
    //修改项目内容
    @RequestMapping("/smanage/changeProContent")
    @ResponseBody
    public Object changeProContent(Integer id,String content){
        if (id!=null&&content!=null){
            Project project = new Project();
            project.setId(id);
            project.setContent(content);
            project.setUpdatetime(TimeUtil.getCurrentTime());
            int result = projectService.modifyProject(project);
            if (result>0){
                return "true";
            }
            return "false";
        }
        return "false";
    }

    //项目编辑页面
    @RequestMapping("/smanage/proEdit")
    public String proedit(Model model,Integer id){
        Project project = projectService.getProjectById(id);
        model.addAttribute("project",project);

        return "manage/project/proedit";
    }
    //修改项目标题等信息
    @RequestMapping("/smanage/proInfo")
    @ResponseBody
    public Object proInfo(Integer id,String title,String imgurl,Integer total,Double now){
        Project project = new Project();
        project.setId(id);
        project.setTitle(title);
        project.setImgurl(imgurl);
        project.setTotal(total);
        project.setNow(now);
        Double progress = total/now;
        project.setProgress(progress);
        project.setUpdatetime(TimeUtil.getCurrentTime());
        int result = projectService.modifyProject(project);
        if(result>0){
            return "true";  //修改成功
        }
        return "false";  //修改失败
    }
    //项目删除
    @RequestMapping("/smanage/proDel")
    @ResponseBody
    public Object proDel(Integer id){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if(user.getPower().equals("2")){
            int result = projectService.removeProject(id);
            if(result>0){
                return "true";
            }
            return "发生错误";
        }else {
            return "删除失败：权限不足";
        }

    }

    //添加项目
    @RequestMapping("/smanage/proAdd")
    public String proAdd(){

        return "manage/project/proadd";
    }
    @RequestMapping("/smanage/doProAdd")
    @ResponseBody
    public Object doProAdd(String title,String imgurl,String synopsis,String content,Integer total){
        Project project = new Project();
        project.setTitle(title);
        project.setImgurl(imgurl);
        project.setContent(content);
        project.setSynopsis(synopsis);
        project.setTotal(total);
        project.setNow(0.0);
        project.setCreatetime(TimeUtil.getCurrentTime());
        project.setUpdatetime(TimeUtil.getCurrentTime());
        int result = projectService.addProject(project);
        if(result>0){
            return "true";
        }
        return "false";
    }



    //文章审核
    @RequestMapping("/smanage/articleExamine")
    public String articleExamine(){
        return "manage/examine/artexamine";
    }


    @RequestMapping("/smanage/doArticleExamine")
    @ResponseBody
    public Object doArticleExamine(Integer id,String examine){
        Article article = new Article();
        article.setId(id);
        article.setExamine(examine);
        int result = articleService.modifyArticle(article);
        if(result>0){
            return "true";
        }
        return "false";
    }
    //返回为审核的文章
    @RequestMapping(value = "/smanage/articlepage",method = RequestMethod.POST)
    @ResponseBody
    public Object sarticlepage(Integer page,Integer size){
        Map<String,Object> map=new HashMap<>();
        List<Article> list = articleService.getReadyArticlePage(page,size);
        PageInfo<Article> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }




    //视频审核
    @RequestMapping("/smanage/videoExamine")
    public String videoExamine(){
        return "manage/examine/videoexamine";
    }

    @RequestMapping("/smanage/doVideoExamine")
    @ResponseBody
    public Object doVideoExamine(Integer id,String examine){
        Video video = new Video();
        video.setId(id);
        video.setExamine(examine);
        int result = videoService.modifyVideo(video);
        if(result>0){
            return "true";
        }
        return "false";
    }
    @RequestMapping(value = "/smanage/videopage",method = RequestMethod.POST)
    @ResponseBody
    public Object svideopage(Integer page,Integer size){
        Map<String,Object> map=new HashMap<>();
        List<Video> list = videoService.getReadyVideoPage(page,size);
        PageInfo<Video> pageInfo=new PageInfo<>(list);
        int total= (int) pageInfo.getTotal();
        int totalPages=0;
        if(total%size==0){
            totalPages=total/size;
        }else {
            totalPages=total/size+1;
        }
        map.put("totalPages",totalPages);
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }






}
