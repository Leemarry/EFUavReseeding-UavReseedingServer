package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 删除媒体文件回复(#3210)
 */
public class EFLINK_MSG_3210 implements Serializable {
    public final int EFLINK_MSG_ID = 3210;

    /**
     * 内容段数据长度
     */
    public final int EFLINK_MSG_LENGTH = 2;

    int VersionInside;
    int Tag;
    List<String> MediasName = new ArrayList<>();


    public EFLINK_MSG_3210() {
    }

    public EFLINK_MSG_3210(int versionInside, int tag) {
        VersionInside = versionInside;
        Tag = tag;
    }

    public void InitPacket(byte[] packet) {
        InitPacket(packet, 12);
    }

    public EFLINK_MSG_3210(byte[] packet) {
        InitPacket(packet, 0);
    }

    public EFLINK_MSG_3210(byte[] packet, int index) {
        InitPacket(packet, index);
    }

    public void InitPacket(byte[] packet, int index) {
        VersionInside = packet[index] & 0xFF;  //当前载荷段数据版本，协议升级后可根据版本号区分载荷段内容。
        index++;
        Tag = packet[index] & 0xFF; //标记
        index++;
        while (index < packet.length - 2) {
            try {
                byte type = packet[index]; //文件类型
                index++;
                int fileIndex = BytesUtil.bytes2Int(packet, index); //取文件索引
                index += 4;
                long time = BytesUtil.bytes2Long(packet, index); //取文件名时间戳
                index += 8;
                String suffix = "";
                if (type == 0) {
                    suffix = ".jpeg";
                } else if (type == 1) {
                    suffix = ".raw_dng";
                } else if (type == 2) {
                    suffix = ".mov";
                } else if (type == 3) {
                    suffix = ".mp4";
                } else if (type == 4) {
                    suffix = ".panorama";
                } else if (type == 5) {
                    suffix = ".shallow_focus";
                } else if (type == 6) {
                    suffix = ".tiff";
                } else if (type == 7) {
                    suffix = ".seq";
                } else if (type == 8) {
                    suffix = ".tiff_seq";
                } else if (type == 9) {
                    suffix = ".json";
                } else if (type == 10) {
                    suffix = ".photo_folder";
                } else if (type == 11) {
                    suffix = ".video_folder";
                } else if (type == 12) {
                    suffix = ".unknown";
                }
                MediasName.add("dji" + fileIndex + "_" + time + suffix);
            } catch (Exception e) {
            }
        }
    }


    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(EFLINK_MSG_LENGTH + MediasName.size() * 13);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) (VersionInside));
        buffer.put((byte) (Tag));
        for (int i = 0; i < MediasName.size(); i++) {
            try {
                String name = MediasName.get(i); // 123.png
                String type = name.split("\\.")[1];
                if (name.startsWith("dji")) {
                    name = name.replace("dji", "");
                }
                int fileIndex = Integer.parseInt(name.split("\\.")[0].split("\\_")[0]);
                String time = (name.split("\\.")[0]).split("\\_")[1];
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                buffer.put((BytesUtil.long2Bytes(dateFormat.parse(time).getTime())));
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

    public List<String> getMediasName() {
        return MediasName;
    }

    public void setMediasName(List<String> mediasName) {
        MediasName = mediasName;
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_3210{" +
                "EFLINK_MSG_ID=" + EFLINK_MSG_ID +
                ", EFLINK_MSG_LENGTH=" + EFLINK_MSG_LENGTH +
                ", VersionInside=" + VersionInside +
                ", Tag=" + Tag +
                ", MediasName=" + MediasName +
                '}';
    }
}
