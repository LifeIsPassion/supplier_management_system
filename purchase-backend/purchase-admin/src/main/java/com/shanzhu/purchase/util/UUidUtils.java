package com.shanzhu.purchase.util;

import java.util.UUID;

public class UUidUtils {

    /**
     * 生成 9位uuid
     */
    public static Integer  uuid() {
        Integer uuid = UUID.randomUUID().toString().replaceAll("-", "").hashCode();
        uuid = uuid < 0 ? -uuid : uuid;//String.hashCode() 值会为空
          uuid = Integer.valueOf(uuid.toString().substring(0,6));
        return  uuid;
    }

    public static void main(String[] args) {
        Integer uuid = uuid();
    }
}
