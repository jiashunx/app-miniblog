# app-miniblog

## 简介

基于[masker-rest][1] 实现的高性能极简(丑)个人博客。使用sqlite进行数据读写，不依赖其他数据库，开箱即用，部署简单；服务端进行数据渲染，利于SEO。

## 技术栈

JDK8、[masker-rest][1]、[tools-sqlite3][2]（sqlite工具包）、jQuery、JFinal Enjoy（模板引擎）

## 计划实现功能：

### 管理控制台(带权限控制)
  - 登录
  - 文章列表
  - 文章发布
  - 标签管理
  - 分类管理
  - 文件管理(上传、下载、链接生成等)
### 博客页(公共)
  - 首页(timeline)
  - 博客详情页
  - 时间轴
  - 标签及对应文章查询
  - 分类及对应文章查询

[1]: https://github.com/jiashunx/masker-rest
[2]: https://github.com/jiashunx/tools-sqlite3
