insert into sys_anno_menu (id, parent_id, title, type, open_type, sort, icon, href, create_by, create_time, del_flag, update_by, update_time)
values  ('1', null, '系统菜单', 0, null, null, 'layui-icon layui-icon-console', '', null, null, 0, null, null),
        ('1667078879380410368', '1', '系统角色', 1, '_iframe', 998, 'layui-icon layui-icon-console', '/system/config/amis/SysRole', null, '2023-06-09 15:59:12.000000', 0, null, '2023-06-26 16:14:28.766000'),
        ('2', '1', '首页', 1, '_iframe', 1000, 'layui-icon layui-icon-console', '/login.html', null, null, 0, null, '2023-06-26 16:14:43.865000'),
        ('3', '1', '菜单配置', 1, '_iframe', 2, 'layui-icon layui-icon-console', '/system/config/amis/AnnoMenu', null, null, 0, null, '2023-06-26 16:13:35.908000'),
        ('5', '1', '系统权限', 1, null, null, 'layui-icon layui-icon-console', '/system/config/amis/SysPermission', null, null, 0, null, null),
        ('7', '1', '系统组织', 1, '_iframe', 999, 'layui-icon layui-icon-console', '/system/config/amis/SysOrg', null, null, 0, null, '2023-06-26 16:14:19.822000'),
        ('8', '1', '系统用户', 1, null, 997, 'layui-icon layui-icon-console', '/system/config/amis/SysUser', null, null, 0, null, '2023-06-26 16:14:32.981000');