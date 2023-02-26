package com.example.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable // 여러 채널에서 핸들러를 공유할 수 있음
@RequiredArgsConstructor
public class Handler extends ChannelInboundHandlerAdapter {
    private int DATA_LENGTH = 2048;
    private ByteBuf buff;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 핸들러가 생성될 때 호출
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        buff = ctx.alloc().buffer(DATA_LENGTH);
    }

    /**
     * 핸들러가 제거될 때 호출
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buff = null;
    }

    /**
     * 클라이언트와 연결되어 트래픽을 생성할 준비가 되었을 때 호출
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        log.info("Remote Address: " + remoteAddress);
    }

    /**
     * inbound buffer에서 읽을 값이 있을 경우 호출. 즉, 메시지가 들어올 때마다 호출
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
