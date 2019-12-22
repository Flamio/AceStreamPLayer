package com.mmenshikov;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

public class AceStreamSource {

    public void startReading(String id) throws IOException {
        URL website = new URL(String.format("http://127.0.0.1:6878/ace/getstream?id=%s", id));
        ReadableByteChannel channel = Channels.newChannel(website.openStream());

        ByteBuffer buffer = ByteBuffer.allocate(1000000);
        ByteBuffer buffer2 = ByteBuffer.allocate(5000000);

        int readed = 0;
        int allReaded = 0;
        int i = 0;
        while (true) {
            readed = channel.read(buffer);
            if (readed != -1) {
                if ( buffer2.capacity() < allReaded + readed)
                {
                    buffer2.position(0);
                    buffer2.clear();
                    allReaded = 0;

                    if (streamListener != null)
                        streamListener.newChunk(buffer2.array());
                    System.out.println("ace " + buffer2.capacity());

                   // FileUtils.writeByteArrayToFile(new File(i + ".mp4"), buffer2.array());
                    i++;
                }
                buffer2.put(Arrays.copyOfRange(buffer.array(), 0,readed));
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
