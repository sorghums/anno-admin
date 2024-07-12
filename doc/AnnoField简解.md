# @AnnoField

## 核心注解之一

## 常用作用

- 指定字段的名称 --- title()
  - 默认值：无 需要手动指定
  - Example：title = "手机号"
- 指定字段的数据库字段名 --- tableFieldName()
  - 默认值：则默认使用驼峰式转下划线
  - Example：tableFieldName = "mobile"
- 指定数据库中的字段长度 --- fieldSize()
  - 默认值：使用 anno 设置的默认长度
  - Example：fieldSize = 255
- 指定是否展示在表格中 --- show()
  - 默认值：true
  - 说明：值为 false 则不展示在表格中，但是在详情页中依然会展示
- 指定字段是否可搜索 --- search()
  - 默认值：@AnnoSearch(enable = false)
  - Example：search = @AnnoSearch(enable = true)
- 指定字段是否可编辑/新增 --- edit()
  - 默认值：@AnnoEdit(editEnable = false, addEnable = false)
  - Example：edit = @AnnoEdit(editEnable = true, addEnable = false)
- 指定数据类型 --- dataType()
  - 默认值：AnnoDataType.STRING
  - 说明：指定字段的数据类型，默认使用字符串
  - Example：dataType = AnnoDataType.NUMBER