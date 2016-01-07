package org.mf.startupadpage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import org.mf.startupadpage.entity.BnfStartupAdPageDto;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    private Bitmap mBitmap;
    private String mFileName;
    public final static String ALBUM_PATH
            = Environment.getExternalStorageDirectory() + "/download/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BnfStartupAdPageDto bnfStartupAdPageDto = new BnfStartupAdPageDto();
        bnfStartupAdPageDto.setPicUrl("http://watermelon-pic-prod.b0.upaiyun.com/StartupAdPage/201512251628492795420.png");
        bnfStartupAdPageDto.setDisplaySecond(3);
        AdPreference.getInstance().saveStartupAdPage(bnfStartupAdPageDto);
        new Thread(connectNet).start();
    }

    private Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                saveFile(mBitmap, mFileName);
            } catch (IOException e) {
                AdPreference.getInstance().clear();
                e.printStackTrace();
            }
        }

    };

    /**
     * 连接网络
     * 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
     */
    private Runnable connectNet = new Runnable() {
        @Override
        public void run() {
            try {
                String filePath = AdPreference.getInstance().getStartupAdPage().getPicUrl();
                mFileName = "ad.png";
                //******** 方法2：取得的是InputStream，直接从InputStream生成bitmap ***********/
                mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
                //********************************************************************/
                new Thread(saveFileRunnable).start();
                // 发送消息，通知handler在主线程中更新UI
            } catch (Exception e) {
                AdPreference.getInstance().clear();
                e.printStackTrace();
            }

        }

    };

    /**
     * 从网络获取图片
     *
     * @param path
     * @return
     * @throws Exception
     */
    public InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(ALBUM_PATH + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
        bos.flush();
        bos.close();
    }

}
