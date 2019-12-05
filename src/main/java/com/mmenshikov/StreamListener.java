package com.mmenshikov;

@FunctionalInterface
public interface StreamListener {
    void newChunk(byte[] array);
}
