# LumeBlog 博客系统

## 项目概述

LumeBlog是一个基于SSM（Spring + Spring MVC + MyBatis）框架开发的博客系统，支持文章发布、阅读、评论、分类管理、用户登录等核心功能。系统采用前后端分离思想，前端使用Vue.js构建用户界面，后端提供RESTful API接口，实现了完整的博客生命周期管理。

## 技术栈

### 后端技术

- **框架**：Spring 6.0.13、Spring MVC 6.0.13、MyBatis 3.5.15
- **数据库**：MySQL 8.0.33
- **连接池**：Druid 1.2.6
- **分页插件**：PageHelper 5.3.3
- **日志**：Log4j 1.2.17
- **构建工具**：Maven 3.x
- **服务器**：Tomcat 10.1.x

### 前端技术

- **框架**：Vue.js 2.6.14
- **HTTP客户端**：Axios 0.21.1
- **UI框架**：Bootstrap 4.6.0
- **图标库**：Font Awesome 4.x

## 核心功能模块

1. **用户管理**：登录、注册、身份验证
2. **文章管理**：发布、编辑、删除、查看文章
3. **评论系统**：对文章进行评论、删除评论
4. **分类管理**：文章分类筛选
5. **搜索功能**：按标题、作者、关键字搜索文章
6. **分页展示**：文章列表分页加载

## 框架特性应用

1. **Spring IoC/DI**：通过`@Autowired`实现依赖注入，`@Service`、`@Controller`等注解管理Bean
2. **Spring AOP**：实现日志记录、登录验证等横切关注点
3. **Spring声明式事务**：在文章发布、评论操作等方法上使用`@Transactional`保证数据一致性
4. **Spring MVC注解驱动**：使用`@RestController`、`@RequestMapping`等注解实现RESTful API
5. **MyBatis动态SQL**：实现多条件文章查询
6. **MyBatis关联映射**：通过`resultMap`实现文章与用户、分类、评论的关联查询
7. **MyBatis PageHelper**：实现文章列表分页功能
8. **Spring拦截器**：实现登录状态验证，保护需要权限的接口

## 环境配置

1. **JDK**：17及以上
2. **MySQL**：8.0.33
3. **Maven**：3.6及以上
4. **Tomcat**：10.1.x

## 部署步骤

1. 克隆项目到本地

2. 配置数据库：
   - 创建MySQL数据库（默认数据库名：LumeBlog）
   - 导入数据库脚本（脚本位置：`src/main/resources/sql/LumeBlog_*.sql`）
   - 修改数据库配置：`src/main/resources/db.properties`

3. 构建项目：

   ```bash
   mvn clean package
   ```

4. 部署到Tomcat：
   - 项目会自动将WAR包部署到配置的Tomcat目录（可在pom.xml中修改部署路径）
   - 启动Tomcat服务器

5. 访问系统：
   - 浏览器访问：`http://localhost:8080/LumeBlog`（很多功能需要先登录才能使用）

## 项目结构

```
LumeBlog/
├── src/
│   ├── main/
│   │   ├── java/com/hoico/
│   │   │   ├── controller/    # 控制器层
│   │   │   ├── dao/           # 数据访问层
│   │   │   ├── model/         # 实体类
│   │   │   ├── service/       # 服务层
│   │   │   └── util/          # 工具类
│   │   ├── resources/
│   │   │   ├── mapper/        # MyBatis映射文件
│   │   │   └── spring/        # Spring配置文件
│   │   └── webapp/
│   │       ├── static/        # 静态资源
│   │       ├── views/         # 前端页面
│   │       └── WEB-INF/       # Web配置文件
│   └── test/                  # 测试代码
├── pom.xml                    # Maven配置
└── README.md                  # 项目说明
```

## 接口说明

系统提供以下核心RESTful API：

- 文章管理：`/api/articles`（GET/POST/PUT/DELETE）
- 评论管理：`/api/comments`（GET/POST/DELETE）
- 用户管理：`/api/users`（POST登录/注册）
- 分类管理：`/api/categories`（GET）

## 备注

- 登录状态通过Token验证，存储在sessionStorage中
- 分页默认每页显示5条数据，可通过前端组件调整

如有问题或建议，请联系项目维护者。
