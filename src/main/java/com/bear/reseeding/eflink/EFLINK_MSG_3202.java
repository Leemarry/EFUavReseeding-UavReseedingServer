package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 从无人机获取媒体文件信息回复(#3202)
 */
public class EFLINK_MSG_3202 implements Serializable {
    private final int EFLINK_MSG_ID = 3202;

    /**
     * 内容段数据长度
     */
    private final int EFLINK_MSG_LENGTH = 2;

    int VersionInside;
    int Tag;
    List<EFLINK_MSG_3202_2> MediasName = new ArrayList<>();

    public EFLINK_MSG_3202() {
    }

    public EFLINK_MSG_3202(int versionInside, int tag, int mediasName) {
        VersionInside = versionInside;
        Tag = tag;
    }

    public void InitPacket(byte[] packet) {
        InitPacket(packet, 12);
    }

    public EFLINK_MSG_3202(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3202(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        MediasName.clear();
        while (index < packet.length - 2) {
            try {
                int type = packet[index] & 0xFF; //文件类型
                index++;
                int fileIndex = BytesUtil.bytes2Int(packet, index); //取文件索引
                index += 4;
                int size = BytesUtil.bytes2Int(packet, index); //取文件大小
                index += 4;
                long time = BytesUtil.bytes2Long(packet, index); //取文件名时间戳
                index += 8;
                byte[] bytes = new byte[20];
                for (int i = 0; i < 20; i++) {
                    bytes[i] = packet[index];
                    index++;
                }
                String mediaTypeDisplayName = new String(bytes, StandardCharsets.UTF_8).trim();
                MediasName.add(new EFLINK_MSG_3202_2(time, size, (int) type, fileIndex, mediaTypeDisplayName));
            } catch (Exception e) {
            }
        }
    }


    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH + MediasName.size() * 37);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        for (int i = 0; i < MediasName.size(); i++) {
            try {
                EFLINK_MSG_3202_2 eflink_msg_3202_2 = MediasName.get(i);// 123.png
                int fileType = eflink_msg_3202_2.getFileType();
                String type = String.valueOf(fileType);
                int fileSize = eflink_msg_3202_2.getFileSize();
                long fileTime = eflink_msg_3202_2.getFileTime();
                int fileIndex = eflink_msg_3202_2.getFileIndex();
                String mediaTypeDisplayName = eflink_msg_3202_2.getMediaTypeDisplayName();
                byte typeB = 0;
                if (type.equals("jpeg")) {
                    typeB = 0;
                } else if (type.equals("raw_dng")) {
                    typeB = 1;
                } else if (type.equals("mov")) {
                    typeB = 2;
                } else if (type.equals("mp4")) {
                    typeB = 3;
                } else if (type.equals("panorama")) {
                    typeB = 4;
                } else if (type.equals("shallow_focus")) {
                    typeB = 5;
                } else if (type.equals("tiff")) {
                    typeB = 6;
                } else if (type.equals("seq")) {
                    typeB = 7;
                } else if (type.equals("tiff_seq")) {
                    typeB = 8;
                } else if (type.equals("json")) {
                    typeB = 9;
                } else if (type.equals("photo_folder")) {
                    typeB = 10;
                } else if (type.equals("video_folder")) {
                    typeB = 11;
                } else if (type.equals("unknown")) {
                    typeB = 12;
                }
                buffer.put((byte) (typeB));
                buffer.put(BytesUtil.int2Bytes(fileIndex));
                buffer.put(BytesUtil.int2Bytes(fileSize));
                buffer.put((BytesUtil.long2Bytes(fileTime)));
                byte[] bytes = new byte[20];
                if (mediaTypeDisplayName != null) {
                    byte[] temp = mediaTypeDisplayName.getBytes(StandardCharsets.UTF_8);
                    int tempLengh = temp.length;
                    for (int j = 0; j < 20; j++) {
                        if (j < tempLengh) {
                            buffer.put(temp[j]);
                            bytes[j] = temp[j];
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return buffer.array();
    }


    public String toJsonArray() {
        return JSONObject.toJSONString(this);
    }

    public int getVersionInside() {
        return VersionInside;
    }

    public void setVersionInside(int versionInside) {
        VersionInside = versionInside;
    }

    public int getTag() {
        return Tag;
    }

    public void setTag(int tag) {
        Tag = tag;
    }

    public List<EFLINK_MSG_3202_2> getMediasName() {
        return MediasName;
    }

    public void setMediasName(List<EFLINK_MSG_3202_2> mediasName) {
        MediasName = mediasName;
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_3202{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", VersionInside=" + VersionInside +
                ", Tag=" + Tag +
                ", MediasName=" + MediasName +
                '}';
    }
}
