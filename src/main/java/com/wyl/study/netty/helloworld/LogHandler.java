package com.wyl.study.netty.helloworld;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class LogHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("------------");
        //ctx.writeAndFlush(msg);
    }

    /*@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("LogHandler channelReadComplete..");
    }*/

}
