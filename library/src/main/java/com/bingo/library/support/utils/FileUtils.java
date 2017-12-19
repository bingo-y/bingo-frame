package com.bingo.library.support.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * 文件工具
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    /**
     * APP 的主存储上的目录
     */
    public enum AppDirTypeInMain {
        CACHE("cache"),
        USR("usr"),
        DB("db");
        String dir;

        AppDirTypeInMain(String dir) {
            this.dir = dir;
        }

        public String getDir() {
            return dir;
        }
    }

    /**
     * APP 在SD卡上的目录
     */
    public enum AppDirTypeInExt {
        CACHE("cache"),
        RXCACHE("rxcache"),
        IMAGE("image"),
        UPLOAD("upload"),
        PATCH("patch"),
        INSTALL("install");
        String dir;

        AppDirTypeInExt(String dir) {
            this.dir = dir;
        }

        public String getDir() {
            return dir;
        }
    }

    /**
     * 在主存上，用户私有的目录
     */
    public enum AppUsrDirType {
        DB("db");
        String dir;

        AppUsrDirType(String dir) {
            this.dir = dir;
        }

        public String getDir() {
            return dir;
        }
    }

    /**
     * 清空某个文件下所有的文件
     */
    public static void deleteDir(String filePath) {
        try {
            if (filePath == null) {
                return;
            }
            File file = new File(filePath);
            if (file.exists() && file.isDirectory()) {
                //若目录下没有文件则直接删除
                if (file.listFiles().length == 0) {
                    file.delete();
                } else {
                    //若有则把文件放进数组，并判断是否有下级目录
                    File delFile[] = file.listFiles();
                    int i = file.listFiles().length;
                    for (int j = 0; j < i; j++) {
                        if (delFile[j].isDirectory()) {
                            //递归调用del方法并取得子目录路径
                            deleteDir(delFile[j].getAbsolutePath());
                        }
                        //删除文件
                        delFile[j].delete();
                    }
                    file.delete();
                }
            }
        } catch (Exception e) {
            Timber.e(e, e.getMessage());
        }
    }


    /**
     * 获取APP的主目录，在主存卡
     */
    public static String getAppDirInMain(Context context, AppDirTypeInMain appDirTypeInMain) {
        return getAppFileInMain(context, appDirTypeInMain).getAbsolutePath();
    }

    public static File getAppFileInMain(Context context, AppDirTypeInMain appDirTypeInMain) {
        //创建 Main SD 卡上，关于该App的 ROOT 目录
        File appRootDir = context.getDir("bucket", Context.MODE_PRIVATE);
        if (!appRootDir.exists()) {
            appRootDir.mkdirs();
        }
        //创建具体的类型目录
        File dir = new File(appRootDir, appDirTypeInMain.getDir());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取用户的私有目录，在主存上
     */
    public static String getAppUsrDir(Context context, String usrId, AppUsrDirType appUsrDirType) {
        //检测USR_ID
        if (usrId == null) {
            throw new NullPointerException("USR ID IS NULL");
        }
        //创建私有用户目录
        File usrDir = new File(getAppDirInMain(context, AppDirTypeInMain.USR), usrId);
        if (!usrDir.exists())
            usrDir.mkdirs();
        //创建具体的类型目录
        File dir = new File(usrDir, appUsrDirType.getDir());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * 获取app sd 卡上的目录,  如果SD卡出问题, 则返回null
     */
    public static String getAppDirInExt(Context context, AppDirTypeInExt type) throws NotFoundExternalSD {
        return getAppFileInExt(context, type).getAbsolutePath();
    }

    public static File getAppFileInExt(Context context, AppDirTypeInExt type) throws NotFoundExternalSD {

        try {
            //如果没有挂载，或者没有写入外部磁盘权限，则抛出异常
            if (!"mounted".equals(Environment.getExternalStorageState())
                    || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                throw new RuntimeException("NO SD MOUNTED");
            }
            //外部存储路劲
            String externalStorePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            //如果找不到外部存储, 则抛出异常
            if (TextUtils.isEmpty(externalStorePath)) {
                throw new NullPointerException("externalStorePath is null");
            }
            //在SD卡上建立APP主目录
            File appDirInExt = new File(externalStorePath, context.getPackageName());
            if (!appDirInExt.exists()) {
                appDirInExt.mkdirs();
            }
            //创建具体的TYPE目录
            File dir = new File(appDirInExt, type.getDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        } catch (Exception e) {
            Timber.e(e, e.getMessage());
            throw new NotFoundExternalSD(e);
        }
    }

    /**
     * 获取缓存文件夹地址, 依次读取一下的目录
     * 1. 外部 app_home->cache目录
     * 2. 内部 app_home->cache目录
     */
    public static String getAppCacheDir(Context context) {
        //读取APP 在 SD卡上主要的CACHE目录
        try {
            return getAppDirInExt(context, AppDirTypeInExt.CACHE);
        } catch (NotFoundExternalSD e) {
            Timber.e(e, e.getMessage());
        }
        //读取 Android 在Main Store 上创建的CACHE目录
        return getAppDirInMain(context, AppDirTypeInMain.CACHE);
    }

    /**
     * 获取图片缓存路径
     * @param context
     * @return
     */
    public static File getAppImageFile(Context context) {
        //读取APP 在 SD卡上主要的CACHE目录
        try {
            return getAppFileInExt(context, AppDirTypeInExt.IMAGE);
        } catch (NotFoundExternalSD e) {
            Timber.e(e, e.getMessage());
        }
        //读取 Android 在Main Store 上创建的CACHE目录
        return getAppFileInMain(context, AppDirTypeInMain.CACHE);
    }

    /**
     * 获取下载路径
     * @param context
     * @return
     */
    public static File getAppDownloadFile(Context context) {
        //读取APP 在 SD卡上主要的Install目录
        try {
            return getAppFileInExt(context, AppDirTypeInExt.INSTALL);
        } catch (NotFoundExternalSD e) {
            Timber.e(e, e.getMessage());
        }
        //读取 Android 公共下载路径
        return getDefaultDownloadFile();
    }

    /**
     * 获取默认下载路径
     * @return
     */
    public static File getDefaultDownloadFile() {
        //读取 Android 公共下载路径
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获得下载保存默认地址
     */
    public static String getDefaultDownLoadPath(Context context) {
        try {
            if (existExternalSD(context)) {
                return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+ File.separator;
            }
        } catch (NotFoundExternalSD notFoundExternalSD) {
            notFoundExternalSD.printStackTrace();
            Timber.e(notFoundExternalSD);
            return "";
        }
        return "";
    }


    /**
     * 从url中，获得默认文件名
     */
    public static String getDefaultDownLoadFileName(String url) {
        if (url == null || url.length() == 0) return "";
        int nameStart = url.lastIndexOf('/')+1;
        return url.substring(nameStart);
    }

    public static String getFileNameWithTime(String url) {
        if (url == null || url.length() == 0) {
            return "";
        }
        int nameStart = url.lastIndexOf('/') + 1;
        String name = url.substring(nameStart);
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        int point = name.lastIndexOf('.');
        return name.substring(0, point) + System.currentTimeMillis() + name.substring(point);
    }

    public static boolean existExternalSD(Context context) throws NotFoundExternalSD {
        //如果没有挂载，或者没有写入外部磁盘权限，返回fasle
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            throw new NotFoundExternalSD(new Exception("NO SD CARD"));
        }
        return true;
    }

    /**
     * 没有外部存储
     */
    public static class NotFoundExternalSD extends Exception {
        public NotFoundExternalSD(Exception e) {
            super(e);
        }
    }

    /**
     * byte 转 file
     * @param bfile
     * @param filePath
     * @param fileName
     * @return
     */
    public static File byte2File(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists() && dir.isDirectory()){
                //判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 从assets copy 到sdcard中
     * @param context
     * @param fileName
     */
    public static String copyFromAssets(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        File file = new File(context.getFilesDir(), fileName);
        try {
            in = assetManager.open(fileName);
            out = context.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Timber.e(e.getMessage());
            return "";
        }

        return String.format("%s/%s", context.getFilesDir(), fileName);
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static File copyResource(Context context, String src, int flag){
        AssetManager assetManager = context.getAssets();
        File filesDir = null;
        try {
            if(flag == 0) {//copy to sdcard
                filesDir = new File(getAppFileInExt(context, AppDirTypeInExt.IMAGE) + File.separator + src);
                File parentDir = filesDir.getParentFile();
                if(!parentDir.exists()){
                    parentDir.mkdirs();
                }
            } else {//copy to data
                filesDir = new File(context.getFilesDir(), src);
            }
            if(!filesDir.exists()) {
                filesDir.createNewFile();
                InputStream open = assetManager.open(src);
                FileOutputStream fileOutputStream = new FileOutputStream(filesDir);
                byte[] buffer = new byte[4 * 1024];
                int len = 0;
                while ((len = open.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                open.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(flag == 0){
                filesDir = copyResource(context, src, 1);
            }
        } catch (NotFoundExternalSD notFoundExternalSD) {
            notFoundExternalSD.printStackTrace();
        }
        return filesDir;
    }

}
