package com.example.netty.socket;

import com.example.netty.decoder.Decoder;
import com.example.netty.handler.Handler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Handler handler;

    /**
     * 클라이언트 소켓 채널이 생성될 때 사용
     * 채널 파이프라인: 채널에서 발생한 이벤트가 이동하는 통로
     * 이벤트 핸들러: 이벤트를 처리하는 클래스
     * 코덱: 이벤트 핸들러를 상속받아서 구현한 구현체
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline channelPipeline = ch.pipeline();

        Decoder decoder = new Decoder();

        // 뒤이어 처리할 디코더 및 핸들러 추가
        channelPipeline.addLast(decoder);
        channelPipeline.addLast(handler);

    }
}
