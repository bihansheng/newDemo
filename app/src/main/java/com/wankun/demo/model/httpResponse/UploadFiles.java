/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.model.httpResponse;

import java.util.List;

/**
 * 〈文件上传返回值〉
 *
 * @author wankun
 * @create 2019/5/14
 * @since 1.0.0
 */
public class UploadFiles {
    public  List<String> src;

    public List<String> getSrc() {
        return src;
    }

    public void setSrc(List<String> src) {
        this.src = src;
    }
}