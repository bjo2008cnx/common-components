package com.github.common.components.util.io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * 流工具类
 *
 * @author Wangxm
 */
@Slf4j
public class StreamUtil {

  private static final int BUFFER_START_POSITION = 0;

  private static final int CHAR_BUFFER_LENGTH = 1024;

  /**
   * 通用的关闭方法
   */
  public static void close(final AutoCloseable... autoCloseables) {
    if (autoCloseables == null) {
      return;
    }
    for (AutoCloseable closeable : autoCloseables) {
      try {
        if (closeable != null) {
          closeable.close();
        }
      } catch (Throwable e) {
        log.error("fail to close.", e);
      }
    }
  }

  /**
   * 通用的关闭方法
   */
  public static void close(final Closeable... closeables) {
    closeQuietly(closeables);
  }

  /**
   * 通用的关闭方法
   */
  public static void closeQuietly(final Closeable... closeables) {
    for (Closeable closeable : closeables) {
      try {
        if (null != closeable) {
          closeable.close();
        }
      } catch (Throwable e) {
        log.error("fail to close.", e);
      }
    }
  }

  /**
   * 读取流
   */
  public static String read(InputStream in) {
    if (in == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder("");
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      char[] chars = new char[CHAR_BUFFER_LENGTH];
      int bytesRead;
      while ((bytesRead = reader.read(chars)) > 0) {
        builder.append(chars, BUFFER_START_POSITION, bytesRead);
      }
    } catch (IOException e) {
      log.error("Fail to read input stream", e);
    }
    return builder.toString();
  }

  /**
   * Read an input stream into a byte[]
   */
  public static byte[] stream2Byte(InputStream is) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int len = 0;
    byte[] b = new byte[1024];
    while ((len = is.read(b, 0, b.length)) != -1) {
      baos.write(b, 0, len);
    }
    byte[] buffer = baos.toByteArray();
    return buffer;
  }

  /**
   * @方法功能 InputStream 转为 byte
   */
  public static byte[] inputStream2Byte(InputStream inStream) throws Exception {
    int count = 0;
    while (count == 0) {
      count = inStream.available();
    }
    byte[] b = new byte[count];
    inStream.read(b);
    return b;
  }

  /**
   * @return InputStream
   * @方法功能 byte 转为 InputStream
   */
  public static InputStream byte2InputStream(byte[] b) throws Exception {
    InputStream is = new ByteArrayInputStream(b);
    return is;
  }

  /**
   * 将流另存为文件
   */
  public static void streamSaveAsFile(InputStream is, File outfile) {
    try (FileOutputStream fos = new FileOutputStream(outfile)) {
      byte[] buffer = new byte[1024];
      int len;
      while ((len = is.read(buffer)) > 0) {
        fos.write(buffer, 0, len);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 写到流中
   */
  public static void write2Stream(String content, OutputStream os) throws IOException {
    BufferedOutputStream out = new BufferedOutputStream(os);
    out.write(content.getBytes());
    out.write('\r');
    out.flush();
  }
}
