package org.openhab.binding.daikinir.internal.api;

import java.io.FileWriter;
import java.io.IOException;

public class BroadlinkInterface {

    private int[] packet;
    private int index = 0;
    private int frameCount = 0;
    private int[] headerArray = { 0x26, 0x00, 0x50, 0x02, 0x09, 0x0d, 0x0e, 0x10, 0x0c, 0x10, 0x0d, 0x0f, 0x0d, 0x10,
            0x0c, 0x00, 0x03, 0x42, 0x70, 0x3a };

    public BroadlinkInterface() {
        packet = new int[604];
        index = headerArray.length;
        frameCount = 0;
    }

    public void AddFrame(int[] frame) {

        int i;
        int bit;
        int tmpByte;
        int mask;

        for (i = 0; i < frame.length; i++) {

            tmpByte = frame[i];
            mask = 1;
            for (bit = 0; bit < 8; bit++) {

                if ((mask & tmpByte) != 0) {

                    packet[index] = 12;
                    packet[index + 1] = 44;
                } else {
                    packet[index] = 12;
                    packet[index + 1] = 13;
                }
                mask = mask << 1;
                index = index + 2;
            }
        }
        /* Add frame separarion */
        if ((frameCount < 2)) {

            int[] separation = { 0x0c, 0x00, 0x04, 0x77, 0x71, 0x39 };
            for (i = 0; i < separation.length; i++) {
                packet[index + i] = separation[i];
            }
            index = index + i;

            frameCount++;
        } else {

            packet[index] = 0x0c;
            packet[index + 1] = 0x00;
            packet[index + 2] = 0x0d;
            packet[index + 3] = 0x05;
        }
    }

    public void send() {

        for (index = 0; index < headerArray.length; index++) {
            packet[index] = headerArray[index];
        }

        try {
            FileWriter fileWriter = new FileWriter("/home/jordi/testOut.txt");
            fileWriter.write(BinaryUtils.bytesToStringNoSpace(packet));
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        index = 6;
        frameCount = 0;
    }

}
