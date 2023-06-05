create table sys_anno_menu
(
    id          varchar(20)  not null
        primary key,
    parent_id   varchar(20)  null comment '父菜单id',
    title       varchar(255) null comment '菜单名称',
    type        int          null comment '菜单类型',
    sort        varchar(255) null comment '菜单排序',
    icon        varchar(255) null comment '菜单图标',
    href        varchar(255) null comment '菜单链接',
    create_by   varchar(255) null comment '创建人',
    create_time datetime(6)  null comment '创建时间',
    del_flag    int          null comment '删除标记',
    update_by   varchar(255) null comment '更新人',
    update_time datetime(6)  null comment '更新时间'
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
    id          varchar(20)  not null comment '主键'
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
    role_key    varchar(100)     not null comment '角色权限字符串',
    sort        int              not null comment '显示顺序',
    data_scope  char default '1' null comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    enable      char             not null comment '角色状态（1正常 0停用）',
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
    permission_id varchar(20)  null comment '限id',
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
    role_id     varchar(20)   null comment '角色id',
    enable      int default 0 null comment '状态 1正常 0 封禁',
    create_by   varchar(255)  null comment '创建人',
    create_time datetime(6)   null comment '创建时间',
    del_flag    int           null comment '删除标记',
    update_by   varchar(255)  null comment '更新人',
    update_time datetime(6)   null comment '更新时间'
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

