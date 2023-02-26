package com.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class Handler extends ChannelInboundHandlerAdapter {
    private ByteBuf buff;


    /**
     * 서버에 대한 연결이 만들어지면 호출
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty Connect()", CharsetUtil.UTF_8)); // 채널 활성화 시 메시지 전송
    }

    /**
     * 서버로부터 메시지를 수신하면 호출
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf mBuf = (ByteBuf) msg;
        buff.writeBytes(mBuf); // 클라이언트에서 보내는 데이터가 축척됨
        mBuf.release();

        final ChannelFuture f = ctx.writeAndFlush(buff);
        f.addListener(ChannelFutureListener.CLOSE);

//        System.out.println("Client receive : " + msg.toString(CharsetUtil.UTF_8));  // 수신한 메시지 로깅
    }

    /**
     * 읽기 작업 중 오류가 발생했을 때 호출
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();
    }
}
