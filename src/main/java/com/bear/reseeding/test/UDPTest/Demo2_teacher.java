package com.bear.reseeding.test.UDPTest;

/**
 *
 * 实现相互交流 （老师与学生的交流）
 *
 * @author 86155
 *
 */
public class Demo2_teacher {
    public static void main(String[] args) {


        new Thread(new Demo12_talk发送02(8888, 9998, "localhost")).start();//发送

        new Thread(new Demo11_talk接收端02(9997)).start();//接受
    }
}
