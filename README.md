## app-miniblog

### 简介

- 基于[masker-rest][1] 实现的高性能极简(丑)个人博客。使用sqlite进行数据读写，不依赖其他数据库，开箱即用，部署简单；服务端进行数据渲染，利于SEO。

### 技术栈

- JDK8
- [masker-rest][1] rest服务框架
- [tools-sqlite3][2] sqlite工具包
- jQuery
- [JFinal Enjoy][3] 模板引擎

### 已实现功能：

- 管理控制台（带权限控制）
  - 登录
  - 文章列表
  - 文章发布
  - 标签管理
  - 分类管理
  - 文件管理(上传、下载、链接生成等)
- 博客页（公共）
  - 首页(timeline)
  - 博客详情页
  - 标签页及对应文章查询
  - 分类页及对应文章查询

### 服务url

   - / 首页
   - /console/login.html 控制台登录页
   - /console/index.html 控制台首页

### 运行参数介绍

   - [可选]--ctx 应用context-path，默认"/"，（example：<i>--ctx /myblog</i>
   - [可选] -p | --port 监听端口，默认8080，（example：<i>-p 8080</i> 或 <i>--port 8080</i>
   - [可选] --auser 权限认证用户名，默认admin，（example：<i>--auser admin</i>
   - [可选] --apwd 权限认证密码，默认admin，（example：<i>--apwd admin</i>
   - [可选] --jwt_secret_key jwt secret key，（example：<i>--jwt_secret_key jjjjjjjjj</i>

### 版本清单（最新版本：<b>1.1.1</b>）：

   - version 1.0.0 (released)
      - feature: 规划功能全部完成
   - version 1.1.0 (released)
      - optimizing: 更新 [masker-rest][1] 版本至1.6.6，优化 [IndexServlet][4] 的映射拦截处理逻辑
   - version 1.1.1 (released)
      - optimizing: 更新 [masker-rest][1] 版本至1.6.7，修复部分缺陷。
   - version 1.1.2 (released)
      - optimizing: 更新 [masker-rest][1] 版本至1.6.8，修复部分缺陷。
   - version 1.1.3 (released)
      - optimizing: 更新 [masker-rest][1] 版本至1.7.0，修复部分缺陷。

[1]: https://github.com/jiashunx/masker-rest
[2]: https://github.com/jiashunx/tools-sqlite3
[3]: https://jfinal.com/
[4]: src/main/java/io/github/jiashunx/app/miniblog/index/IndexServlet.java
