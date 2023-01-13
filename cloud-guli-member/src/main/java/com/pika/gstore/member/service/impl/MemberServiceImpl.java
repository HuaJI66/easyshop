package com.pika.gstore.member.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pika.gstore.common.to.GiteeUserInfoTo;
import com.pika.gstore.common.to.UserLoginTo;
import com.pika.gstore.common.utils.PageUtils;
import com.pika.gstore.common.utils.Query;
import com.pika.gstore.member.dao.MemberDao;
import com.pika.gstore.member.entity.MemberEntity;
import com.pika.gstore.member.exception.PhoneExistException;
import com.pika.gstore.member.exception.UsernameExistException;
import com.pika.gstore.member.service.MemberLevelService;
import com.pika.gstore.member.service.MemberService;
import com.pika.gstore.member.vo.UserRegistryReqVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * @author pi'ka'chu
 */
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    @Resource
    private MemberLevelService memberLevelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void userRegistry(UserRegistryReqVo vo) {
        MemberEntity member = new MemberEntity();

        // 验证手机号,用户名唯一性
        String username = vo.getUsername().trim();
        String phone = vo.getPhone().trim();
        availableInfo(username, phone);

        member.setCreateTime(new Date());
        member.setMobile(phone);
        member.setUsername(username);
        member.setSourceType(0);
        //密码加密
        member.setPassword(BCrypt.hashpw(vo.getPassword()));
        member.setLevelId(memberLevelService.getDefaultLevel());

        //保存用户
        save(member);
    }

    public void availableInfo(String username, String phone) throws UsernameExistException, PhoneExistException {
        LambdaQueryWrapper<MemberEntity> wrapper = new LambdaQueryWrapper<>();

        wrapper.select(MemberEntity::getId)
                .eq(MemberEntity::getUsername, username)
                .last("limit 1");
        MemberEntity entity = getOne(wrapper);
        if (entity != null) {
            throw new UsernameExistException();
        }

        LambdaQueryWrapper<MemberEntity> wrapper1 = new LambdaQueryWrapper<>();

        wrapper1.select(MemberEntity::getId)
                .eq(MemberEntity::getMobile, phone)
                .last("limit 1");
        MemberEntity entity1 = getOne(wrapper1);
        if (entity1 != null) {
            throw new PhoneExistException();
        }
    }

    @Override
    public MemberEntity login(UserLoginTo userLoginTo) {
        MemberEntity memberDb = getOne(new LambdaQueryWrapper<MemberEntity>()
                .eq(MemberEntity::getUsername, userLoginTo.getAccount())
                .or()
                .eq(MemberEntity::getMobile, userLoginTo.getAccount())
                .last("limit 1"));
        return memberDb != null && BCrypt.checkpw(userLoginTo.getPassword(), memberDb.getPassword()) ? memberDb : null;
    }

    @Override
    public MemberEntity loginOrRegistry(GiteeUserInfoTo userInfoTo) {
        MemberEntity member = null;
        if (userInfoTo.getId() != null) {
            //需要判断是登录还是注册
            LambdaQueryWrapper<MemberEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MemberEntity::getSourceType, 1)
                    .eq(MemberEntity::getSocialId, userInfoTo.getId())
                    .last("limit 1");

            member = getOne(wrapper);
            if (member != null) {
                //登录
                member.setSocialAccessToken(userInfoTo.getAccess_token());
                updateById(member);
            } else {
                //注册
                member = new MemberEntity();
                member.setSocialAccessToken(userInfoTo.getAccess_token());
                member.setSourceType(1);
                member.setSocialId(userInfoTo.getId());
                member.setHeader(userInfoTo.getAvatar_url());
                member.setUsername(userInfoTo.getLogin());
                member.setNickname(userInfoTo.getName());
                member.setEmail(userInfoTo.getEmail());
                member.setLevelId(memberLevelService.getDefaultLevel());
                member.setCreateTime(new Date());
                save(member);
            }
        }
        return member;
    }

}
