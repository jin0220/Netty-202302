package com.example.netty.config;

import com.example.netty.socket.NettyChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 네티 설정 클래스
 */
@Configuration
@RequiredArgsConstructor
public class NettyConfig {

    @Value("${server.netty.boss-count}")
    private int bossCount;
    @Value("${server.netty.worker-count}")
    private int workerCount;
    @Value("${server.netty.backlog}")
    private int backlog;

    /**
     * 서버 설정을 도와주는 클래스
     * @param nettyChannelInitializer
     * @return
     */
    @Bean
    public ServerBootstrap serverBootstrap(NettyChannelInitializer nettyChannelInitializer) {
        ServerBootstrap b = new ServerBootstrap();

        b.group(bossGroup(), workerGroup()) // 이벤트 루프 설정(연결 수락, 데이터 송수신 처리)
                .channel(NioServerSocketChannel.class) // 소켓 입출력 모드 (논블로킹)
                .handler(new LoggingHandler(LogLevel.DEBUG)) // 여기서 등록된 핸들러는 서버 소켓 채널에서 발생한 이벤트만을 처리
                .childHandler(nettyChannelInitializer); // 클라이언트 소켓 채널에서 발생하는 이벤트를 수신하여 처리
                                                        // 서버 소켓 채널로 연결된 클라이언트 채널에 파이프라인을 설정하는 역할을 수행

        b.option(ChannelOption.SO_BACKLOG, backlog); // 소켓 동작 방식 지정

        return b;
    }

    /**
     * 연결을 수락하고, 수락한 연결을 worker에게 등록
     * @return
     */
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    /**
     * 수락한 연결의 트래픽 관리
     * @return
     */
    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }
}
