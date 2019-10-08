/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.model.httpResponse;

import java.util.List;

/**
 * 〈干货网站返回数据〉
 *
 * @author wankun
 * @create 2019/8/7
 * @since 1.0.0
 */
public class BaseGankEntity<T> extends BaseResponse {
    private boolean error;
    private T results;
    private List<String> category;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}