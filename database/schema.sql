-- 个人博客系统数据库脚本
-- MySQL 8.0

-- 创建数据库
CREATE DATABASE IF NOT EXISTS personal_blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE personal_blog;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(加密后)',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `birthday` DATE COMMENT '生日',
    `introduction` VARCHAR(500) COMMENT '个人简介',
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(200) COMMENT '分类描述',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 标签表
CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `color` VARCHAR(20) COMMENT '标签颜色',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `user_id` BIGINT NOT NULL COMMENT '作者ID',
    `category_id` BIGINT COMMENT '分类ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `summary` VARCHAR(500) COMMENT '文章摘要',
    `content` LONGTEXT COMMENT '文章内容',
    `cover_image` VARCHAR(255) COMMENT '封面图片',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `comment_count` INT DEFAULT 0 COMMENT '评论次数',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `is_recommend` TINYINT DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-草稿，1-待审核，2-已发布，3-审核未通过',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `publish_time` DATETIME COMMENT '发布时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 文章标签关联表
CREATE TABLE IF NOT EXISTS `article_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- 随笔表
CREATE TABLE IF NOT EXISTS `note` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '随笔ID',
    `user_id` BIGINT NOT NULL COMMENT '作者ID',
    `content` TEXT NOT NULL COMMENT '随笔内容',
    `images` VARCHAR(1000) COMMENT '图片URL列表，多个用逗号分隔',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `comment_count` INT DEFAULT 0 COMMENT '评论次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='随笔表';

-- 留言表
CREATE TABLE IF NOT EXISTS `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '留言ID',
    `user_id` BIGINT NOT NULL COMMENT '留言用户ID',
    `content` TEXT NOT NULL COMMENT '留言内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
    `type` TINYINT NOT NULL COMMENT '评论类型：1-文章评论，2-随笔评论',
    `target_id` BIGINT NOT NULL COMMENT '目标ID（文章ID或随笔ID）',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父评论ID，0表示顶级评论',
    `reply_user_id` BIGINT COMMENT '回复的用户ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `like_count` INT DEFAULT 0 COMMENT '点赞次数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已删除，1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_target` (`type`, `target_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 文件表
CREATE TABLE IF NOT EXISTS `file` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `user_id` BIGINT NOT NULL COMMENT '上传用户ID',
    `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) COMMENT '文件类型',
    `category` VARCHAR(20) COMMENT '文件分类：image-图片，video-视频，document-文档，other-其他',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT COMMENT '配置值',
    `config_type` VARCHAR(20) COMMENT '配置类型',
    `description` VARCHAR(200) COMMENT '配置描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 插入初始管理员账户（密码：admin123，使用BCrypt加密）
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `role`, `status`) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin@blog.com', 'ADMIN', 1);

-- 插入默认分类
INSERT INTO `category` (`name`, `description`, `sort`) VALUES
('技术分享', '技术相关的文章', 1),
('生活随笔', '生活中的点点滴滴', 2),
('学习笔记', '学习过程中的笔记', 3),
('其他', '其他类型的文章', 99);

-- 插入默认标签
INSERT INTO `tag` (`name`, `color`) VALUES
('Java', '#f56c6c'),
('Spring Boot', '#67c23a'),
('Vue', '#409eff'),
('MySQL', '#e6a23c'),
('前端', '#909399'),
('后端', '#606266');

-- 插入系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `config_type`, `description`) VALUES
('site_name', '个人博客系统', 'string', '网站名称'),
('site_description', '分享技术，记录生活', 'string', '网站描述'),
('site_keywords', '博客,技术,分享', 'string', '网站关键词'),
('comment_audit', '0', 'boolean', '评论是否需要审核：0-不需要，1-需要'),
('register_enabled', '1', 'boolean', '是否允许注册：0-不允许，1-允许');

-- 首页轮播图表
CREATE TABLE IF NOT EXISTS `carousel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `img_url` VARCHAR(255) NOT NULL COMMENT '图片地址',
  `target_url` VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '标题',
  `sort` INT DEFAULT 0 COMMENT '排序（大在前）',
  `status` TINYINT DEFAULT 1 COMMENT '是否显示：1显示，0隐藏',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页轮播图';

-- 轮播图示例数据（可按需修改图片链接）
INSERT INTO `carousel` (`img_url`, `target_url`, `title`, `sort`, `status`) VALUES
('https://picsum.photos/1200/300?random=1', 'https://www.baidu.com', '示例轮播1', 3, 1),
('https://picsum.photos/1200/300?random=2', 'https://www.jd.com', '示例轮播2', 2, 1),
('https://picsum.photos/1200/300?random=3', 'https://www.taobao.com', '示例轮播3', 1, 1);


