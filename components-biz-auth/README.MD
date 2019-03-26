###主要功能

对web请求进行签名认证

###使用流程：
* 定义filter config，示例：FilterConfigExample
* 在apollo中配置secret key,并注入AuthFilter
* 在client中请求接口时，使用WebSignUtil进行签名

###参考module：java-archetype项目的 example-remote模块

