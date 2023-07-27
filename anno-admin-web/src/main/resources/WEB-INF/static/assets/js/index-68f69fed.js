import{_ as J,b as K,c as Q,d as U,r,e as V,C as W,f as b,h as f,i as X,k as g,l as Y,m as Z,n as ee,I as te,o as oe,E as ne,p as re,q as se,s as ae,t as ce}from"./index-8edebbff.js";import{g as le}from"./getDataOrAriaProps-8dc50099.js";var ie=function(a){J(n,a);var e=K(n);function n(){var t;return Q(this,n),t=e.apply(this,arguments),t.state={error:void 0,info:{componentStack:""}},t}return U(n,[{key:"componentDidCatch",value:function(o,c){this.setState({error:o,info:c})}},{key:"render",value:function(){var o=this.props,c=o.message,l=o.description,C=o.children,v=this.state,x=v.error,p=v.info,y=p&&p.componentStack?p.componentStack:null,h=typeof c=="undefined"?(x||"").toString():c,E=typeof l=="undefined"?y:l;return x?r.exports.createElement(Ce,{type:"error",message:h,description:r.exports.createElement("pre",null,E)}):C}}]),n}(r.exports.Component),pe=globalThis&&globalThis.__rest||function(a,e){var n={};for(var t in a)Object.prototype.hasOwnProperty.call(a,t)&&e.indexOf(t)<0&&(n[t]=a[t]);if(a!=null&&typeof Object.getOwnPropertySymbols=="function")for(var o=0,t=Object.getOwnPropertySymbols(a);o<t.length;o++)e.indexOf(t[o])<0&&Object.prototype.propertyIsEnumerable.call(a,t[o])&&(n[t[o]]=a[t[o]]);return n},ue={success:ee,info:te,error:oe,warning:ne},de={success:re,info:se,error:ae,warning:ce},me=function(e){var n=e.description,t=e.icon,o=e.prefixCls,c=e.type,l=(n?de:ue)[c]||null;return t?Y(t,r.exports.createElement("span",{className:"".concat(o,"-icon")},t),function(){return{className:b("".concat(o,"-icon"),f({},t.props.className,t.props.className))}}):r.exports.createElement(l,{className:"".concat(o,"-icon")})},fe=function(e){var n=e.isClosable,t=e.closeText,o=e.prefixCls,c=e.closeIcon,l=e.handleClose;return n?r.exports.createElement("button",{type:"button",onClick:l,className:"".concat(o,"-close-icon"),tabIndex:0},t?r.exports.createElement("span",{className:"".concat(o,"-close-text")},t):c):null},A=function(e){var n,t=e.description,o=e.prefixCls,c=e.message,l=e.banner,C=e.className,v=C===void 0?"":C,x=e.style,p=e.onMouseEnter,y=e.onMouseLeave,h=e.onClick,E=e.afterClose,N=e.showIcon,$=e.closable,I=e.closeText,S=e.closeIcon,L=S===void 0?r.exports.createElement(Z,null):S,O=e.action,u=pe(e,["description","prefixCls","message","banner","className","style","onMouseEnter","onMouseLeave","onClick","afterClose","showIcon","closable","closeText","closeIcon","action"]),_=r.exports.useState(!1),k=V(_,2),w=k[0],F=k[1],j=r.exports.useRef(),M=r.exports.useContext(W),D=M.getPrefixCls,R=M.direction,s=D("alert",o),B=function(i){var m;F(!0),(m=u.onClose)===null||m===void 0||m.call(u,i)},H=function(){var i=u.type;return i!==void 0?i:l?"warning":"info"},q=I?!0:$,T=H(),P=l&&N===void 0?!0:N,z=b(s,"".concat(s,"-").concat(T),(n={},f(n,"".concat(s,"-with-description"),!!t),f(n,"".concat(s,"-no-icon"),!P),f(n,"".concat(s,"-banner"),!!l),f(n,"".concat(s,"-rtl"),R==="rtl"),n),v),G=le(u);return r.exports.createElement(X,{visible:!w,motionName:"".concat(s,"-motion"),motionAppear:!1,motionEnter:!1,onLeaveStart:function(i){return{maxHeight:i.offsetHeight}},onLeaveEnd:E},function(d){var i=d.className,m=d.style;return r.exports.createElement("div",g({ref:j,"data-show":!w,className:b(z,i),style:g(g({},x),m),onMouseEnter:p,onMouseLeave:y,onClick:h,role:"alert"},G),P?r.exports.createElement(me,{description:t,icon:u.icon,prefixCls:s,type:T}):null,r.exports.createElement("div",{className:"".concat(s,"-content")},c?r.exports.createElement("div",{className:"".concat(s,"-message")},c):null,t?r.exports.createElement("div",{className:"".concat(s,"-description")},t):null),O?r.exports.createElement("div",{className:"".concat(s,"-action")},O):null,r.exports.createElement(fe,{isClosable:!!q,closeText:I,prefixCls:s,closeIcon:L,handleClose:B}))})};A.ErrorBoundary=ie;var Ce=A;export{Ce as A};
