package com.github.common.components.zk.ioitech;

import com.github.common.components.util.lang.AssertUtil;
import com.github.common.components.zk.config.ConfigLoader;
import com.github.common.components.zk.config.ZkConfig;
import java.io.Closeable;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * zk 访问类
 *
 * @author
 * @date 2018/9/29
 */
@Slf4j
public class ZkConnector implements Closeable {

  private ZkClient client;

  public ZkConnector(String namespace) {
    init(namespace);
  }

  /**
   * 创建实例
   */
  private void init(ZkConfig config) {
    String servers = config.getZkServers();
    AssertUtil.assertNotEmpty(servers, "zookeeper server");
    client = new ZkClient(servers, config.getSessionTimeoutMs(), config.getConnectionTimeoutMs(), new SerializableSerializer());
  }


  /**
   * 创建实例
   */
  private void init(String namespace) {
    ZkConfig config = ConfigLoader.load(namespace);
    init(config);
  }

  /**
   * 创建永久节点
   */
  public String createEphemeral(String path) {
    AssertUtil.assertNotEmpty(path, "path");
    if (client.exists(path)) {
      log.info("ZNode {} has exist", path);
      return path;
    }
    int index = path.lastIndexOf("/");
    if (index == 0) {
      client.createEphemeral(path);
      return path;
    }
    String parentNode = path.substring(0, index);
    if (!client.exists(parentNode)) {
      client.createPersistent(parentNode, true);
    }
    client.createEphemeral(path);
    return path;
  }

  /**
   * 创建永久节点
   */
  public void createPersistent(String path) {
    AssertUtil.assertNotEmpty(path, "path");
    if (!client.exists(path)) {
      client.createPersistent(path, true);
    }
  }

  /**
   * 删除节点
   */
  public void delete(String path) {
    AssertUtil.assertNotEmpty(path, "path");
    client.delete(path);
  }

  /**
   * 递归删除节点
   */
  public void deleteRecursive(String path) {
    AssertUtil.assertNotEmpty(path, "path");
    client.deleteRecursive(path);
  }

  @Override
  public void close() throws IOException {
    if (client != null) {
      this.client.close();
    }
  }

  /**
   * 订阅子节点变更
   */
  public void subscribeChildChanges(String path, IZkChildListener listener) {
    AssertUtil.assertNotEmpty(path, "path");
    client.subscribeChildChanges(path, listener);
  }
}