##CAT server 配置
cat server启动后，修改服务端配置和客户端路由中的ip列表为cat集群的地址

##CAT client 配置
  放在/data/appdatas/cat 目录下. 后续可接入apollo

##logback接入：
 如果需要使用Cat自定义的Appender，需要在logback.xml中添加如下配置：
   
   ```
       <appender name="CatAppender" class="com.dianping.cat.log.CatLogbackAppender"></appender>
   
       <root level="warn">
           <appender-ref ref="CatAppender" />
       </root>
   ```

## CAT验证
运行CatTest,在CAT中验证Transaction，Problem与Bussiness
   
   
## aop 方式接入
将SpringGlobalConfig拷贝到目标项目里，并修改一下ComponentScan
测试类：CatAopTest



