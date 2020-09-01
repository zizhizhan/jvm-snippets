package com.zizhizhan.legacies.crypto;

public abstract class CRCUtil {

    private CRCUtil() {

    }

    /**
     * Calculate crc for the given array of bytes
     *
     * @param bArray input byte array
     * @return crc
     */
    public static long getCrc(byte[] bArray) {
        CRC32 crc = new CRC32();

        crc.update(bArray, 0, bArray.length);
        long hash = crc.getValue();
        // Drop the higher order 32 bits, which may have been set because of sign extension
        hash = hash & 0x00ffffffffL;
        return hash;
    }

}


