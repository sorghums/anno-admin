# @AnnoMain

## 核心注解之一

## 常用作用

- 指定表单标题  --- name()
  - 默认值：无 需要手动置顶值
  - Example： name = "菜单管理"
- 指定表单数据库名称 --- tableName()
  - 默认值：类名 驼峰 转下划线
  - 示例：tableName = "anno_menu"
- 指定表单默认排序 --- annoOrder()
  - 类型：@AnnoOrder
  - 默认值：{} 空数组，数据库默认排序
  - 示例：annoOrder = {@AnnoOrder(orderType = "desc", orderValue = "id")}
- 指定表单相关权限 --- annoPermission()
  - 类型：@AnnoPermission
  - 默认值：@AnnoPermission(enable = false, baseCode = "", baseCodeTranslate = "") 空数组，无权限
  - 示例：annoPermission = @AnnoPermission(enable = true, baseCode = "anno_menu", baseCodeTranslate = "菜单管理")
  - 说明：配置注解后，将自动生成权限，并自动配置菜单，分别生成 增、删、改、查 四种权限。
- 指定是否为树类型表单 --- annoTree()
  - 类型：@AnnoTree
  - 默认值：@AnnoTree(enable = false, parentIdName = "", parentIdValue = "") 非树类型
  - 示例 annoTree = @AnnoTree(label = "title", parentKey = "parentId", key = "id", displayAsTree = true),
  - 说明：配置注解后，将自动生成树类型表单，如果displayAsTree为false，则不显示为树类型
