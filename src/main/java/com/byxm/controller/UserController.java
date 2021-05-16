package com.byxm.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.byxm.config.AlipayConfig;
import com.byxm.model.Donation;
import com.byxm.model.Order;
import com.byxm.model.User;
import com.byxm.services.DonationService;
import com.byxm.services.OrderService;
import com.byxm.services.ProjectService;
import com.byxm.services.UserService;
import com.byxm.util.TimeUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    @RequestMapping("/user/endow")
    @ResponseBody
    public String pay(HttpServletRequest request, String out_trade_no, String subject, String total_amount, String body, String product_code) throws AlipayApiException {
        AlipayClient alipayClient=new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.app_id,AlipayConfig.merchant_private_key,"json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        AlipayTradePagePayRequest alipayRequest=new AlipayTradePagePayRequest();
//        alipayRequest.setReturnUrl(AlipayConfig.return_url);
//        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

        Donation donation = new Donation();
        donation.setNo(out_trade_no);
        donation.setAmount(Double.parseDouble(total_amount));
        donation.setCreatetime(TimeUtil.getCurrentTime());
        donation.setProname(subject);

        Subject sub = SecurityUtils.getSubject();
        sub.getSession().setAttribute("donation",donation);
        alipayRequest.setReturnUrl(path+"/user/doDonation");
        alipayRequest.setNotifyUrl(path+"/user/doDonation");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }

    //捐赠成功后返回的地址
    @RequestMapping("/user/doDonation")
    @Transactional
    public String success(HttpServletRequest request,Model model){
        Subject subject = SecurityUtils.getSubject();
        try{
            String username = (String) request.getSession().getAttribute("username");
            Donation donation = (Donation) subject.getSession().getAttribute("donation");
            donation.setUsername(username);
            int result1 = donationService.createDonation(donation);
            int result2 = projectService.endow(donation.getProname(),donation.getAmount());
            int result3 = userService.addAmount(username,donation.getAmount());
            if(result1>0&&result2>0&&result3>0){
                subject.getSession().removeAttribute("donation");
                return "redirect:/index";
            }
        }catch (Exception e){
            return "fail";
        }
        return "fail";
    }

    //个人中心
    @RequestMapping("/user/personal")
    public String personal(Model model){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        model.addAttribute("user",user);
        List<Order> orders = orderService.getAllByUserName(user.getUsername());
        if(orders!=null){
            model.addAttribute("orderLength",orders.size());
        }else {
            model.addAttribute("orderLength",0);
        }
        List<Donation> donations = donationService.getAllByUserName(user.getUsername());
        if(donations!=null){
            model.addAttribute("donationLength",donations.size());
        }else {
            model.addAttribute("donationLength",0);
        }
        return "user/personal";
    }
    //以废代捐
    @RequestMapping(value = "/user/doyfdj",method = RequestMethod.POST)
    @ResponseBody
    public Object doyfdj(String no,String address,String people,String phonenumber,Double weight,String type){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        Order order =new Order();
        order.setNo(no);
        order.setAddress(address);
        order.setPeople(people);
        order.setPhonenumber(phonenumber);
        order.setWeight(weight);
        order.setType(type);
        order.setUsername(user.getUsername());
        order.setCreatetime(TimeUtil.getCurrentTime());
        order.setUpdatetime(TimeUtil.getCurrentTime());
        int result = orderService.addOrder(order);
        if(result>0){
            return "true";
        }
        return "false";
    }
    //更新用户信息
    @RequestMapping(value = "/user/update",method = RequestMethod.POST)
    @ResponseBody
    public Object updateUser(Integer userid,String username,String realname,String email,String phonenumber,String address){
        User user = new User();
        user.setId(userid);
        user.setUsername(username);
        if(realname!=""){
            user.setRealname(realname);
        }
        if(email!=""){
            user.setEmail(email);
        }
        if(phonenumber!=""){
            user.setPhonenumber(phonenumber);
        }
        if(address!=""){
            user.setAddress(address);
        }
        user.setUpdatetime(TimeUtil.getCurrentTime());
        int result = userService.modifyUser(user);
        if(result>0){
            return "true";
        }
        return "false";
    }

    @RequestMapping("/user/Porderpage")
    @ResponseBody
    public Object Porderpage(Integer page,Integer size){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        Map<String,Object> map=new HashMap<>();
        List<Order> list = orderService.getOrderByUserName(user.getUsername(),page,size);
        List<Order> all = orderService.getAllByUserName(user.getUsername());
        PageInfo<Order> pageInfo=new PageInfo<>(all);
        map.put("totalPages",pageInfo.getPages());
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;
    }

    @RequestMapping("/user/Pdonationpage")
    @ResponseBody
    public Object Pdonationpage(Integer page,Integer size){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        Map<String,Object> map=new HashMap<>();
        List<Donation> list = donationService.getDonationByUserName(user.getUsername(),page,size);
        List<Donation> all = donationService.getAllByUserName(user.getUsername());
        PageInfo<Donation> pageInfo=new PageInfo<>(all);
        map.put("totalPages",pageInfo.getPages());
        map.put("total",pageInfo.getTotal());
        map.put("currentPage",pageInfo.getPageNum());
        map.put("list",list);

        return map;

    }
}
