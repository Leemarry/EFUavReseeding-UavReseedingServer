package com.bear.reseeding.eflink;

import com.bear.reseeding.utils.BytesUtil;
import com.bear.reseeding.utils.LogUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 停机坪气象站数据心跳包
 *
 * @Auther: bear
 * @Date: 2021/12/30 18:20
 * @Description: null
 */
public class EFLINK_MSG_2350 implements Serializable {

    public final int EFLINK_MSG_ID = 2350;

    private long hiveId;
    private Timestamp dataDate;
    private double windSpeed;
    private long windPower;
    private String windPowerText;
    private double windDirection;
    private double airTemp;
    private double airHumidity;
    private long light;
    private double pm1;
    private double pm2;
    private int rainSnow;

    public int getRainSnow() {
        return rainSnow;
    }

    public void setRainSnow(int rainSnow) {
        this.rainSnow = rainSnow;
    }

    public double getRainWater() {
        return rainWater;
    }

    public void setRainWater(double rainWater) {
        this.rainWater = rainWater;
    }

    private double rainWater;


    public long getHiveId() {
        return hiveId;
    }

    public void setHiveId(long hiveId) {
        this.hiveId = hiveId;
    }


    public Timestamp getDataDate() {
        return dataDate;
    }

    public void setDataDate(Timestamp dataDate) {
        this.dataDate = dataDate;
    }


    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }


    public long getWindPower() {
        return windPower;
    }

    public void setWindPower(long windPower) {
        this.windPower = windPower;
    }

    public void setWindPowerText(String windPowerText) {
        this.windPowerText = windPowerText;
    }

    public double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(double windDirection) {
        this.windDirection = windDirection;
    }


    public double getAirTemp() {
        return airTemp;
    }

    public void setAirTemp(double airTemp) {
        this.airTemp = airTemp;
    }


    public double getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(double airHumidity) {
        this.airHumidity = airHumidity;
    }


    public long getLight() {
        return light;
    }

    public void setLight(long light) {
        this.light = light;
    }


    public double getPm1() {
        return pm1;
    }

    public void setPm1(double pm1) {
        this.pm1 = pm1;
    }

    @Override
    public String toString() {
        return "EfHiveWeatherstation{" +
                "hiveId=" + hiveId +
                ", dataDate=" + dataDate +
                ", windSpeed=" + windSpeed +
                ", windPower=" + windPower +
                ", windDirection=" + windDirection +
                ", airTemp=" + airTemp +
                ", airHumidity=" + airHumidity +
                ", light=" + light +
                ", pm1=" + pm1 +
                ", pm2=" + pm2 +
                ", rainSnow=" + rainSnow +
                ", rainWater=" + rainWater +
                '}';
    }

    public double getPm2() {
        return pm2;
    }

    public void setPm2(double pm2) {
        this.pm2 = pm2;
    }

    public void InitPacket(byte[] packet, int Index) {
        try {
            Timestamp time1 = new Timestamp(System.currentTimeMillis());//获取系统当前时间
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timeStr1 = df1.format(time1);
            dataDate = (Timestamp.valueOf(timeStr1));

            hiveId = (BytesUtil.bytes2UShort(packet, Index)); // 蜂巢编号
            Index += 2;
            // 指令类型
            Index += 1;
            windSpeed = BytesUtil.bytes2UShort(packet, Index) / 10d; // 风速 0.01m/s
            Index += 2;
            windPower = (packet[Index] & 0xFF); // 风力等级 0-12级
            if (windPower == 0 && windSpeed > 0) {
                windPower = getWindPowerBySpeed();
            }
            windPowerText = getWindPowerText() + "(" + windPower + ")";
            Index += 1;
            int dir = BytesUtil.bytes2UShort(packet, Index);
            windDirection = dir / 10d; // 风向 0.01 0-360度
//            LogUtil.logMessage("风向值：" + dir/10);
//            LogUtil.logMessage("风向原始值 无符号：" + BytesUtil.bytes2UShort(packet, Index));
//            LogUtil.logMessage("风向原始值 无符号，弧度转为度：" + BytesUtil.bytes2UShort(packet, Index) * (180d / Math.PI));
//            LogUtil.logMessage("风向原始值 有符号：" + BytesUtil.bytes2Short(packet, Index));
//            LogUtil.logMessage("风向原始值 无符号，弧度转为度：" + BytesUtil.bytes2Short(packet, Index) * (180d / Math.PI));
            Index += 2;
            setAirTemp(BytesUtil.bytes2UShort(packet, Index) / 10d); // 环境温度 0.1 单位度℃
            Index += 2;
            setAirHumidity(BytesUtil.bytes2UShort(packet, Index) / 10d); // 环境湿度 0.1 %RH
            Index += 2;
            setLight(BytesUtil.bytes2UInt(packet, Index)); // 环境光照 单位Lux 0-20万Lux
            Index += 4;
            pm1 = (BytesUtil.bytes2UShort(packet, Index)); // PM1.0
            Index += 2;
            pm2 = (BytesUtil.bytes2UShort(packet, Index)); // PM2.5
            Index += 2;
            rainSnow = packet[Index] & 0xFF; //  雨雪 0 无雨雪 1 有雨雪
            Index += 1;
            rainWater = BytesUtil.bytes2UShort(packet, Index) / 10d; //  降雨量 0.1毫米
            Index += 2;
        } catch (Exception ignored) {
        }
    }

    public int getWindPowerBySpeed() {
        if (windSpeed < 0.3) {
            return 0;
        } else if (windSpeed <= 1.5) {
            return 1;
        } else if (windSpeed <= 3.3) {
            return 2;
        } else if (windSpeed <= 5.4) {
            return 3;
        } else if (windSpeed <= 7.9) {
            return 4;
        } else if (windSpeed <= 10.7) {
            return 5;
        } else if (windSpeed <= 13.8) {
            return 6;
        } else if (windSpeed <= 17.1) {
            return 7;
        } else if (windSpeed <= 20.7) {
            return 8;
        } else if (windSpeed <= 24.4) {
            return 9;
        } else if (windSpeed <= 28.4) {
            return 10;
        } else if (windSpeed <= 32.6) {
            return 11;
        } else if (windSpeed <= 36.9) {
            return 12;
        } else {
            return 13;
        }
    }

    public String getWindPowerText() {
        if (windPower == 0) {
            return "无风";
        } else if (windPower == 1) {
            return "软风";
        } else if (windPower == 2) {
            return "轻风";
        } else if (windPower == 3) {
            return "微风";
        } else if (windPower == 4) {
            return "和风";
        } else if (windPower == 5) {
            return "清劲风";
        } else if (windPower == 6) {
            return "强风";
        } else if (windPower == 7) {
            return "疾风";
        } else if (windPower == 8) {
            return "大风";
        } else if (windPower == 9) {
            return "烈风";
        } else if (windPower == 10) {
            return "狂风";
        } else if (windPower == 11) {
            return "暴风";
        } else if (windPower == 12) {
            return "台风";
        } else {
            return "飓风";
        }
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(16 + 9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
//        buffer.put((byte) (IsGcsOnline));
//        buffer.put((byte) (IsUavOnline));
//        buffer.put((byte) (IsRomoteControlOnline));
//        buffer.put((byte) (IsAirLinkOnline));
//        buffer.put((byte) (IsCameraOnline));
//        buffer.put(BytesUtil.int2Bytes((int) BootTime));
        return buffer.array();
    }
}
