package com.github.common.components.web.wrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * 带本地buffer的InputStream
 */
public class BufferedServletInputStream extends ServletInputStream {

  /**
   * 本地buffer
   */
  private ByteArrayInputStream in;


  public BufferedServletInputStream(ByteArrayInputStream in) {
    this.in = in;
  }

  @Override
  public int read() throws IOException {
    return in.read();
  }

  public boolean isFinished() {
    return false;
  }

  public boolean isReady() {
    return false;
  }

  public void setReadListener(ReadListener readListener) {
  }
}