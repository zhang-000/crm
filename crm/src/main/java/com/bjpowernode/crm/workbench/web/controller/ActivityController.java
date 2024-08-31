package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ActivityController {
    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    //让市场活动的创建和修改按钮可以动态获取user的数据
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        //查询出lock_state为1的所有用户
        List<User> userList = userService.queryAllUsers();
        //将查询出的user集合保存到request中
        request.setAttribute("userList",userList);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    /**
     * 接收用户新建市场活动的数据，保存到数据库中，activity表中的create_by不是用户填写的，
     * 二十需要从当前用户的id来获取，用户登录成功时我们保存了user到session中，于是可以从
     * session中获取user的id.
     * activity的id用工具类UUIDUtils来生成uuid
     * 返回json数据还是交给ReturnObject来
     * @param activity
     * @param session
     * @return
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) (session.getAttribute(Constants.SESSION_USER));
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject ro = new ReturnObject();
        int i = 0;
        //一般除了查询都会try catch一下
        try {
            i = activityService.saveCreateActivity(activity);
            if (i > 0) {
                ro.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统忙，请稍后再试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ro.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙，请稍后再试...");
        }

        return ro;
    }
}
