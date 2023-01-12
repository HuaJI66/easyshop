package com.pika.gstore.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import com.pika.gstore.common.to.UserLoginTo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.member.entity.MemberEntity;
import com.pika.gstore.member.exception.PhoneExistException;
import com.pika.gstore.member.exception.UsernameExistException;
import com.pika.gstore.member.vo.UserRegistryReqVo;

import java.util.Map;

/**
 * 会员
 *
 * @author pikachu
 * @email pikachu@pikachu.com
 * @date 2022-11-21 21:24:18
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void userRegistry(UserRegistryReqVo vo)throws UsernameExistException, PhoneExistException;

    void availableInfo(String username, String phone) throws UsernameExistException, PhoneExistException;

    MemberEntity login(UserLoginTo userLoginTo);

    MemberEntity loginOrRegistry(GiteeUserInfoTo giteeUserInfoTo);
}

