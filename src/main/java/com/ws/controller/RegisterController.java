package com.ws.controller;

import com.ws.bean.Sys_User;
import com.ws.common.utils.UuidUtils;
import com.ws.service.UserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final Logger logger= LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String goToRegister() {
        return "/register";
    }

    /**
     * @Author: WS
     * @Date: 2018/11/30 10:29
     * @Params: [formData]
     * @return: 成功 true，失败false
     * @Description: 用户注册
     */
    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Boolean register(Sys_User formData) {
        try {
            //生成用户的uuid
            //String uuid = UuidUtils.getUuid();
            //加密用户密码
            String pwd = formData.getPassword();
            SecureRandomNumberGenerator srng = new SecureRandomNumberGenerator();
            String salt = srng.nextBytes().toBase64();
            String password = new Sha256Hash(pwd, salt, 1024).toBase64();
            //将uuid和加密后的密码重新添加到用户信息中
            //formData.setId(uuid);
            formData.setSalt(salt);
            formData.setLoginCount(0);
            formData.setPassword(password);
            int count = userService.saveUser(formData);
            logger.info("用户[{}]注册成功",formData.getUsername());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Author: WS
     * @Date: 2018/12/5 12:24
     * @Params: username
     * @return: 返回一个json数据，格式为{"valid":false}
     * @Description: 校验用户名是否存在，存在返回false，不存在返回true
     * {"valid":false} //表示不合法，验证不通过
     * {"valid":true} //表示合法，验证通过
     */
    @ResponseBody
    @RequestMapping(value = "/checkUsername", method = RequestMethod.POST)
    public Map<String, Boolean> checkUsername(String username) {
        Boolean result = userService.checkUsername(username);
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("valid", result);
        return map;
    }

    /**
     * @Author: WS
     * @Date: 2018/12/5 19:01
     * @Params: email
     * @return: 返回一个json数据，格式为{"valid":false}
     * @Description: 校验邮箱是否存在，存在返回false，不存在返回true
     * {"valid":false} //表示不合法，验证不通过
     * {"valid":true} //表示合法，验证通过
     */
    @ResponseBody
    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    public Map<String, Boolean> checkEmail(String email) {
        Boolean result= userService.checkEmail(email);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("valid",result);
        return map;
    }
    /**
     * @Author: WS
     * @Date: 2018/12/5 19:13
     * @Params: tellphone
     * @return:  返回一个json数据，格式为{"valid":false}
     * @Description: 校验手机号是否存在，存在返回false，不存在返回true
     *  {"valid":false} //表示不合法，验证不通过
     *  {"valid":true} //表示合法，验证通过
     */

    @ResponseBody
    @RequestMapping(value = "checkPhone",method = RequestMethod.POST)
    public Map<String ,Boolean >checkPhone(String tellphone){
        Boolean result=userService.checkPhone(tellphone);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("valid",result);
        return map;
    }
}
