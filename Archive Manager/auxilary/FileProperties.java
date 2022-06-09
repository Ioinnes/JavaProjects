package auxilary;

public class FileProperties {
    private String name;
    private long size;
    private long compressedSize;
    private int compressionMethod;


    public FileProperties(String filename, long size, long compressedSize, int compressionMethod) {
        this.name = filename;
        this.size = size;
        this.compressedSize = compressedSize;
        this.compressionMethod = compressionMethod;
    }

    public String getName() {
        return this.name;
    }

    public long getSize() {
        return this.size;
    }

    public long getCompressedSize() {
        return this.compressedSize;
    }

    public int getCompressionMethod() {
        return this.compressionMethod;
    }

    public long getCompressionRatio() {
        return 100 - ((compressedSize * 100) / size);
    }

    @Override
    public String toString() {
        if (size > 0)
            return this.name + " " + this.size / 1024 + " Kb (" + this.compressedSize / 1024 + " Kb) сжатие: " + this.getCompressionRatio() + "%" ;
        else
            return this.name;
    }
}
