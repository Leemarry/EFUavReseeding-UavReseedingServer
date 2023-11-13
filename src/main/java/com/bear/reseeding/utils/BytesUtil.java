package com.bear.reseeding.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @Auther: bear
 * @Date: 2021/7/16 09:50
 * @Description: null
 */
public class BytesUtil {
    static ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();


    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

      /**
     * InputStream转Byte数组
     *
     * @param inputStream 输入流
     * @return byte数组
     * @throws IOException
     */
    public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        return outputStream.toByteArray();
    }

    public static byte[] short2Bytes(int x) {
        return short2Bytes((short) x);
    }

    public static byte[] short2Bytes(short x) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(byteOrder);
        buffer.putShort(x);
        return buffer.array();
    }

    public static byte[] int2Bytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(byteOrder);
        buffer.putInt(x);
        return buffer.array();
    }

    public static byte[] float2Bytes(double x) {
        return float2Bytes((float) x);
    }

    public static byte[] float2Bytes(float f) {
       /* ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(byteOrder);
        buffer.putFloat(x);
        return buffer.array();*/
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    public static byte[] long2Bytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(byteOrder);
        buffer.putLong(x);
        return buffer.array();
    }

    public static short get2Bytes(byte[] from, int fromIndex) {
        int high = from[fromIndex] & 0xff;
        int low = from[fromIndex + 1] & 0xff;
        return (short) (high << 8 + low);
    }


    /**
     * 取2字节转换为uint16，用int表示
     */
    public static int bytes2UShort(byte[] src, int index) {
        short value = bytes2Short(src, index);
        return Short.toUnsignedInt(value);
    }

    public static short bytes2Short(byte[] src, int index) {
        if (src.length <= index + 1) {
            return 0;
        }
        byte[] temp = new byte[2];
        temp[0] = src[index];
        temp[1] = src[index + 1];
        return bytes2Short(temp);
    }

    public static short bytes2Short(byte[] src) {
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getShort();
    }


    /**
     * 取4字节转换为uint32，用long表示
     */
    public static long bytes2UInt(byte[] src, int index) {
        int value = bytes2Int(src, index);
        return Integer.toUnsignedLong(value);
    }

    public static int bytes2Int(byte[] src, int index) {
        if (src.length <= index + 3) {
            return 0;
        }
        byte[] temp = new byte[4];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = src[index + i];
        }
        return bytes2Int(temp);
    }

    public static int bytes2Int(byte[] src) {
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getInt();
    }

    public static float bytes2Float(byte[] b, int index) {
        /*if (src.length <= index + 3) {
            return 0;
        }
        byte[] temp = new byte[4];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = src[index + i];
        }
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getFloat();*/
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public static float getFloat(byte[] src, int index) {
        // 4 bytes
        int accum = 0;
        int shiftBy = index;
        for (shiftBy = 0; shiftBy < 4; shiftBy++) {
            accum |= (src[shiftBy] & 0xff) << shiftBy * 8;
        }
        return Float.intBitsToFloat(accum);
    }

    /**
     * 取8字节转换为uint64，用string表示
     */
    public static String bytes2ULong(byte[] src, int index) {
        long value = bytes2Long(src, index);
        return Long.toUnsignedString(value);
    }

    public static long bytes2Long(byte[] src, int index) {
        if (src.length <= index + 7) {
            return 0;
        }
        byte[] temp = new byte[8];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = src[index + i];
        }
        return bytes2Long(temp);
    }

    public static long bytes2Long(byte[] src) {
        ByteBuffer buffer = ByteBuffer.wrap(src);
        buffer.order(byteOrder);
        return buffer.getLong();
    }
}
