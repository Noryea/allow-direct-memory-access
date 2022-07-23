package me.noryea.dma.client.vertex.type;

import me.noryea.dma.client.vertex.VertexSink;
import me.noryea.dma.client.vertex.buffer.VertexBufferView;

public interface BlittableVertexType<T extends VertexSink> extends BufferVertexType<T> {

    T createBufferWriter(VertexBufferView buffer, boolean direct);

    default T createBufferWriter(VertexBufferView buffer) {
        return this.createBufferWriter(buffer, true);
    }
}
