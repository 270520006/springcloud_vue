package com.zsp.zspoamember.controller;


import com.alibaba.fastjson.JSON;
import com.zsp.utils.R;
import com.zsp.zspoamember.entity.Member;
import com.zsp.zspoamember.service.MemberService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zsp
 * @since 2021-05-03
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;
    @GetMapping("/list")
    public R getAllMember(){
        return R.ok().put("members", JSON.toJSONString(memberService.list()));
    }
    @GetMapping("/add/{userName}/{userPassword}")
    public R addMember(@PathVariable String userName, @PathVariable String userPassword ){
        Member member = new Member();
        member.setUserName(userName);
        member.setUserPassword(userPassword);
        return R.ok().put("addResult",JSON.toJSON(memberService.save(member)));
    }

    @GetMapping("/login/{userName}/{userPassword}")
    public String login(@PathVariable(value = "userName") String userName,
                        @PathVariable(value = "userPassword")String userPassword, Model model){

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, userPassword);
        try {
            subject.login(token);//执行登陆的方法
            return "index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg","用户名错误");
            return "login";
        }catch (IncorrectCredentialsException e)
        {
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

}

