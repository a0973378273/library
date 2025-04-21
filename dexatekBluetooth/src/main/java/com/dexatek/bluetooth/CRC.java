package com.dexatek.bluetooth;


public class CRC {
    private static final int CRC_KERMIT_PRESET = 0x0000;
    private static final int CRC_KERMIT_POLYNOM = 0x8408;
    public static int crc16CCITT_Kermit(byte[] data) {
        int crc = CRC_KERMIT_PRESET;
        for (byte b : data) {
            crc = crc ^ (b & 0xFF);
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = (crc >>> 1) ^ CRC_KERMIT_POLYNOM;
                } else {
                    crc = (crc >>> 1);
                }
            }
        }
        return crc & 0xFFFF;
    }
}
