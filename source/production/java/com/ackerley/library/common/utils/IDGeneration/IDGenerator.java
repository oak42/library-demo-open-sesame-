package com.ackerley.library.common.utils.IDGeneration;

import java.util.UUID;

/**
 * Created by ackerley on 2018/5/20.
 * 封装各种生成唯一性ID算法的工具类.
 * 抄、删减了jeesite4的IdGenerate与IdWorker，IdWorker是借鉴的twitter的snowflake ID生成方案...
 */
public class IDGenerator {

    private static IDWorker IDWorker = new IDWorker(-1, -1);

    /**
     * 生成UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取新唯一编号（18为数值）
     * 来自于twitter项目snowflake的id产生方案，全局唯一，时间有序。
     * 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
     */
    public static String nextId() {
        return String.valueOf(IDWorker.nextId());
    }

}
