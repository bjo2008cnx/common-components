###主要功能
使用AOP拦截方法的输入输出，用于快捷定位系统错误

###使用说明
* 在需要拦截的方法上增加@CallLog注解
* 增加配置类,示例：GlobalConfig
  @EnableAspectJAutoProxy // 启用AspectJ自动代理
  @ComponentScan(basePackages = "com.github.biz;com.github.common.components.log") // 扫描基础包
* 将@CallLog注解输出的log文件配置到独立的log文件中 ,便于结构化分析
  
### 示例程序
  运行BaseJunit4Test，在log中输出拦截方法的出入参
