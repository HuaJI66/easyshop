package com.pika.gstore.common.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回数据
 * <p>注意，此类继承了HashMap，在进行Json序列化时，会使用 MapSerializer，
 * 该序列化器仅遍历序列化entrySet的Entry实体，并不会将Map结构中的成员字段进行序列化
 * </p>
 *
 * @author pikachu
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public <T> T getData(TypeReference<T> typeReference) {
        Object data = this.get("data");
        String jsonStr = JSONUtil.toJsonStr(data);
        return JSONUtil.toBean(jsonStr, typeReference, true);
    }

    public Object getData() {
        return this.get("data");
    }

    public <T> T getData(String key, TypeReference<T> typeReference) {
        Object data = this.get(key);
        String jsonStr = JSONUtil.toJsonStr(data);
        return JSONUtil.toBean(jsonStr, typeReference, false);
    }

    public String getMsg() {
        return (String) this.get("msg");
    }

    public R setData(Object data) {
        this.put("data", data);
        return this;
    }

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Integer getCode() {
        return Integer.parseInt(get("code").toString());
    }
}
