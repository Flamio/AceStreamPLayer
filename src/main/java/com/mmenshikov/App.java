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
                aceStreamSource.startReading("dd1e67078381739d14beca697356ab76d49d1a2d");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        gStreamerOutput.start();
    }
}
