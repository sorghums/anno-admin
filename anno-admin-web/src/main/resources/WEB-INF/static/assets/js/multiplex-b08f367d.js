import{a}from"./codemirror-f6dd0797.js";function x(y,A){return A.forEach(function(e){e&&typeof e!="string"&&!Array.isArray(e)&&Object.keys(e).forEach(function(c){if(c!=="default"&&!(c in y)){var f=Object.getOwnPropertyDescriptor(e,c);Object.defineProperty(y,c,f.get?f:{enumerable:!0,get:function(){return e[c]}})}})}),Object.freeze(Object.defineProperty(y,Symbol.toStringTag,{value:"Module"}))}var S={exports:{}};(function(y,A){(function(e){e(a.exports)})(function(e){e.multiplexingMode=function(c){var f=Array.prototype.slice.call(arguments,1);function g(n,i,l,r){if(typeof i=="string"){var p=n.indexOf(i,l);return r&&p>-1?p+i.length:p}var v=i.exec(l?n.slice(l):n);return v?v.index+l+(r?v[0].length:0):-1}return{startState:function(){return{outer:e.startState(c),innerActive:null,inner:null,startingInner:!1}},copyState:function(n){return{outer:e.copyState(c,n.outer),innerActive:n.innerActive,inner:n.innerActive&&e.copyState(n.innerActive.mode,n.inner),startingInner:n.startingInner}},token:function(n,i){if(i.innerActive){var t=i.innerActive,r=n.string;if(!t.close&&n.sol())return i.innerActive=i.inner=null,this.token(n,i);var o=t.close&&!i.startingInner?g(r,t.close,n.pos,t.parseDelimiters):-1;if(o==n.pos&&!t.parseDelimiters)return n.match(t.close),i.innerActive=i.inner=null,t.delimStyle&&t.delimStyle+" "+t.delimStyle+"-close";o>-1&&(n.string=r.slice(0,o));var u=t.mode.token(n,i.inner);return o>-1?n.string=r:n.pos>n.start&&(i.startingInner=!1),o==n.pos&&t.parseDelimiters&&(i.innerActive=i.inner=null),t.innerStyle&&(u?u=u+" "+t.innerStyle:u=t.innerStyle),u}else{for(var l=1/0,r=n.string,p=0;p<f.length;++p){var v=f[p],o=g(r,v.open,n.pos);if(o==n.pos){v.parseDelimiters||n.match(v.open),i.startingInner=!!v.parseDelimiters,i.innerActive=v;var d=0;if(c.indent){var m=c.indent(i.outer,"","");m!==e.Pass&&(d=m)}return i.inner=e.startState(v.mode,d),v.delimStyle&&v.delimStyle+" "+v.delimStyle+"-open"}else o!=-1&&o<l&&(l=o)}l!=1/0&&(n.string=r.slice(0,l));var s=c.token(n,i.outer);return l!=1/0&&(n.string=r),s}},indent:function(n,i,l){var r=n.innerActive?n.innerActive.mode:c;return r.indent?r.indent(n.innerActive?n.inner:n.outer,i,l):e.Pass},blankLine:function(n){var i=n.innerActive?n.innerActive.mode:c;if(i.blankLine&&i.blankLine(n.innerActive?n.inner:n.outer),n.innerActive)n.innerActive.close===`
`&&(n.innerActive=n.inner=null);else for(var l=0;l<f.length;++l){var r=f[l];r.open===`
`&&(n.innerActive=r,n.inner=e.startState(r.mode,i.indent?i.indent(n.outer,"",""):0))}},electricChars:c.electricChars,innerMode:function(n){return n.inner?{state:n.inner,mode:n.innerActive.mode}:{state:n.outer,mode:c}}}}})})();var b=S.exports,j=x({__proto__:null,default:b},[S.exports]);export{j as m};