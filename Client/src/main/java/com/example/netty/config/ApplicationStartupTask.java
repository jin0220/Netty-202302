package com.example.netty.config;

import com.example.netty.socket.NettyClientSocket;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {
    private final NettyClientSocket nettyClientSocket;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        nettyClientSocket.start();
    }
}
