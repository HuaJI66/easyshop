package com.pika.gstore;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pika.gstore.product.entity.AttrEntity;
import com.pika.gstore.product.vo.Attr;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Optional;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2022/11/30 20:23
 */
public class OptionalTest {
    @Test
    public void test1(){
        AttrEntity attr = new AttrEntity();
        Optional<Long> attrId = Optional.ofNullable(attr.getAttrId());
        System.out.println("attrId = " + attrId);
    }
    @Test
    public void test2(){
        AttrEntity attr = new AttrEntity();
        attr.setAttrId(1L);
        Optional<Long> attrId = Optional.ofNullable(attr.getAttrId());
        System.out.println("attrId = " + attrId.orElse(0L));
    }
    @Test
    public void test3(){
        AttrEntity attr = new AttrEntity();
//        attr.setAttrId(1L);
        Optional<Long> attrId = Optional.ofNullable(attr.getAttrId());
        System.out.println("attrId.isPresent() = " + attrId.isPresent());
    }
    @Test
    public void test4(){
        AttrEntity attr = new AttrEntity();
        attr.setAttrId(0L);
        attr.setAttrName("123");
        attr.setSearchType(0);
        attr.setValueType(0);
        attr.setIcon("14321");
        attr.setValueSelect("3123");
        attr.setAttrType(0);
        attr.setEnable(0L);
        attr.setCatelogId(0L);
        attr.setShowDesc(0);

        byte[] bytes = RedisSerializer.json().serialize(attr);
        System.out.println("bytes = " + new String(bytes));
        AttrEntity o = (AttrEntity) RedisSerializer.json().deserialize(bytes);
        System.out.println("o = " + o);
    }
}
