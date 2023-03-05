package com.example.netty.config;

import com.example.netty.handler.Handler;
import com.example.netty.socket.NettyChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@RequiredArgsConstructor
public class NettyConfig {

    @Value("${server.netty.backlog}")
    private int backlog;
    @Value("${server.host}")
    private String host;
    @Value("${server.port}")
    private int port;

    @Bean
    public Bootstrap bootstrap(NettyChannelInitializer nettyChannelInitializer) throws InterruptedException {
        Bootstrap b = new Bootstrap();

        EventLoopGroup group = new NioEventLoopGroup();

        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(nettyChannelInitializer);

        //b.option(ChannelOption.SO_BACKLOG, backlog);

        return b;
    }

    /**
     * IP 소켓 주소(IP 주소, Port 번호)를 구현
     * 도메인 이름으로 객체 생성 가능
     * @return
     */
    @Bean
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(host,port);
    }
}
