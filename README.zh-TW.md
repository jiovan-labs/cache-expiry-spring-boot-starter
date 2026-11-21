[English](README.md) | [簡體中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)

# 快取過期 Spring Boot Starter

讓 Spring Boot 應用快速整合帶過期能力的快取。

## 快速開始

- 匯入相依性

```xml
    <dependency>
        <groupId>com.jiovan.spring.starter</groupId>
        <artifactId>cache-expiry-spring-boot-starter</artifactId>
        <version>LATEST</version>
    </dependency>
```

> 預設情況下，此 starter 會自動生效，你可以透過 `jiovan.cache-expiry.enabled = false` 關閉它。

- 使用字首 `jiovan.cache-expiry` 設定 Spring Boot 的 `application.yml`（**可選**）

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

- 在（非靜態）方法上標註 `@CacheExpiry` 註解即可！

- 此 starter 支援主流快取框架，包括
  - caffeine
  - ehcache
  - jcache
  - redis

## 文件

- GitHub：https://github.com/jiovan-labs/cache-expiry-spring-boot-starter

## 環境要求

- jdk 17+

## 許可證

本專案採用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 許可證。

請參閱 `NOTICE.txt` 檔案瞭解所需的宣告與歸屬資訊。

## 捐贈

喜歡這個工具包嗎？歡迎[向我們捐贈](https://jiovan.com/donation)以支援開發。

## 版權

北京玖漫科技有限公司

## 網站

- 玖漫：https://jiovan.com
