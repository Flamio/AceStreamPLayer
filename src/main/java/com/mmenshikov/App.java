package com.mmenshikov;

import org.freedesktop.gstreamer.Gst;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GStreamerOutput gStreamerOutput = new GStreamerOutput();
        Thread thread = new Thread(() -> {
            AceStreamSource aceStreamSource = null;
            aceStreamSource = new AceStreamSource();

            aceStreamSource.setStreamListener((bytes) ->{
                gStreamerOutput.setBytes(bytes);
            });

            try {
                //955bc653f090c238b8d933b41d8a66fee1bf7893
                //aceStreamSource.startReading("dd1e67078381739d14beca697356ab76d49d1a2d");
                //acestream://18699df05bd2b99c37b2d827a6206a04b0b9c891
                aceStreamSource.startReading("dd1e67078381739d14beca697356ab76d49d1a2d");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        gStreamerOutput.start();
    }
}
