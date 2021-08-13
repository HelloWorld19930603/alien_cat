package com.aliencat.communication.netty.http;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    //该方法的作用是用来读取客户端的数据， FullHttpRequest 请求对象
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        System.out.println("客户端：" + ctx.channel().remoteAddress() + " 发来请求");
        //读取数据
        Map<String, Object> data = readData(request);
        System.out.println("data:" + data);
        //根据请求参数，处理不同的业务，做出不同的响应
        //就返回一个Hello好了
        String content = "Hello Netty";
        //响应数据
        writeDate(ctx, request, content);
    }

    //写数据给客户端
    private void writeDate(ChannelHandlerContext ctx, FullHttpRequest request, String content) {
        //把数据拷贝到buffer
        ByteBuf buffer = Unpooled.copiedBuffer(content, CharsetUtil.UTF_8);
        //创建Http响应对象，设置http版本和状态吗，以及数据
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
        //响应头信息
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plan");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());

        //响应结果
        ctx.writeAndFlush(httpResponse);
    }

    //读取数据，get,或者post
    public Map<String, Object> readData(FullHttpRequest request) {
        Map<String, Object> param = null;
        if (request.method() == HttpMethod.GET) {
            param = getGetParams(request);
        } else if (request.method() == HttpMethod.POST) {
            param = getPostParams(request);
        }
        System.out.println(param);
        return param;
    }

    //获取GET方式传递的参数
    private Map<String, Object> getGetParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        if (fullHttpRequest.method() == HttpMethod.GET) {
            // 处理get请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> paramList = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : paramList.entrySet()) {
                params.put(entry.getKey(), entry.getValue().get(0));
            }
            return params;
        }
        return null;
    }

    //获取POST方式传递的参数
    private Map<String, Object> getPostParams(FullHttpRequest fullHttpRequest) {

        if (fullHttpRequest.method() == HttpMethod.POST) {
            // 处理POST请求
            String strContentType = fullHttpRequest.headers().get("Content-Type").trim();
            if (strContentType.contains("x-www-form-urlencoded")) {
                return getFormParams(fullHttpRequest);
            } else if (strContentType.contains("application/json")) {
                return getJSONParams(fullHttpRequest);
            }
        }
        return null;
    }

    //解析JSON数据
    private Map<String, Object> getJSONParams(FullHttpRequest fullHttpRequest) {
        ByteBuf content = fullHttpRequest.content();
        byte[] reqContent = new byte[content.readableBytes()];
        content.readBytes(reqContent);
        try {
            return JSON.parseObject(new String(reqContent, "UTF-8"), Map.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //解析from表单数据（Content-Type = x-www-form-urlencoded）
    private Map<String, Object> getFormParams(FullHttpRequest fullHttpRequest) {
        Map<String, Object> params = new HashMap<String, Object>();
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullHttpRequest);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

}
