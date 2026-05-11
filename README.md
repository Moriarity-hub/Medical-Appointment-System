# 医疗预约系统

一个前后端分离的医疗预约管理系统，支持患者预约、医生管理、科室管理等功能。

## 功能特性

### 1. 科室管理

- 科室CRUD操作
- 科室信息展示（名称、描述）
- 初始数据预置（内科、外科、妇产科、儿科、急诊科）

### 2. 医生管理

- 医生信息管理（姓名、职称、专业领域、联系方式）
- 科室关联
- 医生可用性状态管理

### 3. 患者管理

- 患者信息管理（姓名、身份证号、联系方式、地址）
- 患者预约历史记录

### 4. 预约管理

- 创建预约（选择医生、预约时间、症状描述）
- 预约状态管理（待确认、已确认、已完成、已取消）
- 预约查询与筛选

## 任务状态

### 已完成

- [x] 科室管理（CRUD）
- [x] 医生管理（CRUD、科室关联）
- [x] 患者管理（CRUD）
- [x] 预约管理（创建、状态更新）
- [x] 预约状态流转（PENDING -> CONFIRMED -> COMPLETED/CANCELLED）
- [x] 初始数据预置（科室、医生）
- [x] RESTful API设计
- [x] Spring Security安全配置
- [x] Swagger API文档

### 待完成

- [ ] 预约时间冲突检测
- [ ] 患者端/管理员端角色分离
- [ ] 邮件/短信通知功能
- [ ] 预约统计报表

## 技术栈

- 后端：Spring Boot 4.0.6 + Spring Data JPA + MySQL + Spring Security + SpringDoc OpenAPI
- 前端：Vue 3 + Vite + Element Plus + Vue Router + Axios

## 环境要求

- JDK 17+
- Node.js ^20.19.0 || >=22.12.0
- MySQL 8.0+

## 快速启动

### 1. 创建数据库

登录 MySQL 后执行：

```sql
CREATE DATABASE mas_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

然后导入数据库表结构和初始数据：

```bash
mysql -u root -p mas_db < src/main/resources/mas_db.sql
```

### 2. 启动后端

在项目根目录执行：

```bash
# 打包（首次运行需要）
mvn clean package -DskipTests

# 运行
java -jar target/medical-appointment-system-0.0.1-SNAPSHOT.jar
```

或使用Maven直接运行：

```bash
mvn spring-boot:run
```

后端启动后访问：
- API服务：`http://localhost:8080`
- Swagger文档：`http://localhost:8080/swagger-ui.html`

### 3. 启动前端

```bash
cd frontend

# 安装依赖（首次）
npm install

# 启动开发服务器
npm run dev
```

前端启动后访问：`http://localhost:5173`

## 项目结构
```
mas/ 
├── src/main/resources/ 
│ ├── application.yml # 后端配置 
│ └── mas_db.sql # 数据库建表脚本 
├── src/main/java/com/example/mas/ 
│ ├── controller/ # REST API控制器 
│ ├── service/ # 业务逻辑层 
│ ├── repository/ # 数据访问层 
│ ├── entity/ # 数据库实体 
│ ├── dto/ # 数据传输对象 
│ ├── config/ # 配置类 
│ └── exception/ # 异常处理 
├── frontend/ # Vue3 前端项目 
│ ├── src/ 
│ │ ├── api/ # 接口调用 
│ │ ├── views/ # 页面组件 
│ │ └── router/ # 路由配置 
│ │ └── vite.config.js # Vite 配置 
 └── pom.xml # Maven 配置
```