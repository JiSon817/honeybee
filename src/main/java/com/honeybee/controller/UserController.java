package com.honeybee.controller;

import com.honeybee.common.bean.HoneyResult;
import com.honeybee.common.bean.UserBean;
import com.honeybee.service.UserService;
import com.honeybee.utils.HoneybeeConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * userController
 * @author HXY
 * @version 1.0
 */
@RestController
@RequestMapping("/honeybee/user")
public class UserController {

    @Autowired
    private UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 用户登录
     * @param user 用户
     * @return 登录结果
     */
    @PostMapping("/login")
    public HoneyResult userLogin(@RequestBody UserBean user, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        return userService.userLogin(user.getName(), user.getPassword(), request, response);
    }

    /**
     * 根据token查询用户信息
     * @return
     */
    @GetMapping("/token/{token}")
    public HoneyResult getUserByToken(@PathVariable String token) {
        return userService.getUserByToken(token);
    }

    /**
     * 用户注册
     * @return 注册结果
     */
    @PostMapping("/register")
    public HoneyResult userRegister(@RequestBody UserBean user) throws Exception {

        HoneyResult honeyResult = userService.userRegister(user);
        return honeyResult;
    }

    /**
     * ajax校验用户输入
     * @param param 待校验的参数
     * @param type 校验类型
     * @return 校验结果
     */
    @GetMapping("/check/{param}/{type}")
    public HoneyResult checkUser(@PathVariable String param, @PathVariable Integer type) {

        // 判断校验内容是否为空
        if (StringUtils.isBlank(param)) {
            logger.info("check param is null...");
            return HoneyResult.build(HoneybeeConstants.HttpStatusCode.BAD_REQUEST.getCode(),
                    "check param is not allowed null");
        }

        // 判断校验类型是否为空
        if (null == type) {
            logger.info("check type is null...");
            return HoneyResult.build(HoneybeeConstants.HttpStatusCode.BAD_REQUEST.getCode(),
                    "check type is not allowed null");
        }

        // 判断校验类型是否为用户名、电话、密码
        if (type != HoneybeeConstants.CheckCode.CHECK_USERNAME
                && type != HoneybeeConstants.CheckCode.CHECK_PASSWORD
                && type != HoneybeeConstants.CheckCode.CHECK_PHONE) {
            logger.info("check type error");
            return HoneyResult.build(HoneybeeConstants.HttpStatusCode.BAD_REQUEST.getCode(),
                    "check param error");
        }

        // 调用service层校验
        return userService.checkUser(param, type);

    }

}
