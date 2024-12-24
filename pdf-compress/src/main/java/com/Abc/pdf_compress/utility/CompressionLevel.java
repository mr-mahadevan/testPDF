package com.Abc.pdf_compress.utility;

public enum CompressionLevel {
    BASIC(0.7f, 0.9f),
    STANDARD(0.5f, 0.75f),
    HIGH(0.3f, 0.5f);

    private final float scaleFactor;
    private final float quality;

    CompressionLevel(float scaleFactor, float quality) {
        this.scaleFactor = scaleFactor;
        this.quality = quality;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public float getQuality() {
        return quality;
    }
}

