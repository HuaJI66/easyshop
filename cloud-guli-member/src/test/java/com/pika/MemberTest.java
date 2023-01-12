package com.pika;

import com.pika.gstore.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/10 17:53
 */
@SpringBootTest
public class MemberTest {
    @Resource
    private MemberService memberService;
    @Test
    public void test1() {
        memberService.availableInfo("Sato", "17132692161");
    }
}
