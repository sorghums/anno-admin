中文 &nbsp; | &nbsp; [English](./README-EN.md)

<h1 align="center"> Anno Admin</h1>
<h3 align="center">零前端代码，注解驱动，轻松拓展实现界面化CRUD</h3>
<h4 align="center">Solon / SpringBoot 3.x</h4>
<h3 align="center"><a href="https://www.yuque.com/sorghums/anno-admin" target="_blank">Anno Admin</a></h3>
---

<p align="center">
    <a href="./LICENSE"><img src="https://img.shields.io/badge/license-Apache%202-blue" alt="license Apache 2.0"></a>
    <a href="https://gitee.com/cmeet/anno-admin"><img src="https://gitee.com/cmeet/anno-admin/badge/star.svg?theme=dark" alt="Gitee star"></a>
    <a href="https://gitee.com/cmeet/anno-admin"><img src="https://gitee.com/cmeet/anno-admin/badge/fork.svg?theme=dark" alt="Gitee fork"></a>
    <a href="https://github.com/sorghums/anno-admin"><img src="https://img.shields.io/github/stars/sorghums/anno-admin?style=social" alt="GitHub stars"></a>
    <a href="https://github.com/sorghums/anno-admin"><img src="https://img.shields.io/github/forks/sorghums/anno-admin?style=social" alt="GitHub forks"></a>
</p>

<p align="center">
    <a href="https://gitee.com/cmeet/anno-admin-demo">Demo项目</a> &nbsp; | &nbsp;
    <a href="https://gitee.com/cmeet/anno-admin-ant-design-ui">前端仓库(ant-design)</a> &nbsp; | &nbsp;
    <a href="https://gitee.com/cmeet/anno-admin">码云仓库</a> &nbsp; | &nbsp; 
    <a href="https://github.com/sorghums/anno-admin">Github 仓库</a> &nbsp; | &nbsp; 
    <a href="https://www.yuque.com/sorghums/anno-admin" target="_blank"><b>📕 使用文档</b></a>
</p>

---
<p align="center">
作者微信（添加请备注 anno-admin）:
</p>
<p align="center"><img src="./img/微信.png" height="300" alt="logo"/></p>

---
## 🚀 简介 | Intro
Anno-Admin是一个新兴的开源项目，旨在通过注解生成后台管理系统，在原项目上轻松拓展实现界面化CRUD。

零前端代码、零 CURD、自动建表，仅需 **一个类文件** + 简洁的注解配置，快速开发企业级 Admin 管理后台。

高扩展性，支持CURD自由扩展、自定义数据源、逻辑删除、OSS。

提供企业级中后台管理系统的全栈解决方案，大幅压缩研发周期，专注核心业务。

> 完美取代 **代码生成器**，开发后台管理系统更优解！


## 🌈 特性 | Features

+ **适配主流框架**：支持**Solon** / **Spring Boot**

+ **自动建表**：表结构自动生成，无需手动建表

+ **虚拟表 / 列**：支持虚拟表 / 虚拟列，轻松对接拓展

+ **易于上手**：会简单的 **Spring Boot / Solon** 基础知识即可

+ **使用简单**：仅需了解 **@AnnoMain** 与 **@AnnoField** 两个注解即可上手开发

+ **代码简洁**：仅需一个 `.java` 文件, template、controller、service、dao 都不需要创建

+ **功能强大**：动态条件处理，逻辑删除等

+ **数据源**：支持：MySQL,H2,SqlLite,Oracle,PgSql等几乎所有主流数据库。

+ **高扩展性**：支持自定义数据源实现、动态权限管理、**自定义 OSS**

+ **大量组件**：时间选择、**一对多**、图片上传、代码编辑器、自动完成、树、**多对多**、地图等多类组件

+ **丰富展示**：普通文本、**二维码**、链接、图片、HTML、代码段等

---

+ **低侵入性**：几乎所有功能都围绕注解而展开，不影响Spring Boot / Solon其他功能或三方库库的使用

+ **前后端分离**：后端与前端可分开部署

+ **前端零代码**：前端布局自动构建，一行前端代码都不用写

+ **无需二次开发**：仅需引用 jar 包即可 ！

## 更新记录
### 2023-12-19 1.0.0发布
+ 初步功能完成，欢迎使用！
### 2024-01-12 1.1.0发布
+ 修复缓存用户信息SerialVersionUID问题
+ 修复父菜单非下拉框树显示的问题
+ 删除大量无用代码，优化历史代码逻辑.
+ 数据库方言支持更多，几乎主流数据库都支持
+ 新增TPL渲染器，菜单 和 列按钮支持 TPL渲染
+ 新增Link,二维码,MarkDown,TextArea,Avatar类型.
+ 支持RichText预览.
+ 修复annoField中数据库名不自定义出现的问题,优化字段和数据库字段名称映射逻辑.
+ 修复javaCmd异常无法获取到的问题,新增管理员修改密码功能.
+ 新增会话管理功能,在线用户、登陆日志相关.
+ 修复翻译代理顺序错乱导致虚拟表不会翻译的问题.
+ 新增TPL渲染器.
+ 修复SpringBoot启动错误的问题.
## 贡献
如果您发现了任何问题或有任何建议，请随时提交issue或pull request。我们非常欢迎您的贡献！

开发环境准备：
```
# 1. 打前端包到本地仓库（idea 启动项目前，需要先打包前端）
mvn clean install -pl anno-admin-ui -am

# 2. 打包后端代码
mvn clean package -pl \!anno-admin-ui -Dmaven.test.skip=true
```

## 许可证
Anno-Admin使用Apache 2.0许可证。详情请参阅LICENSE文件。
