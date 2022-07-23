package me.noryea.dma.client.vertex;

import me.noryea.dma.client.vertex.type.VertexType;
import net.minecraft.client.render.VertexConsumer;

/**
 * A drain allows the instantiation of {@link VertexSink} and is implemented on outputs which take vertex data.
 */
public interface VertexDrain {

    static VertexDrain of(VertexConsumer consumer) {
        return (VertexDrain) consumer;
    }

    <T extends VertexSink> T createSink(VertexType<T> factory);
}
