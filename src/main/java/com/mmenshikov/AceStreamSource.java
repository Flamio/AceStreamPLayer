package com.mmenshikov;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AceStreamSource {

    public void startReading(String id) throws IOException {
        URL website = new URL(String.format("http://127.0.0.1:6878/ace/getstream?id=%s", id));
        ReadableByteChannel channel = Channels.newChannel(website.openStream());

        ByteBuffer buffer = ByteBuffer.allocate(1024*1024);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024 * 1024*2);

        int readed = 0;
        int allReaded = 0;
        while (true) {
            readed = channel.read(buffer);
            if (readed != -1) {
                if (allReaded + readed > buffer2.capacity())
                {
                    if (streamListener != null)
                        streamListener.newChunk(buffer2.array());

                    allReaded = 0;
                    buffer2.clear();
                    System.out.println("ace " + readed);
                }
                buffer2.put(buffer);
                allReaded+=readed;
            }
            buffer.clear();
        }

    }

    public void setStreamListener(StreamListener streamListener) {
        this.streamListener = streamListener;
    }

    private StreamListener streamListener = null;
}
