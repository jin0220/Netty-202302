package com.example.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * network stream을 다시 프로그램의 message 포맷으로 변환
 * 인바운드 데이터를 channelpipeline의 다음 channelinboundhandler를 위해 변환할때 사용한다.
 * ByteToMessageDecoder: inbound data 가 처리할 만큼 모일 때 까지 버퍼에 저장한 후 처리한다.
 */
@Component
@RequiredArgsConstructor
public class Decoder extends ByteToMessageDecoder {
    private int DATA_LENGTH = 2048;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // DATA_LENGTH 길이 만큼 데이터가 들어올 때까지 기다린다.
        if (in.readableBytes() < DATA_LENGTH) {
            return;
        }

        out.add(in.readBytes(DATA_LENGTH));
    }
}
