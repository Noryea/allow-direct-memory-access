package me.noryea.dma.client.vertex.type;

import me.noryea.dma.client.gl.attribute.BufferVertexFormat;
import me.noryea.dma.client.vertex.VertexSink;

public interface BufferVertexType<T extends VertexSink> extends VertexType<T> {
    BufferVertexFormat getBufferVertexFormat();
}
