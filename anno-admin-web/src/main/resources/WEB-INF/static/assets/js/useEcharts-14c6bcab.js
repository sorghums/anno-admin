import{i as c}from"./index-a0b7cb32.js";import{r as t}from"./index-8edebbff.js";const x=(i,n)=>{const e=t.exports.useRef(),s=t.exports.useRef(null),o=()=>{var r;s&&((r=e==null?void 0:e.current)==null||r.resize())};return t.exports.useEffect(()=>{var r;(n==null?void 0:n.length)!==0&&((r=e==null?void 0:e.current)==null||r.setOption(i))},[n]),t.exports.useEffect(()=>{var r;return s!=null&&s.current&&(e.current=c(s.current)),(r=e==null?void 0:e.current)==null||r.setOption(i),window.addEventListener("resize",o,!1),()=>{var u;window.removeEventListener("resize",o),(u=e==null?void 0:e.current)==null||u.dispose()}},[]),[s]};export{x as u};
