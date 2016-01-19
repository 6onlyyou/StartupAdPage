package org.mf.startupadpage;

/**
 * Created by mafei on 16/1/19.
 */
public class Utils {
    public static String getImgName(String imgUrl) {
        if (imgUrl == null||imgUrl.equals("")) {
            return "";
        }
        String[] strs = imgUrl.split("/");
        return strs[strs.length - 1];
    }
}
