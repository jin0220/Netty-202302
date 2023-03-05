package com.example.netty.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
@RequiredArgsConstructor
public class NettyClientSocket {

    private final Bootstrap bootstrap;
    private final InetSocketAddress tcpPort;
    private Channel clientChannel;

    public void start() {
        try {
            ChannelFuture clientChannelFuture = bootstrap.connect(tcpPort).sync();

            clientChannel = clientChannelFuture.channel().close().sync().channel();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        if(clientChannel != null){
            clientChannel.close();
            clientChannel.parent().closeFuture();
        }
    }

}
