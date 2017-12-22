package com.bingo.bingoframe.ui;

/**
 * @author bingo.
 * @date Create on 2017/12/21.
 * @Description
 */

public interface PermissionCallback {

    /**
     * 权限已授予
     */
    void permissionsGranted();

    /**
     * 权限未授予，可以显示申请权限框
     */
    void shouldShowRequestPermissions();

    /**
     * 权限未授予，不在显示申请框，去设置打开
     */
    void openPermissionsInSetting();

}
