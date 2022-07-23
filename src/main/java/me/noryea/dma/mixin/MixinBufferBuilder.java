package me.noryea.dma.mixin;

import me.noryea.dma.client.gl.attribute.BufferVertexFormat;
import me.noryea.dma.client.vertex.VertexDrain;
import me.noryea.dma.client.vertex.VertexSink;
import me.noryea.dma.client.vertex.buffer.VertexBufferView;
import me.noryea.dma.client.vertex.type.BlittableVertexType;
import me.noryea.dma.client.vertex.type.VertexType;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.util.GlAllocationUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements VertexBufferView, VertexDrain {
    @Shadow
    private int elementOffset;

    @Shadow
    private ByteBuffer buffer;

    @Shadow
    private static int roundBufferSize(int amount) {
        throw new UnsupportedOperationException();
    }

    @Shadow
    private VertexFormat format;

    @Shadow
    private int vertexCount;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Override
    public boolean ensureBufferCapacity(int bytes) {
        // Ensure that there is always space for 1 more vertex; see BufferBuilder.next()
        bytes += format.getVertexSize();

        if (this.elementOffset + bytes <= this.buffer.capacity()) {
            return false;
        }

        int newSize = this.buffer.capacity() + roundBufferSize(bytes);

        LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", this.buffer.capacity(), newSize);

        this.buffer.position(0);

        ByteBuffer byteBuffer = GlAllocationUtils.allocateByteBuffer(newSize);
        byteBuffer.put(this.buffer);
        byteBuffer.rewind();

        this.buffer = byteBuffer;

        return true;
    }

    @Override
    public ByteBuffer getDirectBuffer() {
        return this.buffer;
    }

    @Override
    public int getWriterPosition() {
        return this.elementOffset;
    }

    @Override
    public BufferVertexFormat getVertexFormat() {
        return BufferVertexFormat.from(this.format);
    }

    @Override
    public void flush(int vertexCount, BufferVertexFormat format) {
        if (BufferVertexFormat.from(this.format) != format) {
            throw new IllegalStateException("Mis-matched vertex format (expected: [" + format + "], currently using: [" + this.format + "])");
        }

        this.vertexCount += vertexCount;
        this.elementOffset += vertexCount * format.getStride();
    }

    @Override
    public <T extends VertexSink> T createSink(VertexType<T> factory) {
        BlittableVertexType<T> blittable = factory.asBlittable();

        if (blittable != null && blittable.getBufferVertexFormat() == this.getVertexFormat())  {
            return blittable.createBufferWriter(this, true);
        }

        return factory.createFallbackWriter((VertexConsumer) this);
    }
}
