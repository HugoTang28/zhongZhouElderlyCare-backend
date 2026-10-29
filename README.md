# 家家乐养老管理系统（zzyl-backend）

家家乐养老管理系统后端，基于 Spring Boot 3 + MyBatis-Plus 的老年公寓（养老院）业务管理平台，涵盖入住/退住、来访登记、在住房型床位、护理服务、财务账单等核心业务模块，并提供系统管理（用户/角色/菜单/部门）与权限控制。

## 技术栈

| 分类 | 技术 |
| --- | --- |
| 基础框架 | Spring Boot 3.2.12 |
| 持久层 | MyBatis-Plus 3.5.7 |
| 数据库 | MySQL 8.0.33 |
| 连接池 | Druid 1.2.23 |
| 安全/鉴权 | JWT（jjwt 0.11.5） |
| 接口文档 | Knife4j 4.5.0（OpenAPI 3） |
| 工具包 | Hutool 5.8.32、Fastjson2 2.0.53 |
| 构建 | Maven（JDK 17） |

## 模块结构

```
zzyl-backend
├── pom.xml                  # 父 POM（依赖管理、插件、阿里云镜像）
├── zzyl-admin/              # 启动模块：入口类、Controller、鉴权、业务聚合接口
├── zzyl-system/             # 业务模块：entity / mapper / service（各业务领域）
├── zzyl-framework/          # 框架模块：安全、拦截器、通用组件
├── zzyl-common/             # 公共模块：工具类、常量、通用 DTO/VO
├── sql/                     # 数据库初始化与种子数据脚本
├── mvn.sh                   # Windows + Git Bash 下的 Maven 启动脚本
├── tools_gen_biz_java.py    # 业务模块 Java 代码生成脚本
├── tools_gen_biz_perms.py   # 业务权限 SQL 生成脚本
└── test_*.py                # 接口/业务 Python 测试脚本
```

## 环境要求

- JDK 17
- Maven 3.9.x（或直接使用仓库内的 `mvn.sh`）
- MySQL 8.0+

## 快速开始

### 1. 初始化数据库

按依赖顺序执行 `sql/` 目录下的脚本（建议先建库 `zzyl`，字符集 `utf8mb4`）：

```bash
mysql -uroot -proot -e "CREATE DATABASE IF NOT EXISTS zzyl DEFAULT CHARSET utf8mb4;"
mysql -uroot -proot zzyl < sql/zzyl.sql
mysql -uroot -proot zzyl < sql/zzyl_business.sql
mysql -uroot -proot zzyl < sql/zzyl_business_seed.sql
mysql -uroot -proot zzyl < sql/zzyl_sys_post.sql
mysql -uroot -proot zzyl < sql/zzyl_post_perms.sql
```

> 也可按需执行根目录的 `biz_perms.sql` 初始化业务权限数据。

### 2. 配置数据库连接

编辑 `zzyl-admin/src/main/resources/application.yml`，按需修改数据源与 JWT 配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zzyl?...
    username: root
    password: root

zzyl:
  token:
    secret: zhongzhou-elderly-care-secret-key-2026   # 生产环境请修改
    expire-minutes: 720
```

支持通过环境变量覆盖：`MYSQL_HOST`、`MYSQL_PORT`、`MYSQL_DB`。

### 3. 构建与运行

**使用 `mvn.sh`（Windows + Git Bash 推荐）：**

```bash
# 可通过环境变量覆盖 JDK / Maven 路径
JAVA=D:/ProgramFiles/Java/jdk-17/bin/java M2=D:/MAVEN/apache-maven-3.9.9 ./mvn.sh clean package -DskipTests

# 运行
java -jar zzyl-admin/target/zzyl-admin-1.0.0.jar
```

**使用普通 Maven：**

```bash
mvn clean package -DskipTests
java -jar zzyl-admin/target/zzyl-admin-1.0.0.jar
```

启动成功后默认监听端口 `9996`。

### 4. 接口文档

启动后访问 Knife4j 文档：

- 文档地址：http://localhost:9996/doc.html
- Swagger UI：http://localhost:9996/swagger-ui.html
- OpenAPI：http://localhost:9996/v3/api-docs

> 启动类日志提示的 `9995` 为占位说明，实际端口以 `application.yml` 中的 `server.port`（9996）为准。

## 主要业务模块

系统业务集中在 `zzyl-system` 模块，包含以下领域：

- **来访管理**：预约登记、来访登记
- **入退管理**：入住管理、退住管理
- **在住管理**：房型管理、床位管理、合同管理、请假管理
- **服务管理**：护理任务、护理计划
- **财务管理**：账单管理、预存管理
- **系统管理**：用户、角色、菜单、部门

业务接口通过 `zzyl-admin` 的 `BizController` 进行聚合与统一校验（`BizModuleRegistry`、`BizValidator`）。

## 代码生成 / 工具脚本

- `tools_gen_biz_java.py`：根据模块定义批量生成 entity / mapper / service 代码。
- `tools_gen_biz_perms.py`：生成业务权限 SQL。
- `test_api.py` / `test_biz.py` / `test_all_biz.py`：基于 Python 的接口与业务回归测试。

## 说明

- 当前缓存默认为本地缓存（`zzyl.cache.type: local`），后续可切换为 Redis（配置 `spring.data.redis` 后切换）。
- 逻辑删除字段为 `deleted`（1 已删除 / 0 未删除）。
