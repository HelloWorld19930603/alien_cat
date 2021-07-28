package com.aliencat.communication.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.nio.charset.Charset;

public class HttpClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if(msg instanceof HttpResponse){
			HttpResponse res = (HttpResponse)msg;
			if(res.status()==HttpResponseStatus.OK){
			}
		}
		
		if(msg instanceof HttpContent){
			HttpContent content = (HttpContent)msg;
			String result = content.content().toString(Charset.defaultCharset());
			System.out.println(result);
			ctx.channel().close();
		}
		
	}
	
	

}
