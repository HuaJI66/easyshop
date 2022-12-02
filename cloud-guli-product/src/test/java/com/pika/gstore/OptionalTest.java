package com.pika.gstore;

import com.pika.gstore.product.entity.AttrEntity;
import org.junit.jupiter.api.Test;

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
}
