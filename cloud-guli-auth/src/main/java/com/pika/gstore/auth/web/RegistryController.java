package com.pika.gstore.auth.web;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import com.pika.gstore.auth.feign.MemberFeignService;
import com.pika.gstore.auth.feign.ThirdFeignService;
import com.pika.gstore.auth.vo.UserRegistryReqVo;
import com.pika.gstore.common.constant.AuthConstant;
import com.pika.gstore.common.exception.BaseException;
import com.pika.gstore.common.to.MemberInfoTo;
import com.pika.gstore.common.to.UserLoginTo;
import com.pika.gstore.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/9 14:08
 */
@Slf4j
@Controller
public class RegistryController {
    @Resource
    private ThirdFeignService thirdFeignService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private MemberFeignService memberFeignService;

    @GetMapping("sms/send")
    @ResponseBody
    public R send(@RequestParam("phone") String phone) {
        String code = IdUtil.simpleUUID().substring(0, 4);
        log.info("code:{}", code);
        //todo 接口防刷防止60s内重复刷新发送
        Long expire = stringRedisTemplate.getExpire(AuthConstant.SMS_CACHE_CAPTCHA_PREFIX + phone, TimeUnit.SECONDS);
        log.info("expire:{}", expire);
        if (expire != null && expire > 0) {
            return R.error(BaseException.REPEATED_SEND_ERROR.getCode(), BaseException.REPEATED_SEND_ERROR.getMsg());
        }
        if (!StringUtils.isEmpty(phone) && phone.matches(AuthConstant.VALID_PHONE)) {
            try {
                // TODO: 2023/1/10 学习阶段,远程调用发送验证码容易超时异常,先注释掉
//                thirdFeignService.send(phone, code);
                // 缓存验证码
                stringRedisTemplate.opsForValue().set(AuthConstant.SMS_CACHE_CAPTCHA_PREFIX + phone,
                        code,
                        Duration.ofSeconds(AuthConstant.SMS_CACHE_CAPTCHA_EXPIRE));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return R.ok();
    }

    @PostMapping("registry")
    public String registry(@Valid UserRegistryReqVo reqVo,
                           BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> map = result.getFieldErrors().stream()
                    .filter(i -> i.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (i, j) -> i));
            //仅可取出一次的数据
            // TODO: 2023/1/10 分布式session
            redirectAttributes.addFlashAttribute("errors", map);
            return "redirect:http://auth.gulimall.com/reg.html";
        } else {
            //校验验证码
            String code = stringRedisTemplate.opsForValue().get(AuthConstant.SMS_CACHE_CAPTCHA_PREFIX + reqVo.getPhone());
            if (StringUtils.isEmpty(code)) {
                redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("code", "验证码已过期"));
                return "redirect:http://auth.gulimall.com/reg.html";
            } else {
                if (code.equals(reqVo.getCode())) {
                    //删除缓存验证码
                    stringRedisTemplate.delete(AuthConstant.SMS_CACHE_CAPTCHA_PREFIX + reqVo.getPhone());
                    //远程调用
                    R r = memberFeignService.userRegistry(reqVo);
                    if (r.getCode() == 0) {
                        return "redirect:http://auth.gulimall.com/login.html";
                    } else {
                        redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("msg", r.getMsg()));
                        return "redirect:http://auth.gulimall.com/reg.html";
                    }
                } else {
                    redirectAttributes.addFlashAttribute("errors", Collections.singletonMap("code", "验证码错误"));
                    return "redirect:http://auth.gulimall.com/reg.html";
                }
            }
        }
    }

}
