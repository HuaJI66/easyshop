package com.pika.gstore.member.controller;

import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import com.pika.gstore.common.to.UserLoginTo;
import com.pika.gstore.common.utils.R;
import com.pika.gstore.member.entity.MemberEntity;
import com.pika.gstore.member.exception.PhoneExistException;
import com.pika.gstore.member.exception.UsernameExistException;
import com.pika.gstore.member.service.MemberService;
import com.pika.gstore.member.vo.UserRegistryReqVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 17:15
 */
@RestController
@RequestMapping("member")
public class UserLoginRegistryController {
    @Resource
    private MemberService memberService;

    @PostMapping("/registry/add")
    public R userRegistry(@RequestBody UserRegistryReqVo vo) {
        try {
            memberService.userRegistry(vo);
        } catch (UsernameExistException e) {
            return R.error(BaseException.USER_EXIST_ERROR.getCode(), BaseException.USER_EXIST_ERROR.getMsg());
        } catch (PhoneExistException e) {
            return R.error(BaseException.PHONE_EXIST_ERROR.getCode(), BaseException.PHONE_EXIST_ERROR.getMsg());
        }
        return R.ok();
    }

    @PostMapping("/login/get")
    public R getMember(@RequestBody UserLoginTo userLoginTo) {
        MemberEntity user = memberService.login(userLoginTo);
        return user != null ? R.ok().setData(user) : R.error(BaseException.LOGIN_INVALID_ERROR.getCode(), BaseException.LOGIN_INVALID_ERROR.getMsg());
    }

    @PostMapping("/oauth/gitee/login")
    public R loginOrRegistry(@RequestBody GiteeUserInfoTo giteeUserInfoTo) {
        try {
            MemberEntity member = memberService.loginOrRegistry(giteeUserInfoTo);
            if (member != null) {
                return R.ok().setData(member);
            }
            return R.error(BaseException.USER_INFO_INVALID_EXCEPTION.getCode(), BaseException.USER_INFO_INVALID_EXCEPTION.getMsg());
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
