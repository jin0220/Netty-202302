package com.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
@ChannelHandler.Sharable // 여러 채널에서 핸들러를 공유할 수 있음
@RequiredArgsConstructor
public class Handler extends ChannelInboundHandlerAdapter {
    private ByteBuf buff;


    /**
     * 서버에 대한 연결이 만들어지면 호출
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String sendMessage = "Hello, Netty !";

        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        StringBuilder builder = new StringBuilder();
        builder.append("전송한 문자열 [");
        builder.append(sendMessage);
        builder.append("]");

        System.out.println(builder.toString());
        ctx.writeAndFlush(messageBuffer);
    }

    /**
     * 서버로부터 메시지를 수신하면 호출
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());

        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
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
