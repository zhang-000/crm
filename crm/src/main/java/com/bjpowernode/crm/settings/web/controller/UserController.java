package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    //建议url和controller方法处理完请求后，响应信息返回的页面资源目录保持一致
    @RequestMapping("/settings/qx/user/tologin.do")
    //请求转发  应该什么时候用重定向？？
    public String toLogin() {
        return "settings/qx/user/login";
    }

    //登录验证
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        //根据查询结果，生成对应的响应信息
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            //用户名或密码错误
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        } else if (DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0){
            //账号已超过过期时间
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号已过期");
        } else if ("0".equals(user.getLockState())) {
            //账号已被锁定
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号已被锁定");
        } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
            //ip不在允许的ip地址范围内
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("ip受限");
        } else {
            //登录成功
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            //保证无论用户在哪个页面都能显示当前用户的用户名，于是将user存放在session对象中
            session.setAttribute(Constants.SESSION_USER, user);
            //如果需要记住密码，则往外写cookie
            if("true".equals(isRemPwd)){
                //当服务器发送一个cookie给客户端，并且没有指定cookie的Path属性时，大多数浏览器会
                // 将cookie的默认路径设置为请求该cookie的URL的目录（即最后一个“/”及其之前的部分）
                Cookie c1=new Cookie("loginAct",user.getLoginAct());
                c1.setMaxAge(10*24*60*60);
                response.addCookie(c1);
                Cookie c2=new Cookie("loginPwd",user.getLoginPwd());
                c2.setMaxAge(10*24*60*60);
                response.addCookie(c2);
            }else{
                //把没有过期cookie删除
                Cookie c1=new Cookie("loginAct","1");
                c1.setMaxAge(0);
                response.addCookie(c1);
                Cookie c2=new Cookie("loginPwd","1");
                c2.setMaxAge(0);
                response.addCookie(c2);
            }
        }

        return returnObject;
    }

    //安全退出
    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session) {
        //清空cookie
        Cookie c1=new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2=new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();

        //重定向到项目首页
        return "redirect:/"; //通过springmvc框架来重定向, response.sendRedirect("/crm/")
    }
}
