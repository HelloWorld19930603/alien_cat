package com.aliencat.javabase.spider;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CatchImage {
    // 地址
    private static final String URL = "http://blog.csdn.net/";
    // 编码
    private static final String ECODING = "utf-8";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "(?x)(src|SRC|background|BACKGROUND)=('|\")/?(([\\w-]+/)*([\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
    // img本地保存路径
    private static final String SAVE_PATH = "";

    /**
     * @param url      要抓取的网页地址
     * @param encoding 要抓取网页编码
     * @return
     */
    public static String getHtmlResourceByUrl(String url, String encoding) {
        URL urlObj = null;
        URLConnection uc = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        // 建立网络连接
        try {
            urlObj = new URL(url);
            // 打开网络连接
            uc = urlObj.openConnection();
            // 建立文件输入流
            isr = new InputStreamReader(uc.getInputStream(), encoding);
            // 建立缓存导入 将网页源代码下载下来
            reader = new BufferedReader(isr);
            // 临时
            String temp = null;
            while ((temp = reader.readLine()) != null) {// 一次读一行 只要不为空就说明没读完继续读
                // System.out.println(temp+"\n");
                buffer.append(temp + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关流
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 获取网页代码保存到本地
     *
     * @param url      网络地址
     * @param encoding 编码格式
     */
    public static void getJobInfo(String url, String encoding) {
        // 拿到网页源代码
        String html = getHtmlResourceByUrl(url, encoding);
        try {
            File fp = new File("c://tmp//src.html");
            //判断创建文件是否存在
            if (fp.exists()) {
                fp.mkdirs();
            }
            OutputStream os = new FileOutputStream(fp);          //建立文件输出流
            os.write(html.getBytes());
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载图片
     *
     * @param listImgSrc
     */
    public static void Download(List<String> listImgSrc) {
        int count = 0;
        try {
            for (int i = 0; i < listImgSrc.size(); i++) {
                String url = listImgSrc.get(i);
                String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
                URL uri = new URL(url);
                // 打开连接
                URLConnection con = uri.openConnection();
                //设置请求超时为5s
                con.setConnectTimeout(5 * 1000);
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                String src = listImgSrc.get(i).substring(URL.length());
                int index = src.lastIndexOf('/');
                String fileName = src.substring(0, index + 1);
                File sf = new File(SAVE_PATH + fileName);
                if (!sf.exists()) {
                    sf.mkdirs();
                }
                OutputStream os = new FileOutputStream(sf.getPath() + "\\" + imageName);
                System.out.println(++count + ".开始下载:" + url);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有链接
                os.close();
                is.close();
                System.out.println(imageName + ":--下载完成");
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }

    /**
     * 得到网页中图片的地址
     *
     * @param htmlStr html字符串
     * @return List<String>
     */
    private static List<String> getImgStr(String htmlStr) {
        List<String> pics = new ArrayList<String>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        p_image = Pattern.compile(IMGURL_REG, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile(IMGSRC_REG).matcher(img);
            while (m.find()) {
                String imgUrl = m.group(3);
                //判断是否有http://+网址
                if (!imgUrl.contains("http://") && !imgUrl.contains("https://")) {
                    imgUrl = URL + imgUrl;
                }
                pics.add(imgUrl);
            }
        }
        return pics;
    }

    /**
     * 下载css文件
     *
     * @param URL 网络地址
     */
    private static void DownCss(String URL) throws ClientProtocolException, IOException {
        // 创建httpclient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httpget实例
        HttpGet httpget = new HttpGet(URL);
        // 执行get请求
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        // 获取返回实体
        String content = EntityUtils.toString(entity, ECODING);
        // 解析网页 得到文档对象
        Document doc = Jsoup.parse(content);
        // 获取指定的 <img />
        Elements elements = doc.select("head link");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            // 获取 <img /> 的 src
            String url = element.attr("href");
            // 再发请求最简单了，并由于该链接是没有 https:开头的，得人工补全 ✔
            HttpGet PicturehttpGet = new HttpGet(URL + url);
            CloseableHttpResponse pictureResponse = httpclient.execute(PicturehttpGet);
            HttpEntity pictureEntity = pictureResponse.getEntity();
            InputStream inputStream = pictureEntity.getContent();
            // 使用 common-io 下载图片到本地，注意图片名不能重复 ✔
            //获取url字符串最后一个/之后的所有，也就是图片名称和格式
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            if (fileName.contains("?")) { //排除url末尾带了参数的情况
                fileName = fileName.substring(0, fileName.lastIndexOf("?"));
            }
            FileUtils.copyToFile(inputStream, new File("c://tmp//" + fileName));
            pictureResponse.close(); // pictureResponse关闭
        }
        response.close(); // response关闭
        httpclient.close(); // httpClient关闭
    }

    public static void main(String[] args) throws ClientProtocolException, IOException {
        //获得html文本内容
        String HTML = CatchImage.getHtmlResourceByUrl(URL, ECODING);
        //获取图片src的url地址
        List<String> imgSrc = CatchImage.getImgStr(HTML);
        //下载图片
        CatchImage.Download(imgSrc);
        //下载css样式
        CatchImage.DownCss(URL);
        //保存网页源码
        getJobInfo(URL, ECODING);
    }
}

