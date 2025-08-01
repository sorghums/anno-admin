中文 &nbsp; | &nbsp; [English](./README-EN.md)

<h1 align="center"> Anno Admin</h1>
<h3 align="center">零前端代码，注解驱动，轻松拓展实现界面化CRUD</h3>
<h4 align="center">Solon / SpringBoot 3.x</h4>
<h3 align="center"><a href="https://www.yuque.com/sorghums/anno-admin" target="_blank">Anno Admin</a></h3>
<br>

<p align="center">
   <a target="_blank" href="https://s01.oss.sonatype.org/content/repositories/snapshots/site/sorghum/anno/">
           <img src="https://img.shields.io/maven-metadata/v.svg?label=Maven%20Central&metadataUrl=https://s01.oss.sonatype.org/content/repositories/snapshots/site/sorghum/anno/anno-admin/maven-metadata.xml" />
       </a>
   <a target="_blank" href="https://mvnrepository.com/artifact/site.sorghum.anno/anno-admin">
           <img src="https://img.shields.io/maven-metadata/v.svg?label=Maven Central&metadataUrl=https://repo1.maven.org/maven2/site/sorghum/anno/anno-admin/maven-metadata.xml" />
       </a>
    <a href="./LICENSE"><img src="https://img.shields.io/badge/license-Apache%202-blue" alt="license Apache 2.0"></a>
    <a href="https://gitee.com/cmeet/anno-admin"><img src="https://gitee.com/cmeet/anno-admin/badge/star.svg?theme=dark" alt="Gitee star"></a>
    <a href="https://gitee.com/cmeet/anno-admin"><img src="https://gitee.com/cmeet/anno-admin/badge/fork.svg?theme=dark" alt="Gitee fork"></a>
    <a href="https://github.com/sorghums/anno-admin"><img src="https://img.shields.io/github/stars/sorghums/anno-admin?style=social" alt="GitHub stars"></a>
    <a href="https://github.com/sorghums/anno-admin"><img src="https://img.shields.io/github/forks/sorghums/anno-admin?style=social" alt="GitHub forks"></a>
    <a href="https://gitcode.com/Cmeet/anno-admin"><img src="https://gitcode.com/Cmeet/anno-admin/star/badge.svg" alt="GStar forks"></a>
</p>

<p align="center">
    <a href="http://anno-admin-demo.sorghum.site/">演示地址</a> &nbsp; | &nbsp;
    <a href="https://gitee.com/cmeet/anno-admin-demo">Demo项目</a> &nbsp; | &nbsp;
    <a href="https://gitee.com/cmeet/anno-admin-ant-design-ui">前端仓库(ant-design)</a> &nbsp; | &nbsp;
    <a href="https://gitcode.com/Cmeet/anno-admin">GitCode仓库</a> &nbsp; | &nbsp; 
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

演示环境：[Anno Admin Demo](http://anno-admin-demo.sorghum.site/)

测试账号：16666666666/16666666666

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
### 2024-07-22 1.2.0发布
+ 新增图表组件，支持多种图表展示.
+ 支持自定义表单提交按钮
+ 支持脱离redis使用，新增anno-admin-cache-caffeine/anno-admin-cache-redis
+ 默认实体类支持mybatis-plus/mybatis-flex的注解，新增anno-admin-db-entity-mybatis-plus/anno-admin-db-entity-mybatis-flex
+ 新增anno-admin-x-file-storage插件，更方便的适配云文件存储. 地址：https://x-file-storage.xuyanwu.cn/#/
+ 修复验证码校验大小写的问题
+ 支持enum作为option选项，新增注解：@AnnoEnumLabel/AnnoEnumValue注解
+ 新增用户重置密码功能
+ 图表支持自定义筛选条件
### 2024-09-25 1.2.1发布
+ 修复验证码认证成功后重复认证的问题.
+ 新增C端鉴权工具类
+ 升级Solon版本到V3
+ 修复fileNotFoundError
+ 修复代理类的代理逻辑
+ 新增annoPf4j插件功能
+ 新增endPoint，可连接http请求，方便插件拓展
+ 修复db连表特殊情况查询失败的问题
+ 新增搜索支持默认值
+ 修复tableButton错误的问题
+ 新增导出Excel的插件包，一键配置导出;
+ 用户可以自定义首页菜单
### 2024-11-28 1.2.2发布
+ 修复methodRoute错误
+ 修复首页菜单翻译出错的问题
+ 新增支持默认平台信息的初始配置
+ 修复iframe预览问题 codec预览问题
+ tabsUi优化
+ 进一步适配移动端UI,手机上也可以简单操作!
+ 修复modal最小高度不生效的问题
+ 新增noTranslate字段，防止自动翻译
+ 支持多数据源功能
+ 翻译组件新增颜色标识，可在enum/supplier中进行配置
+ 修复主键非`ID`系统错误的问题
+ 新增FileProxy
+ 修复空菜单出现的问题
+ 新增在线字典值功能
+ 新增formTable功能
+ 完善excel导出功能代码
### 快照分支
+ 升级Solon版本到3.1.2 wood到1.3.16 等其他依赖版本.
+ 新增新增时默认数据的支持注入 See: ResetPwdDataSupplier.java
+ 支持动态设置按钮的表单结构
+ 完善x-file-storage插件适配
+ 修复因泛型导致的编译问题
+ 去除无用的yml,xml,json代替注解功能[废啦]
+ 去除组织过滤概念[废啦]
+ 支持接口形式的注入AnField元数据,适合接入第三方框架, See: WtfInterfaces.java
+ ddl基础框架改为 sorghum-ddl <a href="https://gitee.com/cmeet/sorghum-ddl">点个start吧</a>
+ 新增代理类中跳过执行逻辑

## 参与贡献流程

开发环境准备：
```
# 1. 打前端包到本地仓库（idea 启动项目前，需要先打包前端）
地址：https://gitcode.com/Cmeet/anno-admin-ant-design-ui

# 2. 打包后端代码
mvn clean package -Dmaven.test.skip=true
```

1. **进群讨论**
    - 可以在群里抛出您遇到的问题，或许已经有人解决了您的问题。

2. **提 Issue**
    - 如果 Issue 列表中已有相关问题，可直接认领该 Issue。
    - 若无，请新建一个 Issue 描述问题。

3. **Fork 仓库**
    - 复制本项目的仓库到您的账号下。

4. **新建分支**
    - 新特性分支命名格式：`feat/#{issue-id}`
    - Bug 修复分支命名格式：`fix/#{issue-id}`

5. **本地自测**
    - 确保通过所有现有单元测试。
    - 为您解决的问题新增单元测试。

6. **提交代码**
    - 将修改推送到您的分支。

7. **创建 Pull Request**
    - 向本项目发起合并请求。

8. **PR 审核**
    - 我会验证和测试您的 PR，通过后将合并至 `dev` 分支，随新版本发布时同步到 `master`。

🎉 被采纳的 PR 贡献者将列入 README 的贡献者列表！

## 许可证
Anno-Admin使用Apache 2.0许可证。详情请参阅LICENSE文件。

## 特别感谢JetBrains对开源项目支持：

<a href="https://jb.gg/OpenSourceSupport">
  <img src="https://user-images.githubusercontent.com/8643542/160519107-199319dc-e1cf-4079-94b7-01b6b8d23aa6.png" align="left" height="100" width="100"  alt="JetBrains">
</a>
