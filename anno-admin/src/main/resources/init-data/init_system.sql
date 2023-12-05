insert into an_anno_menu (id, parent_id, title, type, open_type, sort, icon, href, permission_id, parse_data, create_by, create_time, del_flag, update_by, update_time) values
('10', '', '系统管理', 0, '_iframe', 10000, 'layui-icon layui-icon-console', '', 'an_user', null, null, '2023-06-09 15:59:12.000000', 0, null, '2023-06-29 20:21:01.462000'),
('11', '10', '菜单管理', 1, '_iframe', 10005, 'layui-icon layui-icon-console', '/system/config/amis/AnAnnoMenu', 'an_anno_menu', 'AnAnnoMenu', null, null, 0, null, '2023-06-30 09:39:02.826000'),
('12', '10', '权限管理', 1, '_iframe', 10006, 'layui-icon layui-icon-console', '/system/config/amis/AnPermission', 'an_permission', 'AnPermission', null, null, 0, null, '2023-06-30 09:38:35.653000'),
('13', '10', '用户管理', 1, '_iframe', 10007, 'layui-icon layui-icon-console', '/system/config/amis/AnUser', 'an_user', 'AnUser', null, null, 0, null, '2023-06-29 20:13:47.910000'),
('14', '10', '角色管理', 1, '_iframe', 10008, 'layui-icon layui-icon-console', '/system/config/amis/AnRole', 'an_role', 'AnRole', null, '2023-06-09 15:59:12.000000', 0, null, '2023-06-29 20:13:10.674000'),
('15', '10', '组织管理', 1, '_iframe', 10009, 'layui-icon layui-icon-console', '/system/config/amis/AnOrg', 'an_org', 'AnOrg', null, null, 0, null, '2023-06-29 20:13:35.706000');

insert into an_org (id, org_name, create_by, create_time, del_flag, update_by, update_time) values
('1674391485808001024', '标准组织', null, '2023-06-29 20:16:53.563000', 0, null, '2023-06-29 20:16:53.563000');

insert into an_role (id, role_name, sort, enable, create_by, create_time, del_flag, update_by, update_time) values
('admin', '超级管理员（默认所有权限）', 0, 1, null, '2023-06-29 14:32:50.738000', 0, '超级管理员', '2023-06-30 10:38:56.839000');

insert into an_user (id, avatar, mobile, password, name, enable, create_by, create_time, del_flag, update_by, update_time, org_id) values
('1666356287765979136', 'https://solon.noear.org/img/solon/favicon.png', '18888888888', 'caf545f0cdd499df43d34613dcfa70c0', '超级管理员', 1, 'admin', '2023-06-07 16:07:53.063000', 0, '测试账号', '2023-06-30 09:52:15.775000', '1674391485808001024');

insert into an_user_role (id, role_id, user_id, create_by, create_time, del_flag, update_by, update_time) values
('1674390418047348736', 'admin', '1666356287765979136', null, '2023-06-29 20:12:38.989000', 0, null, '2023-06-29 20:12:38.989000');
