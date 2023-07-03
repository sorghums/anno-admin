create table sys_anno_menu
(
    id            varchar(20)  not null
        primary key,
    parent_id     varchar(20)  null comment '父菜单id',
    title         varchar(255) null comment '菜单名称',
    type          int          null comment '菜单类型',
    open_type     varchar(30)  null,
    sort          int          null comment '菜单排序',
    icon          varchar(255) null comment '菜单图标',
    href          varchar(255) null comment '菜单链接',
    permission_id varchar(20)  null comment '权限ID',
    create_by     varchar(255) null comment '创建人',
    create_time   datetime(6)  null comment '创建时间',
    del_flag      int          null comment '删除标记',
    update_by     varchar(255) null comment '更新人',
    update_time   datetime(6)  null comment '更新时间'
)
    comment '菜单信息';

create table sys_org
(
    id          varchar(20)  not null comment '主键',
    org_name    varchar(30)  not null comment '组织名称',
    create_by   varchar(255) null comment '创建人',
    create_time datetime(6)  null comment '创建时间',
    del_flag    int          null comment '删除标记',
    update_by   varchar(255) null comment '更新人',
    update_time datetime(6)  null comment '更新时间'
)
    comment '系统组织表';

create table sys_org_user
(
    id          varchar(20)  not null comment '主键'
        primary key,
    org_id      varchar(30)  not null comment '组织id',
    user_id     varchar(20)  null comment '用户id',
    create_by   varchar(255) null comment '创建人',
    create_time datetime(6)  null comment '创建时间',
    del_flag    int          null comment '删除标记',
    update_by   varchar(255) null comment '更新人',
    update_time datetime(6)  null comment '更新时间'
)
    comment '用户组织绑定表';

create table sys_permission
(
    id          varchar(30)  not null comment '主键'
        primary key,
    name        varchar(30)  not null comment '权限名称',
    code        varchar(30)  not null comment '权限标识',
    parent_id   varchar(20)  null comment '父权限id',
    create_by   varchar(255) null comment '创建人',
    create_time datetime(6)  null comment '创建时间',
    del_flag    int          null comment '删除标记',
    update_by   varchar(255) null comment '更新人',
    update_time datetime(6)  null comment '更新时间'
)
    comment '权限信息表';

create table sys_role
(
    id          varchar(20)      not null comment '主键'
        primary key,
    role_name   varchar(30)      not null comment '角色名称',
    sort        int              not null comment '显示顺序',
    data_scope  char default '1' null comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    enable      tinyint          not null comment '角色状态（1正常 0停用）',
    remark      varchar(500)     null comment '备注',
    create_by   varchar(255)     null comment '创建人',
    create_time datetime(6)      null comment '创建时间',
    del_flag    int              null comment '删除标记',
    update_by   varchar(255)     null comment '更新人',
    update_time datetime(6)      null comment '更新时间'
)
    comment '角色信息表';

create table sys_role_permission
(
    id            varchar(20)  not null comment '主键'
        primary key,
    role_id       varchar(30)  not null comment '角色id',
    permission_id varchar(30)  null comment '限id',
    create_by     varchar(255) null comment '创建人',
    create_time   datetime(6)  null comment '创建时间',
    del_flag      int          null comment '删除标记',
    update_by     varchar(255) null comment '更新人',
    update_time   datetime(6)  null comment '更新时间'
)
    comment '权限信息表';

create table sys_user
(
    id          varchar(20)   not null comment '主键'
        primary key,
    avatar      varchar(255)  null comment '用户头像',
    mobile      varchar(255)  null comment '手机号',
    password    varchar(255)  null comment '手机号',
    name        varchar(255)  null comment '用户名',
    enable      int default 0 null comment '状态 1正常 0 封禁',
    create_by   varchar(255)  null comment '创建人',
    create_time datetime(6)   null comment '创建时间',
    del_flag    int           null comment '删除标记',
    update_by   varchar(255)  null comment '更新人',
    update_time datetime(6)   null comment '更新时间',
    org_id      varchar(20)   null comment '组织ID'
)
    comment '系统用户表';

create table sys_user_role
(
    id          varchar(20)  not null comment '主键'
        primary key,
    role_id     varchar(30)  not null comment '角色id',
    user_id     varchar(20)  null comment '用户id',
    create_by   varchar(255) null comment '创建人',
    create_time datetime(6)  null comment '创建时间',
    del_flag    int          null comment '删除标记',
    update_by   varchar(255) null comment '更新人',
    update_time datetime(6)  null comment '更新时间'
)
    comment '用户角色绑定表';


insert into sys_anno_menu (id, parent_id, title, type, open_type, sort, icon, href, permission_id,
                                       create_by, create_time, del_flag, update_by, update_time)
values ('10', '', '系统菜单', 0, '_iframe', 0, 'layui-icon layui-icon-console', '', 'sys_user', null, '2023-06-09 15:59:12.000000', 0, null, '2023-06-29 20:21:01.462000'), ('11', '10', '菜单管理', 1, '_iframe', 995, 'layui-icon layui-icon-console', '/system/config/amis/SysAnnoMenu', 'sys_anno_menu', null, null, 0, null, '2023-06-30 09:39:02.826000'), ('12', '10', '权限管理', 1, '_iframe', 996, 'layui-icon layui-icon-console', '/system/config/amis/SysPermission', 'sys_permission', null, null, 0, null, '2023-06-30 09:38:35.653000'), ('13', '10', '用户管理', 1, '_iframe', 997, 'layui-icon layui-icon-console', '/system/config/amis/SysUser', 'sys_user', null, null, 0, null, '2023-06-29 20:13:47.910000'), ('14', '10', '角色管理', 1, '_iframe', 998, 'layui-icon layui-icon-console', '/system/config/amis/SysRole', 'sys_role', null, '2023-06-09 15:59:12.000000', 0, null, '2023-06-29 20:13:10.674000'), ('15', '10', '组织管理', 1, '_iframe', 999, 'layui-icon layui-icon-console', '/system/config/amis/SysOrg', 'sys_org', null, null, 0, null, '2023-06-29 20:13:35.706000');

insert into sys_org (id, org_name, create_by, create_time, del_flag, update_by, update_time)
values  ('1674391485808001024', '标准组织', null, '2023-06-29 20:16:53.563000', 0, null, '2023-06-29 20:16:53.563000');

insert into sys_role (id, role_name, sort, data_scope, enable, remark, create_by, create_time, del_flag, update_by, update_time)
values  ('admin', '超级管理员（默认所有权限）', 0, '1', 1, null, null, '2023-06-29 14:32:50.738000', 0, '超级管理员', '2023-06-30 10:38:56.839000');

insert into sys_user (id, avatar, mobile, password, name, enable, create_by, create_time, del_flag, update_by, update_time, org_id)
values  ('1666356287765979136', 'https://solon.noear.org/img/solon/favicon.png', '18888888888', 'caf545f0cdd499df43d34613dcfa70c0', '超级管理员', 1, 'admin', '2023-06-07 16:07:53.063000', 0, '测试账号', '2023-06-30 09:52:15.775000', '1674391485808001024');

insert into sys_user_role (id, role_id, user_id, create_by, create_time, del_flag, update_by, update_time)
values  ('1674390418047348736', 'admin', '1666356287765979136', null, '2023-06-29 20:12:38.989000', 0, null, '2023-06-29 20:12:38.989000');

# 2023-07-03 新增菜单解析选项
alter table sys_anno_menu
    add parse_type varchar(20) null comment '解析类型' after href;
alter table sys_anno_menu
    add parse_data varchar(255) null comment '解析数据' after parse_type;