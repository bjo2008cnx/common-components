##spring boot 测试用例：

1. 在Apollo配置中心创建AppId为`spring-boot-logger`的项目
2. 在默认的`application`下做如下配置（可以通过文本模式直接复制、粘贴下面的内容）：

    ```properties
    logging.level.com.github.common.components.log.dynamic.boot = error
    ```
3. 运行`....boot.Application`启动Demo
4. 程序只会持续打印error级别日志
5. 在Apollo配置中心修改配置，把`logging.level.com.github.common.components.log.dynamic.boot`的值改为`debug`并发布配置
6. 程序会输出debug, info, warn, error等级别日志，说明动态调整Logging Level生效了

业务方接入:
在Application所在文件夹下增加一个类继承BootLogLevelConfig,并增加@Service 注解即可。

##spring cloud 测试用例：

1. 在Apollo配置中心创建AppId为`spring-boot-logger`的项目
2. 在默认的`application`下做如下配置（可以通过文本模式直接复制、粘贴下面的内容）：

    ```properties
    logging.level.com.github.common.components.log.dynamic.cloud = error
    ```
3. 运行`Application`启动Demo
4. 程序只会持续打印error级别日志
5. 在Apollo配置中心修改配置，把`logging.level.com.github.common.components.log.dynamic.boot`的值改为`debug`并发布配置
6. 程序会输出debug, info, warn, error等级别日志，说明动态调整Logging Level生效了

业务方接入:
1. 在Application所在文件夹下增加一个类继承CloudLogLevelConfig,并增加@Service 。
2. 增加以下依赖      

     ```
       <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-context</artifactId>
        </dependency>
    ```        
