package com.tallybook.controller;

import com.tallybook.base.Response;
import com.tallybook.model.Constants;
import com.tallybook.model.User;
import com.tallybook.service.UserService;
import com.tallybook.utils.MD5Utils;
import com.tallybook.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tallybook.base.Response.Status.*;

/**
 * Created by Administrator on 2018/8/20.
 */
@RestController
@RequestMapping("/api/portal")
public class PortalController {

    private static final String REGEX = "^1[3-8][0-9]{9}$";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Value("${data.valid.code}")
    private String passCode;

    /**
     * 短信验证码
     *
     * @param request     请求
     * @param mobilePhone 手机号
     * @return Response
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/messageValidCode", method = RequestMethod.POST)
    public Response messageValidCode(@RequestParam(value = "mobilePhone",required = true) String mobilePhone) throws AuthenticationException {
        if (!checkPhone(mobilePhone)) return new Response(ERR_INVALID_FORMAT, "手机号格式错误");
        // 发送短信验证码
        String code = MessageUtils.getPhoneMessage(mobilePhone, "112233");
        // 十分钟超时
        redisTemplate.opsForValue().set(Constants.MSG_PHONE_CHECK + mobilePhone, code, Constants.OVER_TIME, TimeUnit.MINUTES);
        return new Response(OK);
    }

    /**
     * 验证码登陆
     */
    @RequestMapping(value = "/messageLogin", method = RequestMethod.POST)
    public Response messageLogin(@RequestParam("mobilePhone") String mobilePhone,
                                 @RequestParam("validCode") String validCode) {
        if (!checkPhone(mobilePhone)) return new Response(ERR_INVALID_FORMAT, "手机号格式错误");
        if (!validCode.equalsIgnoreCase(passCode)) {
            String s = redisTemplate.opsForValue().get(Constants.MSG_PHONE_CHECK + mobilePhone);
            if (StringUtils.isEmpty(s) || !s.equalsIgnoreCase(validCode)){
                return new Response(ERR_INVALID_FORMAT, "验证码错误");
            }
        }
        User user = userService.findOneByPhone(mobilePhone);
        if (user == null) {
            userService.register(mobilePhone);
        }
        return new Response(OK, "登陆成功");
    }

    /**
     * 密码登陆
     */
    @RequestMapping(value = "/passwordLogin", method = RequestMethod.POST)
    public Response passwordLogin(@RequestParam("mobilePhone") String mobilePhone,
                                  @RequestParam("password") String password) {
        if (!checkPhone(mobilePhone)) return new Response(ERR_INVALID_FORMAT, "手机号格式错误");
        if (password.length() < 6) {
            return new Response(ERR_INVALID_FORMAT, "密码长度不能低于6位");
        }
        String md5 = MD5Utils.getMD5(password);
        int count = userService.login(mobilePhone, md5);
        if (count == 0) {
            return new Response(ERR_NOT_EXIST, "用户名或密码错误");
        }
        return new Response(OK, "登陆成功");
    }

    /**
     * 忘记 / 重置密码
     */
    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    public Response resetPass(@RequestParam("mobilePhone") String mobilePhone,
                              @RequestParam("validCode") String validCode,
                              @RequestParam("password") String password) {
        if (!checkPhone(mobilePhone)) return new Response(ERR_INVALID_FORMAT, "手机号格式错误");
        if (password.length() < 6) {
            return new Response(ERR_INVALID_FORMAT, "密码长度不能低于6位");
        }
        if (!validCode.equalsIgnoreCase(passCode)) {
            String s = redisTemplate.opsForValue().get(Constants.MSG_PHONE_CHECK + mobilePhone);
            if (StringUtils.isEmpty(s) || !s.equalsIgnoreCase(validCode)){
                return new Response(ERR_INVALID_FORMAT, "验证码错误");
            }
        }
        User user = userService.findOneByPhone(mobilePhone);
        if (user == null) {
            return new Response(ERR_NOT_EXIST, "用户不存在");
        }
        user.setPassword(MD5Utils.getMD5(password));
        int count = userService.update(user);
        if (count == 0) {
            return new Response(ERR_RUNTIME_EXCEPTION, "修改密码失败");
        }
        return new Response(OK, "重置密码成功");
    }


    private Boolean checkPhone(String phone) {
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(phone);
        boolean b = m.matches();
        return !(phone.length() != 11 || !b);
    }
}
