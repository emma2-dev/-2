# 个人博客系统

基于Spring Boot + Vue的个人博客系统，包含用户端和管理端功能。

## 技术栈

### 后端
- Spring Boot 2.7.14
- MyBatis 2.2.2
- MySQL 8.0
- Spring Security
- JWT
- Druid连接池

### 前端
- Vue 2.6.14
- Element UI 2.15.13
- Vue Router 3.5.1
- Vuex 3.6.2
- Axios 1.4.0
- Mavon Editor 2.10.4（Markdown编辑器）

## 功能特性

### 用户端功能
- ✅ 用户注册/登录
- ✅ 博客主页（文章列表、分类筛选）
- ✅ 文章详情（浏览、点赞、评论）
- ✅ 博客随笔（发布、浏览、点赞）
- ✅ 留言板
- ✅ 个人信息修改
- ✅ 密码修改

### 管理员功能
- ✅ 控制台（数据统计）
- ✅ 文章管理（新建、编辑、删除、发布、审核）
- ✅ 随笔管理（查看、删除）
- ✅ 分类管理（新增、编辑、删除）
- ✅ 标签管理（新增、编辑、删除）
- ✅ 轮播图管理（新增、编辑、删除、启用/禁用）
- ✅ 留言管理（查看、删除）
- ✅ 评论管理（查看、删除）
- ✅ 文件管理（上传、删除、查看）
- ✅ 用户管理（查看、删除）

## 项目结构

```
├── src/main/java/com/blog          # 后端代码
│   ├── entity                      # 实体类
│   ├── dto                         # 数据传输对象
│   ├── mapper                      # MyBatis Mapper接口
│   ├── service                     # 业务逻辑层
│   ├── controller                  # 控制器层
│   ├── config                      # 配置类
│   ├── util                        # 工具类
│   ├── common                      # 公共类
│   └── exception                   # 异常处理
├── src/main/resources
│   ├── mapper                      # MyBatis XML映射文件
│   └── application.yml             # 应用配置
├── database                        # 数据库脚本
│   └── schema.sql                  # 建表SQL
└── blog-frontend                   # 前端代码
    ├── public                      # 静态资源
    ├── src
    │   ├── assets                  # 资源文件
    │   ├── components              # 组件
    │   ├── router                  # 路由配置
    │   ├── store                   # Vuex状态管理
    │   ├── utils                   # 工具类
    │   └── views                   # 页面
    └── package.json                # 依赖配置
```

## 快速开始

### 1. 数据库准备

1. 创建MySQL数据库（数据库名：personal_blog）
2. 执行 `database/schema.sql` 脚本创建数据表
3. 默认管理员账号：admin，密码：admin123（首次运行后会自动创建）

### 2. 后端启动

1. 修改 `src/main/resources/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/personal_blog
    username: root
    password: 你的密码
```

2. 使用IDEA打开项目，等待Maven依赖下载完成

3. 运行 `BlogApplication.java` 启动后端服务

4. 后端默认端口：http://localhost:8080/api

### 3. 前端启动

1. 进入前端目录：
```bash
cd blog-frontend
```

2. 安装依赖：
```bash
npm install
```

3. 启动开发服务器：
```bash
npm run serve
```

4. 前端默认端口：http://localhost:8081

### 4. 访问系统

- 用户端首页：http://localhost:8081
- 管理后台：http://localhost:8081/#/admin/dashboard
- 默认管理员账号：admin / admin123

## API接口文档

### 认证接口
- POST /api/auth/login - 用户登录
- POST /api/auth/register - 用户注册

### 用户接口
- GET /api/user/info - 获取用户信息
- PUT /api/user/update - 更新用户信息
- PUT /api/user/password - 修改密码
- GET /api/user/list - 用户列表（管理员）
- DELETE /api/user/{id} - 删除用户（管理员）

### 文章接口
- POST /api/article - 创建文章
- PUT /api/article/{id} - 更新文章
- DELETE /api/article/{id} - 删除文章
- GET /api/article/{id} - 文章详情
- GET /api/article/list - 文章列表
- GET /api/article/recommend - 推荐文章
- POST /api/article/{id}/like - 文章点赞

### 分类接口
- POST /api/category - 创建分类
- PUT /api/category - 更新分类
- DELETE /api/category/{id} - 删除分类
- GET /api/category/list - 分类列表
- GET /api/category/hot - 热门分类

### 标签接口
- POST /api/tag - 创建标签
- PUT /api/tag - 更新标签
- DELETE /api/tag/{id} - 删除标签
- GET /api/tag/list - 标签列表
- GET /api/tag/hot - 热门标签

### 随笔接口
- POST /api/note - 创建随笔
- DELETE /api/note/{id} - 删除随笔
- GET /api/note/list - 随笔列表
- POST /api/note/{id}/like - 随笔点赞

### 留言接口
- POST /api/message - 创建留言
- DELETE /api/message/{id} - 删除留言
- GET /api/message/list - 留言列表
- POST /api/message/{id}/like - 留言点赞

### 评论接口
- POST /api/comment - 创建评论
- DELETE /api/comment/{id} - 删除评论
- GET /api/comment/target - 获取目标的评论
- GET /api/comment/list - 评论列表
- POST /api/comment/{id}/like - 评论点赞

### 文件接口
- POST /api/file/upload - 上传文件
- DELETE /api/file/{id} - 删除文件
- GET /api/file/list - 文件列表
- GET /api/file/image/{filename} - 访问图片
- GET /api/file/download/{filename} - 下载文件

### 轮播图接口
- POST /api/carousel - 创建轮播图（管理员）
- PUT /api/carousel - 更新轮播图（管理员）
- DELETE /api/carousel/{id} - 删除轮播图（管理员）
- GET /api/carousel/list - 获取展示的轮播图列表（公开）
- GET /api/carousel/all - 获取所有轮播图（管理员）

### 统计接口
- GET /api/statistics - 获取系统统计数据（管理员）

## 配置说明

### 数据库配置
在 `application.yml` 中修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/personal_blog
    username: root
    password: your_password
```

### JWT配置
```yaml
jwt:
  secret: personalBlogSecretKey2024  # JWT密钥
  expiration: 604800000               # 过期时间（7天）
```

### 文件上传配置
```yaml
file:
  upload-path: D:/blog-files/         # 文件上传路径
  access-path: /files/**              # 文件访问路径映射
```

## 项目特点

1. **前后端分离**：后端提供RESTful API，前端使用Vue构建，接口统一返回Result格式
2. **权限控制**：基于JWT的身份认证，拦截器实现权限控制，支持用户和管理员角色
3. **代码规范**：采用三层架构（Controller-Service-Mapper），代码结构清晰
4. **响应式设计**：Element UI组件库，美观的用户界面，良好的用户体验
5. **Markdown编辑器**：集成Mavon Editor，支持Markdown格式编写文章，实时预览
6. **文件管理**：支持图片等文件的上传和管理，可配置文件存储路径
7. **轮播图管理**：支持首页轮播图配置，可启用/禁用
8. **数据统计**：管理员后台提供系统数据统计功能
9. **评论系统**：支持文章、随笔、留言的评论和点赞功能
10. **分类标签**：支持文章分类和标签管理，便于内容组织

## 开发环境

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Node.js 14+
- IDEA 2020+

## 注意事项

1. **数据库配置**：首次运行前需要执行数据库脚本创建表结构，确保MySQL服务已启动
2. **配置文件修改**：修改 `application.yml` 中的数据库连接信息（用户名、密码、数据库名）
3. **文件存储路径**：文件上传路径需要手动创建目录，或修改 `application.yml` 中的 `file.upload-path` 为实际路径
4. **前端代理配置**：前端代理配置在 `vue.config.js` 中，默认代理到 http://localhost:8080/api
5. **JWT密钥**：生产环境请修改 `application.yml` 中的 JWT 密钥，确保安全性
6. **端口冲突**：如8080或8081端口被占用，请修改对应配置文件中的端口号
7. **跨域问题**：后端已配置CORS跨域支持，前端开发时无需担心跨域问题
8. **管理员账号**：首次登录后请及时修改默认管理员密码

## 后续优化建议

- [ ] 添加文章搜索功能
- [ ] 添加文章归档功能
- [ ] 优化图片上传（支持七牛云、阿里云OSS等）
- [ ] 添加数据统计图表
- [ ] 添加系统配置管理
- [ ] 优化移动端适配
- [ ] 添加缓存机制（Redis）
- [ ] 添加日志管理
- [ ] 支持主题切换

## 许可证

MIT License

## 联系方式

如有问题或建议，欢迎反馈交流。


