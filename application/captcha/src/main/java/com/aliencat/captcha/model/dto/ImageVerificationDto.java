package com.aliencat.captcha.model.dto;


import java.io.Serializable;

public class ImageVerificationDto implements Serializable {

    /**
     * 验证码类型
     */
    private String type;

    /**
     * 字符验证码
     */
    private String charImage;

    /**
     * 加减乘除验证码
     */
    private String operationImage;

    /**
     * 滑动验证码，源图
     */
    private String originImage;

    /**
     * 滑动验证码，遮罩图
     */
    private String shadeImage;

    /**
     * 滑动验证码，裁剪图
     */
    private String cutoutImage;

    /**
     * 滑动验证码，X轴
     */
    private int x;

    /**
     * 滑动验证码，Y轴
     */
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCharImage() {
        return charImage;
    }

    public void setCharImage(String charImage) {
        this.charImage = charImage;
    }

    public String getOperationImage() {
        return operationImage;
    }

    public void setOperationImage(String operationImage) {
        this.operationImage = operationImage;
    }

    public String getOriginImage() {
        return originImage;
    }

    public void setOriginImage(String originImage) {
        this.originImage = originImage;
    }

    public String getShadeImage() {
        return shadeImage;
    }

    public void setShadeImage(String shadeImage) {
        this.shadeImage = shadeImage;
    }

    public String getCutoutImage() {
        return cutoutImage;
    }

    public void setCutoutImage(String cutoutImage) {
        this.cutoutImage = cutoutImage;
    }
}