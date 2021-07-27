package com.aliencat.communication.netty.http;

import com.alibaba.fastjson.JSONObject;
import com.aliencat.communication.netty.Media;
import com.aliencat.communication.netty.param.RequestParam;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.Charset;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        try {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest req = (FullHttpRequest) msg;
                //获取请求内容
                String content = req.content().toString(Charset.defaultCharset());
                RequestParam request = JSONObject.parseObject(content, RequestParam.class);
                Object result = Media.execute(request);

                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                        HttpResponseStatus.OK, Unpooled.wrappedBuffer(JSONObject.toJSONString(result).getBytes(Charset.defaultCharset())));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.channel().writeAndFlush(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.channel().writeAndFlush("");
        }


    }


}
