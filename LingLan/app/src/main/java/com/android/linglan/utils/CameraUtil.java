package com.android.linglan.utils;

import android.hardware.Camera;

/**
 * Created by Administrator on 2016/1/8 0008.
 */
public class CameraUtil {
    public static boolean isCameraEnable() {
        Camera camera;
        // Safe camera open for checking camera function.
        try {
            camera = Camera.open();

            if (camera != null) {
                camera.release();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }
}
