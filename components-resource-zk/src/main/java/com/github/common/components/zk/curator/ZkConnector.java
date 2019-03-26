package com.github.common.components.zk.curator;

import com.alibaba.fastjson.JSON;
import com.github.common.components.util.lang.AssertUtil;
import com.github.common.components.util.lang.StringUtil;
import com.github.common.components.zk.config.ConfigLoader;
import com.github.common.components.zk.config.ZkConfig;
import java.io.Closeable;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.data.Stat;

/**
 * zk 访问类
 *
 * @author
 * @date 2018/9/29
 */
@Slf4j
public class ZkConnector implements Closeable {

  private CuratorFramework client;

  public ZkConnector(String namespace) {
    init(namespace);
  }

  /**
   * 创建实例
   */
  private void init(ZkConfig config) {
    AssertUtil.assertNotEmpty(config.getZkServers(), "zookeeper server");
    AssertUtil.assertNotEmpty(config.getNamespace(), "zookeeper namespace");

    RetryPolicy retryPolicy = null;
    client = CuratorFrameworkFactory.builder()
        .connectString(config.getZkServers())
        .sessionTimeoutMs(config.getSessionTimeoutMs())
        .connectionTimeoutMs(config.getConnectionTimeoutMs())
        .retryPolicy(retryPolicy)
        .namespace(config.getNamespace())
        .build();
    client.start();
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
  public void createEphemeral(String path) {

  }

  /**
   * 创建永久节点
   */
  public void createPersistent(String path) {
    AssertUtil.assertNotEmpty(path, "path");
  }

  /**
   * 删除节点
   */
  public void delete(String path) {
    AssertUtil.assertNotEmpty(path, "path");
  }

  /**
   * 递归删除节点
   */
  public void deleteRecursive(String path) {
    AssertUtil.assertNotEmpty(path, "path");
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
  }

  public <T> T getZkNodeInfo (String zkNodePath, Class<T> clazz) throws Exception {
    String nodeData = getZkNodeInfoStr(zkNodePath);
    if (StringUtil.isBlank(nodeData)) {
      return null;
    }
    return JSON.parseObject(nodeData, clazz);
  }

  public String getZkNodeInfoStr (String zkNodePath) throws Exception {
    Stat stat = client.checkExists().forPath(zkNodePath);
    if (stat == null) {
      return null;
    }
    return new String(client.getData().forPath(zkNodePath));
  }


}