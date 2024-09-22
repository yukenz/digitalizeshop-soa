package id.co.awan.digitalizeshopsoa.util;


import io.jsonwebtoken.impl.compression.AbstractCompressionAlgorithm;
import io.jsonwebtoken.io.CompressionAlgorithm;
import net.jpountz.lz4.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class LZ4CompressionAlgorithm extends AbstractCompressionAlgorithm {

    LZ4Factory factory = LZ4Factory.fastestInstance();

    public LZ4CompressionAlgorithm() {
        super("LZ4");
    }

//    @Override
//    public OutputStream compress(OutputStream out) {
//        LZ4Compressor compressor = factory.fastCompressor();
//        return new LZ4BlockOutputStream(out, 64 * 1024, compressor);
//    }

    @Override
    protected OutputStream doCompress(OutputStream outputStream) throws IOException {
        LZ4Compressor compressor = factory.fastCompressor();
        return new LZ4BlockOutputStream(outputStream, 64 * 1024, compressor);
    }

//    @Override
//    public InputStream decompress(InputStream in) {
//        LZ4FastDecompressor lz4FastDecompressor = factory.fastDecompressor();
//        return new LZ4BlockInputStream(in, lz4FastDecompressor);
//    }

    @Override
    protected InputStream doDecompress(InputStream inputStream) throws IOException {
        LZ4FastDecompressor lz4FastDecompressor = factory.fastDecompressor();
        return new LZ4BlockInputStream(inputStream, lz4FastDecompressor);
    }

//    @Override
//    public String getId() {
//        return "LZ4";
//    }
}