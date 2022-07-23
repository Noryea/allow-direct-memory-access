package me.noryea.dma.client.vertex.buffer;

import me.noryea.dma.client.gl.attribute.BufferVertexFormat;

import java.nio.ByteBuffer;

public interface VertexBufferView {

    boolean ensureBufferCapacity(int bytes);

    ByteBuffer getDirectBuffer();

    int getWriterPosition();

    void flush(int vertexCount, BufferVertexFormat format);

    BufferVertexFormat getVertexFormat();
}
