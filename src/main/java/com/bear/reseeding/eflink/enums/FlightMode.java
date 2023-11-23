package com.bear.reseeding.eflink.enums;

import java.util.Arrays;
import java.util.Optional;

public enum FlightMode {
    MANUAL(0),
    ATTI(1),
    ATTI_COURSE_LOCK(2),
    ATTI_HOVER(3),
    HOVER(4),
    GPS_BLAKE(5),
    GPS_ATTI(6),
    GPS_COURSE_LOCK(7),
    GPS_HOME_LOCK(8),
    GPS_HOT_POINT(9),
    ASSISTED_TAKEOFF(10),
    AUTO_TAKEOFF(11),
    AUTO_LANDING(12),
    ATTI_LANDING(13),
    GPS_WAYPOINT(14),
    GO_HOME(15),
    CLICK_GO(16),
    JOYSTICK(17),
    GPS_ATTI_WRISTBAND(18),
    CINEMATIC(19),
    ATTI_LIMITED(23),
    DRAW(24),
    GPS_FOLLOW_ME(25),
    ACTIVE_TRACK(26),
    TAP_FLY(27),
    PANO(28),
    FARMING(29),
    FPV(30),
    GPS_SPORT(31),
    GPS_NOVICE(32),
    CONFIRM_LANDING(33),
    TERRAIN_FOLLOW(35),
    PALM_CONTROL(36),
    QUICK_SHOT(37),

    TRIPOD(38),
    TRACK_SPOTLIGHT(39),
    MOTORS_JUST_STARTED(41),
    DETOUR(43),
    TIME_LAPSE(46),
    POI2(50),
    OMNI_MOVING(49),
    ADSB_AVOIDING(49),


    /*** 对应开源飞控的模式 start  ***/
    STABILIZE(150),
    ACRO(151),
    ALT_HOLD(152),
    AUTO(153),
    GUIDED(154),
    LOITER(155),
    RTL(156),
    SmartRTL(170),
    CIRCLE(157),
    POSITION(158),
    PosHold(166),
    LAND(159),
    OF_LOITER(160),
    APPROACH(161),
    /*** 对应开源飞控的模式 end  ***/

    UNDEFINE(-1),
    UNKNOWN(255);

    private static volatile FlightMode[] flightModes;
    private int data;

    private FlightMode(int var3) {
        data = var3;
    }

    public int getKey() {
        return data;
    }

    public static FlightMode valueOf(int value) {   //   手写的从int到enum的转换函数
        switch (value) {
            case 1:
                return ATTI;
            case 2:
                return ATTI_COURSE_LOCK;
            default:
                return null;
        }
    }

    public static FlightMode valueIndexOf(int index) {   //   手写的从int到enum的转换函数
        try {
            return FlightMode.values()[index];
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    public static FlightMode find(int key) {
        Optional<FlightMode> optional = Arrays.stream(FlightMode.values())
                .filter(p -> p.getKey() == key)
                .findFirst();
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return UNKNOWN;
        }
    }

    public int value() {
        return 0;
    }

    public boolean _equals(int var1) {
        return false;
    }
}

