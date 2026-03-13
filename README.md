<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT">
  <img src="https://img.shields.io/badge/Stripe-Payments-635BFF?style=for-the-badge&logo=stripe&logoColor=white" alt="Stripe">
  <img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License">
  <img src="https://img.shields.io/badge/Deploy-Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white" alt="Railway">
</p>

# 🛒 Store API — B2C 电商平台后端

> 基于 **Spring Boot 3** 构建的 RESTful 电商后端系统，提供完整的用户认证、商品管理、购物车、订单与在线支付能力。

<p align="center">
  🌐 <strong>线上演示：</strong>
  <a href="https://spring-boot-store-production-3a9d.up.railway.app/swagger-ui.html">
    https://spring-boot-store-production-3a9d.up.railway.app
  </a>
</p>

---

## ✨ 核心功能

| 模块 | 功能说明 |
|------|----------|
| 🔐 **认证与授权** | JWT (Access + Refresh Token) 双令牌机制，基于角色的权限控制 (RBAC) |
| 👤 **用户管理** | 注册、登录、个人信息编辑、密码修改、用户档案 (Profile) |
| 📦 **商品管理** | 商品 CRUD、分类筛选、管理员专属操作 |
| 🛒 **购物车** | 创建购物车、添加/更新/删除商品、清空购物车 |
| 📋 **订单系统** | 查看订单列表、订单详情，支持权限隔离 |
| 💳 **在线支付** | 集成 Stripe Checkout，支持 Webhook 回调 |
| 📖 **API 文档** | Swagger UI / OpenAPI 3.0 自动生成的交互式文档 |
| 🗄️ **数据库迁移** | Flyway 版本化数据库迁移管理 |

---

## 🏗️ 技术架构

```
┌────────────────────────────────────────────────────────────┐
│                        客户端 (Client)                       │
└──────────────────────────┬─────────────────────────────────┘
                           │  HTTP / REST
┌──────────────────────────▼─────────────────────────────────┐
│               Spring Security Filter Chain                 │
│          ┌──────────────────────────────────┐               │
│          │   JWT Authentication Filter      │               │
│          └──────────────────────────────────┘               │
├────────────────────────────────────────────────────────────┤
│                     Controller Layer                       │
│  Auth │ User │ Product │ Cart │ Order │ Checkout │ Admin   │
├────────────────────────────────────────────────────────────┤
│                      Service Layer                         │
│  AuthService │ CartService │ OrderService │ JwtService     │
├────────────────────────────────────────────────────────────┤
│                     Data Access Layer                      │
│          Spring Data JPA + Flyway Migration                │
├────────────────────────────────────────────────────────────┤
│                     MySQL Database                         │
└────────────────────────────────────────────────────────────┘
```

---

## 🛠️ 技术栈

| 类别 | 技术 |
|------|------|
| **框架** | Spring Boot 3.4.1, Spring MVC, Spring Security 6 |
| **持久层** | Spring Data JPA, Hibernate, Flyway |
| **数据库** | MySQL 8.0+ |
| **认证** | JWT (jjwt 0.13.0)，BCrypt 密码加密 |
| **支付** | Stripe Java SDK 31.4.1 |
| **工具链** | Lombok, MapStruct, Spring Validation |
| **API 文档** | SpringDoc OpenAPI (Swagger UI) |
| **监控** | Spring Boot Actuator |
| **模板引擎** | Thymeleaf |
| **环境管理** | spring-dotenv (`.env` 文件) |
| **构建** | Maven, Java 17 |

---

## 📂 项目结构

```
src/main/java/com/codewithmosh/store/
├── config/              # 配置类 (Security, JWT)
├── controllers/         # REST 控制器
├── dtos/                # 数据传输对象 (DTO)
├── entities/            # JPA 实体
├── exceptions/          # 自定义异常
├── filters/             # 拦截器 (JWT 过滤器, 日志过滤器)
├── mappers/             # MapStruct 对象映射
├── payments/            # 支付模块 (Stripe 集成)
├── repositories/        # JPA 数据仓库
└── services/            # 业务逻辑层

src/main/resources/
├── application.yaml     # 主配置文件
├── application-dev.yaml # 开发环境配置
├── application-prod.yaml# 生产环境配置
├── db/migration/        # Flyway 数据库迁移脚本
│   ├── V1__initial_migration.sql
│   ├── V2__create_cart_tables.sql
│   ├── V3__add_role_to_users.sql
│   ├── V4__add_order_tables.sql
│   └── V5__populate_database.sql
└── templates/           # Thymeleaf 模板
```

---

## 📊 数据库 ER 模型

```
┌──────────┐     ┌───────────┐     ┌──────────────┐
│  users   │────▶│ addresses │     │  categories  │
│          │     └───────────┘     └──────┬───────┘
│          │                              │
│          │──── profiles (1:1)           │
│          │                              ▼
│          │     ┌───────────┐     ┌──────────────┐
│          │────▶│ wishlist  │◀────│  products    │
│          │     └───────────┘     └──────┬───────┘
│          │                              │
│          │     ┌───────────┐     ┌──────┴───────┐
│          │────▶│  orders   │────▶│ order_items  │
│          │     └───────────┘     └──────────────┘
│          │
│          │     ┌───────────┐     ┌──────────────┐
└──────────┘     │   carts   │────▶│  cart_items   │
                 └───────────┘     └──────────────┘
```

---

## 🚀 快速开始

### 前置要求

- **Java 17** 或更高版本
- **Maven 3.8+**
- **MySQL 8.0+**

### 1. 克隆仓库

```bash
git clone https://github.com/your-username/spring-api-starter.git
cd spring-api-starter
```

### 2. 配置环境变量

复制 `.env.example` 文件并填入实际值：

```bash
cp .env.example .env
```

在 `.env` 文件中设置以下变量：

```env
JWT_SECRET=your-jwt-secret-key-at-least-256-bits
STRIPE_SECRET_KEY=sk_test_xxx
STRIPE_WEBHOOK_SECRET_KEY=whsec_xxx
```

### 3. 创建数据库

```sql
CREATE DATABASE store_api;
```

> 💡 Flyway 会在应用启动时自动执行数据库迁移，无需手动导入 SQL。

### 4. 启动应用

```bash
./mvnw spring-boot:run
```

应用启动后，访问以下地址：

| 服务 | 地址 |
|------|------|
| **API 接口** | http://localhost:8080 |
| **Swagger UI** | http://localhost:8080/swagger-ui.html |
| **API 文档 (JSON)** | http://localhost:8080/v3/api-docs |
| **Actuator** | http://localhost:8080/actuator |

---

## 📡 API 概览

### 🔐 认证 (`/auth`)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `POST` | `/auth/login` | 用户登录，获取 JWT | 公开 |
| `POST` | `/auth/refresh` | 刷新 Access Token | 公开 |
| `GET` | `/auth/me` | 获取当前用户信息 | 已认证 |

### 👤 用户 (`/users`)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `POST` | `/users` | 注册新用户 | 公开 |
| `GET` | `/users` | 查询用户列表 | 已认证 |
| `GET` | `/users/{id}` | 查询单个用户 | 已认证 |
| `PUT` | `/users/{id}` | 更新用户信息 | 已认证 |
| `DELETE` | `/users/{id}` | 删除用户 | 已认证 |
| `POST` | `/users/{id}/change-password` | 修改密码 | 已认证 |

### 📦 商品 (`/products`)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `GET` | `/products` | 查询商品列表 (支持分类筛选) | 公开 |
| `POST` | `/products` | 新增商品 | 管理员 |
| `PUT` | `/products/{id}` | 更新商品 | 管理员 |

### 🛒 购物车 (`/carts`)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `POST` | `/carts` | 创建购物车 | 公开 |
| `GET` | `/carts/{cartId}` | 查看购物车 | 公开 |
| `POST` | `/carts/{cartId}/items` | 添加商品到购物车 | 公开 |
| `PUT` | `/carts/{cartId}/items/{productId}` | 更新购物车商品数量 | 公开 |
| `DELETE` | `/carts/{cartId}/items/{productId}` | 删除购物车中的商品 | 公开 |
| `DELETE` | `/carts/{cartId}/items` | 清空购物车 | 公开 |

### 📋 订单 (`/orders`)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `GET` | `/orders` | 查看订单列表 | 已认证 |
| `GET` | `/orders/{orderId}` | 查看订单详情 | 已认证 |

### 💳 支付 (`/checkout`)

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `POST` | `/checkout` | 创建支付会话 | 已认证 |
| `POST` | `/checkout/webhook` | Stripe Webhook 回调 | 公开 |

---

## 🔒 安全机制

- **无状态认证**：采用 JWT 双令牌机制 (Access Token + Refresh Token)
- **Access Token**：有效期 2 小时，通过 HTTP Header 传递
- **Refresh Token**：有效期 7 天，通过 HttpOnly Secure Cookie 传递
- **密码加密**：BCrypt 哈希存储
- **RBAC 角色控制**：`USER` / `ADMIN` 两种角色，管理员接口受角色保护
- **CSRF**：无状态 API 已禁用 CSRF

---

## ⚙️ 环境配置

项目通过 `spring-dotenv` 支持 `.env` 文件管理敏感配置：

| 环境变量 | 说明 |
|----------|------|
| `JWT_SECRET` | JWT 签名密钥 (至少 256 bit) |
| `STRIPE_SECRET_KEY` | Stripe 私钥 |
| `STRIPE_WEBHOOK_SECRET_KEY` | Stripe Webhook 签名密钥 |

> ⚠️ **注意**：`.env` 文件已加入 `.gitignore`，请勿将其提交到版本控制系统。

---

## 📝 License

本项目基于 [MIT License](LICENSE) 开源。
