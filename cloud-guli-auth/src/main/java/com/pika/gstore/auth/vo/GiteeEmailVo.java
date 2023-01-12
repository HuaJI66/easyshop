package com.pika.gstore.auth.vo;

import lombok.Data;

import java.util.List;

/**
 * Desc:
 *
 * @author pikachu
 * @since 2023/1/11 23:39
 */
@Data
public class GiteeEmailVo {
    private String email;
    private String state;
    private List<String> scope;
}
