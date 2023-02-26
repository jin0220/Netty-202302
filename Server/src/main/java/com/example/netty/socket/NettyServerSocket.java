package com.example.netty.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * 네티 서버 실행 클래스
 */
@Component
@RequiredArgsConstructor
public class NettyServerSocket {
    private final ServerBootstrap serverBootstrap;
    private final InetSocketAddress tcpPort;
    private Channel serverChannel;

    public void start() {
        try {
            // ChannelFuture: I/O operation의 결과나 상태를 제공하는 객체
            // 지정한 host, port로 소켓을 바인딩하고 incoming connections을 받도록 준비함
            ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpPort).sync();

            // 서버 소켓이 닫힐 때까지 대기
            serverChannel = serverChannelFuture.channel().close().sync().channel();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy // 스프링 컨테이너에서 객체(빈)를 제거하기 전에 해야할 작업이 있다면 메소드위에 사용하는 어노테이션.
    public void stop() {
        if(serverChannel != null){
            serverChannel.close();
            serverChannel.parent().closeFuture();
        }
    }

}
