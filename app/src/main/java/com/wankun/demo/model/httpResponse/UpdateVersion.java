package com.wankun.demo.model.httpResponse;

import com.wankun.demo.model.httpResponse.BaseResponse;

import java.io.Serializable;

/**更新实体类
 * @author 万坤
 *
 */
public class UpdateVersion extends BaseResponse implements Serializable {

    /**
     * //文件大小
     */
    private long fileSize;
    /**
     * //更新内容
     */
    private String content;
    /**
     * //下载链接
     */
    private String downloadLink;
    /**
     * //版本code
     */
    private int versionCode;
    /**
     * //版本名称
     */
    private String versionName;
    /**
     * //是否必须更新 1必须 0不需要
     */
    private int isneed;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getIsneed() {
        return isneed;
    }

    public void setIsneed(int isneed) {
        this.isneed = isneed;
    }
}
