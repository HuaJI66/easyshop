package com.pika.gstore.order.feign;

import com.pika.gstore.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("cloud-guli-member")
public interface MemberFeignService {
    @GetMapping("member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getMemberReceiveAddress(@PathVariable("memberId") Long id) ;
}
