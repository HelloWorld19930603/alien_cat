package com.aliencat.captcha.service;

import com.aliencat.captcha.exception.ServiceException;
import com.aliencat.captcha.model.dto.ImageVerificationDto;
import com.aliencat.captcha.model.vo.ImageVerificationVo;

public interface CaptchaService {

    /**
     * 根据类型获取验证码
     * @param imageVerificationDto  图片类型dto
     * @return  图片验证码
     * @throws ServiceException 获取图片验证码异常
     */
    ImageVerificationVo selectImageVerificationCode(ImageVerificationDto imageVerificationDto) throws ServiceException;

    /**
     * 校验图片验证码
     * @param x x轴坐标
     * @param y y轴坐标
     * @return 校验结果
     * @throws ServiceException 校验图片验证码异常
     */
    boolean checkVerificationResult(String x, String y) throws ServiceException;
}
