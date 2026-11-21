[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# 缓存过期 Spring Boot Starter

让 Spring Boot 应用快速集成带过期能力的缓存。

## 快速开始

- 导入依赖

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>cache-expiry-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 默认情况下，此 starter 会自动生效，你可以通过 `jiovan.cache-expiry.enabled = false` 关闭它。

- 使用前缀 `jiovan.cache-expiry` 配置 Spring Boot 的 `application.yml`（**可选**）

```yml
spring:
    redis:
        host: 'localhost'
        port: 6379
jiovan:
    cache-expiry:
        cache-interceptor:
            inject-cache-manager: true
        cache-manager:
            detect-cache-manager: true
        cache-name-resolver:
            name-prefix: "${spring.application.name}:cache:"
```

- 在（非静态）方法上标注 `@CacheExpiry` 注解即可！

- 此 starter 支持主流缓存框架，包括
  - caffeine
  - ehcache
  - jcache
  - redis

## 文档

- GitHub：https://github.com/jiovan-labs/cache-expiry-spring-boot-starter

## 环境要求

- jdk 17+

## 许可证

本项目采用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 许可证。

请参阅 `NOTICE.txt` 文件了解所需的声明与归属信息。

## 捐赠

喜欢这个工具包吗？欢迎[向我们捐赠](https://jiovan.com/donation)以支持开发。

## 版权

北京玖漫科技有限公司

## 网站

- 玖漫：https://jiovan.com
