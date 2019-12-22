package com.mmenshikov;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.AppSrc;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class GStreamerOutput {
    private final Pipeline pipeline;
    private final Object sync = new Object();
    private volatile boolean isNew = false;

    public  GStreamerOutput()
    {
        Gst.init();
        pipeline = (Pipeline) Gst.parseLaunch("appsrc name=src ! video/mpegts ! tsdemux name=demuxer demuxer. " +
                "! queue ! h264parse ! avdec_h264 ! vaapisink demuxer. " +
                "! queue ! aacparse ! avdec_aac ! autoaudiosink");

        AppSrc source = (AppSrc) pipeline.getElementByName("src");
        source.set("emit-signals", true);

        byte[] emptyBytes = new byte[1];

        source.connect((AppSrc.NEED_DATA) (element, size) ->
        {
            byte[] buffer = null;
            synchronized (sync) {
                if (!isNew)
                    buffer = emptyBytes;
                else {
                    if (this.bytes != null)
                        buffer = Arrays.copyOf(this.bytes, this.bytes.length);
                    else
                        buffer = emptyBytes;
                    isNew = false;
                }
            }

                Buffer buf = new Buffer(buffer.length);
                buf.map(true).put(ByteBuffer.wrap(buffer));
                System.out.println("gst " + buffer.length);
                element.pushBuffer(buf);

              //  buf.unmap();

        });

        Bus bus = pipeline.getBus();
        bus.connect((Bus.ERROR)(src, code, message) -> {
            System.out.println("Error detected");
            System.out.println("Error source: " + source.getName());
            System.out.println("Error code: " + code);
            System.out.println("Message: " + message);
          //  Gst.quit();
        });


        bus.connect((Bus.EOS) (s) -> {
            System.out.println("Received the EOS on the playbin!!!");
        });

        /*bus.connect((Bus.WARNING)(src, code, message) -> {
            System.out.println("War detected");
            System.out.println("War source: " + source.getName());
            System.out.println("War code: " + code);
            System.out.println("Message: " + message);

        });*/
    }

    public void start() {
        pipeline.play();
        Gst.main();
    }

    private byte[] bytes;

    public void setBytes(byte[] bytes) {
        synchronized (sync) {
            if (isNew)
                return;

            this.bytes = Arrays.copyOf(bytes, bytes.length);
            this.isNew = true;
        }
    }
}
