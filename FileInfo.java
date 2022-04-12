package ru.corruptzero;

public class FileInfo {
    private String filename;
    private String extension;
    private final long size;

    public FileInfo(String filename, String extension, long size) {
        this.filename = filename;
        this.extension = extension;
        this.size = size;
    }

    @Override
    public String toString() {
        return "File{" +
                "filename='" + filename + '\'' +
                ", extension='" + extension + '\'' +
                ", size=" + size/1024 + "KB " +
                '}';
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

}
