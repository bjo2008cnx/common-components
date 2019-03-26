package com.github.common.components.rpc.serializer;

import java.io.File;
import java.io.IOException;

/**
 * @Auther: Code
 * @Date: 2018/9/9 18:02
 * @Description: 批量重命名文件
 */
public class RenameFile {
    static String dir = "/Users/michael/codes/codes-mine/common-components";//文件所在路径，所有文件的根目录，记得修改为你电脑上的文件所在路径

    public static void main(String[] args) throws IOException {
        recursiveTraversalFolder(dir);//递归遍历此路径下所有文件夹
    }

    /**
     * 递归遍历文件夹获取文件
     */
    public static void recursiveTraversalFolder(String path) {
        File folder = new File(path);
        File[] fileArr = folder.listFiles();
        for (File file : fileArr) {
            if (file.isDirectory()) {
                //是文件夹，继续递归，如果需要重命名文件夹，这里可以做处理
                if (file.getName().contains("quzeng")) {
                    file.renameTo(new File("github"));//重命名
                } else {
                    recursiveTraversalFolder(file.getAbsolutePath());
                }
            }
        }
    }
}