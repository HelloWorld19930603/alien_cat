package com.aliencat.captcha.service.impl;

import com.aliencat.captcha.controller.CaptchaController;
import com.aliencat.captcha.enums.VerificationCodeType;
import com.aliencat.captcha.exception.ServiceException;
import com.aliencat.captcha.exception.code.ServiceExceptionCode;
import com.aliencat.captcha.model.dto.ImageVerificationDto;
import com.aliencat.captcha.model.vo.ImageVerificationVo;
import com.aliencat.captcha.service.CaptchaService;
import com.google.code.kaptcha.Producer;
import com.aliencat.captcha.utils.ImageVerificationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

    /**
     * 字符验证码
     */
    @Autowired
    private Producer captchaProducer;

    /**
     * 数值运算验证码
     */
    @Autowired
    private Producer captchaProducerMath;

    /**
     * 源图路径前缀
     */
    @Value("${captcha.slide-verification-code.path.origin-image}")
    private String verificationImagePathPrefix;

    /**
     * 模板图路径前缀
     */
    @Value("${captcha.slide-verification-code.path.template-image}")
    private String templateImagePathPrefix;

    /**
     * 获取request对象
     * @return 返回request对象
     */
    protected static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取response对象
     * @return 返回response对象
     */
    protected static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 根据类型获取验证码
     * @param imageVerificationDto 用户信息
     * @return 图片验证码
     * @throws ServiceException 查询图片验证码异常
     */
    @Override
    public ImageVerificationVo selectImageVerificationCode(ImageVerificationDto imageVerificationDto) throws ServiceException {

        ImageVerificationVo imageVerificationVo = null;
        String type = null;
        try {
            if (imageVerificationDto == null || imageVerificationDto.getType() == null) {
                type = VerificationCodeType.CHAR.name();
            } else {
                type = imageVerificationDto.getType();
            }
            VerificationCodeType verificationCodeType = Enum.valueOf(VerificationCodeType.class, type.toUpperCase());

            switch (verificationCodeType) {
                //  获取运算验证码
                case OPERATION:
                    imageVerificationVo = selectOperationVerificationCode(imageVerificationDto);
                    break;
                //  获取字符验证码
                case CHAR:
                    imageVerificationVo = selectCharVerificationCode(imageVerificationDto);
                    break;
                //  获取滑动验证码
                case SLIDE:
                    imageVerificationVo = selectSlideVerificationCode(imageVerificationDto);
                    break;
                default:
                    throw new ServiceException(ServiceExceptionCode.SELECT_VERIFICATION_CODE_ERROR);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.SELECT_VERIFICATION_CODE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.SELECT_VERIFICATION_CODE_ERROR);
        }
        return imageVerificationVo;
    }


    /**
     * 获取运算验证码
     *
     * @param imageVerificationDto 用户信息
     * @return 运算验证吗
     * @throws ServiceException 查询运算验证码异常
     */
    private ImageVerificationVo selectOperationVerificationCode(ImageVerificationDto imageVerificationDto) throws ServiceException {

        byte[] bytes = null;
        String text = "";
        BufferedImage bufferedImage = null;
        ImageVerificationVo imageVerificationVo = null;

        try {

            imageVerificationVo = new ImageVerificationVo();
            imageVerificationVo.setType(imageVerificationDto.getType());
            //  生成运算验证码文本
            text = captchaProducerMath.createText();
            String value = text.substring(0, text.lastIndexOf("@"));
            //  生成运算验证码图片
            bufferedImage = captchaProducerMath.createImage(value);
            //  验证码存入redis
            getRequest().getSession().setAttribute("imageVerificationVo", imageVerificationVo);
            //  在分布式应用中，可将session改为redis存储
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            //  图片base64加密
            imageVerificationVo.setOperationImage(Base64Utils.encodeToString(bytes));
            imageVerificationVo.setType(imageVerificationDto.getType());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.SELECT_VERIFICATION_CODE_ERROR);
        }
        return imageVerificationVo;

    }

    /**
     * 获取字符验证码
     *
     * @param imageVerificationDto 用户信息
     * @return 字符验证码
     * @throws ServiceException 获取字符验证码异常
     */
    private ImageVerificationVo selectCharVerificationCode(ImageVerificationDto imageVerificationDto) throws ServiceException {

        byte[] bytes = null;
        String text = "";
        BufferedImage bufferedImage = null;
        ImageVerificationVo imageVerificationVo = null;

        try {

            imageVerificationVo = new ImageVerificationVo();
            //  生成字符验证码文本
            text = captchaProducer.createText();
            //  生成字符验证码图片
            bufferedImage = captchaProducer.createImage(text);
            getRequest().getSession().setAttribute("imageVerificationVo", imageVerificationVo);
            //  在分布式应用中，可将session改为redis存储
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
            //  图片base64加密
            imageVerificationVo.setCharImage(Base64Utils.encodeToString(bytes));
            imageVerificationVo.setType(imageVerificationDto.getType() == null ? "char" : imageVerificationDto.getType());

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.SELECT_VERIFICATION_CODE_ERROR);
        }
        return imageVerificationVo;

    }

    /**
     * 获取滑动验证码
     * @param imageVerificationDto 验证码参数
     * @return 滑动验证码
     * @throws ServiceException 获取滑动验证码异常
     */
    public ImageVerificationVo selectSlideVerificationCode(ImageVerificationDto imageVerificationDto) throws ServiceException {

        ImageVerificationVo imageVerificationVo = null;
        try {

            File verifyImageImport = new File(verificationImagePathPrefix);
            File[] verifyImages = verifyImageImport.listFiles();

            Random random = new Random(System.currentTimeMillis());
            //  随机取得原图文件夹中一张图片
            File originImageFile = verifyImages[random.nextInt(verifyImages.length)];

            //  获取模板图片文件
            File templateImageFile = new File(templateImagePathPrefix + "/template.png");

            //  获取描边图片文件
            File borderImageFile = new File(templateImagePathPrefix + "/border.png");
            //  获取描边图片类型
            String borderImageFileType = borderImageFile.getName().substring(borderImageFile.getName().lastIndexOf(".") + 1);

            //  获取原图文件类型
            String originImageFileType = originImageFile.getName().substring(originImageFile.getName().lastIndexOf(".") + 1);
            //  获取模板图文件类型
            String templateImageFileType = templateImageFile.getName().substring(templateImageFile.getName().lastIndexOf(".") + 1);

            //  读取原图
            BufferedImage verificationImage = ImageIO.read(originImageFile);
            //  读取模板图
            BufferedImage readTemplateImage = ImageIO.read(templateImageFile);

            //  读取描边图片
            BufferedImage borderImage = ImageIO.read(borderImageFile);

            //  获取原图感兴趣区域坐标
            imageVerificationVo = ImageVerificationUtil.generateCutoutCoordinates(readTemplateImage);

            int y  = imageVerificationVo.getY();
            //  在分布式应用中，可将session改为redis存储
            getRequest().getSession().setAttribute("imageVerificationVo", imageVerificationVo);

            //  根据原图生成遮罩图和切块图
            imageVerificationVo = ImageVerificationUtil.pictureTemplateCutout(originImageFile, originImageFileType, templateImageFile, templateImageFileType, imageVerificationVo.getX(), imageVerificationVo.getY());

            //   剪切图描边
            imageVerificationVo = ImageVerificationUtil.cutoutImageEdge(imageVerificationVo, borderImage, borderImageFileType);
            imageVerificationVo.setY(y);
            imageVerificationVo.setType(imageVerificationDto.getType());

        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.URL_DECODER_ERROR);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.IO_EXCEPTION);
        }

        return imageVerificationVo;
    }

    /**
     * 滑动验证码验证方法
     * @param x x轴坐标
     * @param y y轴坐标
     * @return 滑动验证码验证状态
     * @throws ServiceException 验证滑动验证码异常
     */
    @Override
    public boolean checkVerificationResult(String x, String y) throws ServiceException {

        int threshold = 5;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            ImageVerificationVo imageVerificationVo = (ImageVerificationVo) request.getSession().getAttribute("imageVerificationVo");
            if (imageVerificationVo != null) {
                if ((Math.abs(Integer.parseInt(x) - imageVerificationVo.getX()) <= threshold) && y.equals(String.valueOf(imageVerificationVo.getY()))) {
                    System.out.println("验证成功");
                    return true;
                } else {
                    System.out.println("验证失败");
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.IO_EXCEPTION);
        }
    }
}