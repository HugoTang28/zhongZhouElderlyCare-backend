-- ============================================================
-- 岗位管理：菜单组件 + 按钮权限 + admin 授权
-- 对应后端 @BizModule(value=post, perm=system:post)
-- 前端 route.path = /system/post -> business/index.vue
-- ============================================================

-- 1) 菜单 905「岗位管理」指向通用 CRUD 页面
UPDATE sys_menu SET component = 'business/index' WHERE id = 905;

-- 2) 插入按钮级权限（F），父菜单 905，id 接续现有最大 F(922) 用 923-926
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, perms, icon, sort) VALUES
(923, 905, '岗位查询', 'F', '', '', 'system:post:query',   '', 1),
(924, 905, '岗位新增', 'F', '', '', 'system:post:add',     '', 2),
(925, 905, '岗位修改', 'F', '', '', 'system:post:edit',    '', 3),
(926, 905, '岗位删除', 'F', '', '', 'system:post:remove',  '', 4);

-- 3) 超级管理员拥有全部菜单（含新增按钮）：补齐 role_menu
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu WHERE id NOT IN (SELECT menu_id FROM sys_role_menu WHERE role_id = 1);
