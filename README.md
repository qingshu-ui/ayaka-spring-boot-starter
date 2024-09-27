
# ayaka-spring-boot-starter

[![](https://jitpack.io/v/qingshu-ui/ayaka-spring-boot-starter.svg)](https://jitpack.io/#qingshu-ui/ayaka-spring-boot-starter)

[ayaka-spring-boot-starter]() 移植于 [MisakaTAT/Shiro](https://github.com/MisakaTAT/Shiro)，更多详细的文档和教程请移步该项目



## 快速开始

### 1. 添加依赖

项目已经托管在 [JitPack](https://jitpack.io)，你可以通过以下方式添加依赖。

#### 使用 Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.qingshu-ui:ayaka-spring-boot-starter:1.0.0'
}
```

#### 使用 Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.qingshu-ui</groupId>
    <artifactId>ayaka-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

在 `application.yml` 中，添加 OneBot 11 通信相关的配置：

```yaml
ayaka:
  plugin:
    plugin-packages: # 插件所在包名
      - io.github.qingshu.ayaka.example
  http:
    enable: true # 请用 http 上报功能
    api-port: 5800 # onebot api 端口，此项指的是 napcat 的 http 端口
    endpoint: /ayaka # 上报端点 http://localhost:8080/ayaka
    secret: U1FfRzMxQkVvRjBBZ0g5N25tS3Z1YlR3T1NTd0xHNk0= # 校验密钥，关于此配置的详细说明请查看 onebot-11 文档
```

### 3. 编写你的机器人逻辑

通过 `@EventHandler` 注解，可以轻松处理 OneBot 事件。以下是一个简单的例子：

```kotlin
@Slf4j
@Component
class ExamplePlugin : BotPlugin {

    @EventHandler
    @MessageHandlerFilter(cmd = "say")
    fun groupQuitCmd(e: PrivateMessageEvent) {
        val bot = e.bot
        val userId = e.userId
        val msg = MsgUtils.builder()
        	.reply(e.messageId)
        	.text("Hello world!")
        	.build()
        bot.sendPrivateMsg(userId, msg)
    }
}
```



## 参考

- [OneBot 11](https://github.com/botuniverse/onebot-11) - OneBot 11 协议的官方文档。
- [NapNeko/NapCatQQ](https://github.com/NapNeko/NapCatQQ) - Napcat 项目。

## 许可证

本项目使用 [GPL-3.0 许可证](https://github.com/qingshu-ui/example-ayaka/blob/master/LICENSE)。
