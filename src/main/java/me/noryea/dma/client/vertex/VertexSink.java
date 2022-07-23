package me.noryea.dma.client.vertex;


public interface VertexSink {

    void ensureCapacity(int count);

    void flush();

    int getVertexCount();
}
