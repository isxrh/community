package com.nowcoder.community;


import java.io.IOException;

public class WkTests {

    public static void main(String[] args) {
        String cmd = "D:/ProgramFiles/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://programmercarl.com  D:/MyDocs/JavaProject/newcoderProject/data/wk-images/1.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
