
### 项目由JDK8升级至JDK11遇到的问题记录:

- 问题1、使用JDK-11.0.15+10启动服务，debug日志出现异常堆栈如下：

```text
[2022-06-10 15:50:11.030] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:1015] - Platform: Windows
[2022-06-10 15:50:11.033] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:417] - -Dio.netty.noUnsafe: false
[2022-06-10 15:50:11.034] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:885] - Java version: 11
[2022-06-10 15:50:11.036] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:130] - sun.misc.Unsafe.theUnsafe: available
[2022-06-10 15:50:11.037] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:154] - sun.misc.Unsafe.copyMemory: available
[2022-06-10 15:50:11.038] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:192] - java.nio.Buffer.address: available
[2022-06-10 15:50:11.046] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:266] - direct buffer constructor: unavailable
java.lang.UnsupportedOperationException: Reflective setAccessible(true) disabled
	at io.netty.util.internal.ReflectionUtil.trySetAccessible(ReflectionUtil.java:31)
	at io.netty.util.internal.PlatformDependent0$4.run(PlatformDependent0.java:238)
	at java.base/java.security.AccessController.doPrivileged(Native Method)
	at io.netty.util.internal.PlatformDependent0.<clinit>(PlatformDependent0.java:232)
	at io.netty.util.internal.PlatformDependent.isAndroid(PlatformDependent.java:289)
	at io.netty.util.internal.PlatformDependent.<clinit>(PlatformDependent.java:92)
	at io.netty.util.AsciiString.<init>(AsciiString.java:223)
	at io.netty.util.AsciiString.<init>(AsciiString.java:210)
	at io.netty.util.AsciiString.cached(AsciiString.java:1401)
	at io.netty.util.AsciiString.<clinit>(AsciiString.java:48)
	at io.netty.handler.codec.http.HttpMethod.<init>(HttpMethod.java:135)
	at io.netty.handler.codec.http.HttpMethod.<clinit>(HttpMethod.java:36)
	at io.github.jiashunx.masker.rest.framework.servlet.mapping.HttpMethod.<clinit>(HttpMethod.java:8)
	at io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet.init0(AbstractRestServlet.java:56)
	at io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet.init(AbstractRestServlet.java:104)
	at io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet.getMappingUrlList(AbstractRestServlet.java:128)
	at io.github.jiashunx.masker.rest.framework.MRestContext.servlet(MRestContext.java:521)
	at io.github.jiashunx.masker.rest.framework.MRestContext.servlet(MRestContext.java:513)
	at io.github.jiashunx.app.miniblog.MiniBlogBoot.start(MiniBlogBoot.java:31)
	at io.github.jiashunx.app.miniblog.MiniBlogBoot.main(MiniBlogBoot.java:15)
[2022-06-10 15:50:11.048] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:331] - java.nio.Bits.unaligned: available, true
[2022-06-10 15:50:11.050] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:390] - jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable
java.lang.IllegalAccessException: class io.netty.util.internal.PlatformDependent0$6 cannot access class jdk.internal.misc.Unsafe (in module java.base) because module java.base does not export jdk.internal.misc to unnamed module @1fdf1c5
	at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361)
	at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:591)
	at java.base/java.lang.reflect.Method.invoke(Method.java:558)
	at io.netty.util.internal.PlatformDependent0$6.run(PlatformDependent0.java:352)
	at java.base/java.security.AccessController.doPrivileged(Native Method)
	at io.netty.util.internal.PlatformDependent0.<clinit>(PlatformDependent0.java:343)
	at io.netty.util.internal.PlatformDependent.isAndroid(PlatformDependent.java:289)
	at io.netty.util.internal.PlatformDependent.<clinit>(PlatformDependent.java:92)
	at io.netty.util.AsciiString.<init>(AsciiString.java:223)
	at io.netty.util.AsciiString.<init>(AsciiString.java:210)
	at io.netty.util.AsciiString.cached(AsciiString.java:1401)
	at io.netty.util.AsciiString.<clinit>(AsciiString.java:48)
	at io.netty.handler.codec.http.HttpMethod.<init>(HttpMethod.java:135)
	at io.netty.handler.codec.http.HttpMethod.<clinit>(HttpMethod.java:36)
	at io.github.jiashunx.masker.rest.framework.servlet.mapping.HttpMethod.<clinit>(HttpMethod.java:8)
	at io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet.init0(AbstractRestServlet.java:56)
	at io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet.init(AbstractRestServlet.java:104)
	at io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet.getMappingUrlList(AbstractRestServlet.java:128)
	at io.github.jiashunx.masker.rest.framework.MRestContext.servlet(MRestContext.java:521)
	at io.github.jiashunx.masker.rest.framework.MRestContext.servlet(MRestContext.java:513)
	at io.github.jiashunx.app.miniblog.MiniBlogBoot.start(MiniBlogBoot.java:31)
	at io.github.jiashunx.app.miniblog.MiniBlogBoot.main(MiniBlogBoot.java:15)
[2022-06-10 15:50:11.051] [DEBUG] [main] [io.netty.util.internal.PlatformDependent0:403] - java.nio.DirectByteBuffer.<init>(long, int): unavailable
[2022-06-10 15:50:11.051] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:1058] - sun.misc.Unsafe: available
[2022-06-10 15:50:11.121] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:1160] - maxDirectMemory: 2082471936 bytes (maybe)
[2022-06-10 15:50:11.122] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:1179] - -Dio.netty.tmpdir: C:\Users\WENJIA~1\AppData\Local\Temp (java.io.tmpdir)
[2022-06-10 15:50:11.122] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:1258] - -Dio.netty.bitMode: 64 (sun.arch.data.model)
[2022-06-10 15:50:11.123] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:177] - -Dio.netty.maxDirectMemory: -1 bytes
[2022-06-10 15:50:11.124] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:184] - -Dio.netty.uninitializedArrayAllocationThreshold: -1
[2022-06-10 15:50:11.126] [DEBUG] [main] [io.netty.util.internal.CleanerJava9:71] - java.nio.ByteBuffer.cleaner(): available
[2022-06-10 15:50:11.127] [DEBUG] [main] [io.netty.util.internal.PlatformDependent:204] - -Dio.netty.noPreferDirect: false
```

   - 问题分析：这些异常堆栈是debug日志，并不是error，因为JDK 1.8 之前（含 JDK 1.8）使用了不安全的 API，
   自 JDK 9（含 JDK 9）已经废弃，提醒我们注意，推测作者是想提醒我们 JDK 版本的编号可能对我们的存在潜在的风险，
   针对这个现象我们通过设置 JVM 参数使此 debug 不再输出。

   ```text
   --add-opens java.base/jdk.internal.misc=ALL-UNNAMED -Dio.netty.tryReflectionSetAccessible=true
   ```

   - 问题分析2：也可更新masker-rest版本至2.0.0以解决此问题。

- 问题2、本地mvn install时，打包jar包成功，但打包javadoc包失败，原因是未调整本地环境变量Path中Java版本，修改为JDK11后打包javadoc正常
