package com.mmenshikov;

import org.freedesktop.gstreamer.*;
import org.freedesktop.gstreamer.elements.AppSrc;

import java.nio.ByteBuffer;

public class GStreamerOutput {
    private final Pipeline pipeline;
    private final Object sync = new Object();

    public  GStreamerOutput()
    {
        Gst.init();
        pipeline = (Pipeline) Gst.parseLaunch("appsrc name=src ! video/x-h264 ! queue  ! decodebin ! videoconvert ! autovideosink");

        AppSrc source = (AppSrc) pipeline.getElementByName("src");
        source.set("emit-signals", true);

        source.connect((AppSrc.NEED_DATA) (element, size) ->
        {
         /*   synchronized (sync) {

                if (bytes == null)
                    bytes = new byte[size];

                Buffer buf = new Buffer(bytes.length);
                buf.map(true).put(ByteBuffer.wrap(bytes));
                System.out.println("gst " + bytes.length);
                element.pushBuffer(buf);
                buf.unmap();
            }*/

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
            this.bytes = bytes;
        }
    }
}
