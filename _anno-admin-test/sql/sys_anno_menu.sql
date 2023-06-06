insert into sys_anno_menu (id, parent_id, title, type, sort, icon, href, create_by, create_time, del_flag, update_by, update_time)
values  ('1', null, '系统菜单', 0, null, 'layui-icon layui-icon-console', '', null, null, 0, null, null),
        ('2', '1', '首页', 1, null, 'layui-icon layui-icon-console', '/login.html', null, null, 0, null, null),
        ('3', '1', '菜单配置', 1, null, 'layui-icon layui-icon-console', '/system/config/amis/AnnoMenu', null, null, 0, null, null),
        ('4', '1', '数据分析', 1, null, 'layui-icon layui-icon-console', '/login.html', null, null, 0, null, null),
        ('5', '1', '系统权限', 1, null, 'layui-icon layui-icon-console', '/system/config/amis/SysPermission', null, null, 0, null, null),
        ('6', '1', '测试菜单', 1, null, 'layui-icon layui-icon-console', '/system/config/amis/SysPermission', null, null, 0, null, null),
        ('7', '1', '系统组织', 1, null, 'layui-icon layui-icon-console', '/system/config/amis/SysOrg', null, null, 0, null, null),
        ('8', '1', '系统用户', 1, null, 'layui-icon layui-icon-console', '/system/config/amis/SysUser', null, null, 0, null, null);