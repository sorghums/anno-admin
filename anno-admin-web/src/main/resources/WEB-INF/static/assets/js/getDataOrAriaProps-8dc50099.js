function i(a){return Object.keys(a).reduce(function(r,t){return(t.startsWith("data-")||t.startsWith("aria-")||t==="role")&&!t.startsWith("data-__")&&(r[t]=a[t]),r},{})}export{i as g};
