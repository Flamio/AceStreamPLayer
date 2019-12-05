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

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024*50);

        long readed = 0;
        while (readed != -1) {
            readed = channel.read(buffer);
            if (streamListener != null)
                streamListener.newChunk(buffer.array());
            System.out.println("ace " + buffer.array().length);
            buffer.clear();
        }

    }

    public void setStreamListener(StreamListener streamListener) {
        this.streamListener = streamListener;
    }

    private StreamListener streamListener = null;
}
