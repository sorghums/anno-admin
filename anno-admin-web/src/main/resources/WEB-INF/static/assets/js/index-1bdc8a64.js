import{D as n,a as e,j as a,G as s,B as o,J as u}from"./index-8edebbff.js";import{S as i}from"./index-8d76c3bf.js";import{S as p}from"./index-52c69a25.js";const g=()=>{const{Option:r}=i,[l]=n.useForm(),c=t=>{switch(t){case"male":l.setFieldsValue({note:"Hi, man!"});return;case"female":l.setFieldsValue({note:"Hi, lady!"});return;case"other":l.setFieldsValue({note:"Hi there!"})}},m=t=>{u.success("\u63D0\u4EA4\u7684\u6570\u636E\u4E3A : "+JSON.stringify(t)),JSON.stringify(t)},d=()=>{l.resetFields()},h=()=>{l.setFieldsValue({user:"mark",note:"Hello world!",gender:"male"})};return e("div",{className:"card content-box",children:a(n,{form:l,name:"control-hooks",onFinish:m,labelCol:{span:1},children:[e(n.Item,{name:"user",label:"User",children:e(s,{placeholder:"Please enter a user"})}),e(n.Item,{name:"note",label:"Note",children:e(s,{placeholder:"Please enter a user note"})}),e(n.Item,{name:"gender",label:"Gender",children:a(i,{placeholder:"Select a option and change input text above",onChange:c,allowClear:!0,children:[e(r,{value:"male",children:"male"}),e(r,{value:"female",children:"female"}),e(r,{value:"other",children:"other"})]})}),e(n.Item,{wrapperCol:{offset:1},children:a(p,{children:[e(o,{type:"primary",htmlType:"submit",children:"Submit"}),e(o,{htmlType:"button",onClick:d,children:"Reset"}),e(o,{type:"link",htmlType:"button",onClick:h,children:"Fill form"})," "]})})]})})};export{g as default};