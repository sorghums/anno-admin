import{e as Oe,R as Pe,l as f,U as Xe}from"./editor.main-1ee36d46.js";import"./index-8edebbff.js";var $e=2*60*1e3,Fe=function(){function n(r){var i=this;this._defaults=r,this._worker=null,this._idleCheckInterval=window.setInterval(function(){return i._checkIfIdle()},30*1e3),this._lastUsedTime=0,this._configChangeListener=this._defaults.onDidChange(function(){return i._stopWorker()})}return n.prototype._stopWorker=function(){this._worker&&(this._worker.dispose(),this._worker=null),this._client=null},n.prototype.dispose=function(){clearInterval(this._idleCheckInterval),this._configChangeListener.dispose(),this._stopWorker()},n.prototype._checkIfIdle=function(){if(!!this._worker){var r=Date.now()-this._lastUsedTime;r>$e&&this._stopWorker()}},n.prototype._getClient=function(){return this._lastUsedTime=Date.now(),this._client||(this._worker=Oe.createWebWorker({moduleId:"vs/language/html/htmlWorker",createData:{languageSettings:this._defaults.options,languageId:this._defaults.languageId},label:this._defaults.languageId}),this._client=this._worker.getProxy()),this._client},n.prototype.getLanguageServiceWorker=function(){for(var r=this,i=[],t=0;t<arguments.length;t++)i[t]=arguments[t];var e;return this._getClient().then(function(a){e=a}).then(function(a){if(r._worker)return r._worker.withSyncedResources(i)}).then(function(a){return e})},n}(),G;(function(n){n.MIN_VALUE=-2147483648,n.MAX_VALUE=2147483647})(G||(G={}));var j;(function(n){n.MIN_VALUE=0,n.MAX_VALUE=2147483647})(j||(j={}));var k;(function(n){function r(t,e){return t===Number.MAX_VALUE&&(t=j.MAX_VALUE),e===Number.MAX_VALUE&&(e=j.MAX_VALUE),{line:t,character:e}}n.create=r;function i(t){var e=t;return o.objectLiteral(e)&&o.uinteger(e.line)&&o.uinteger(e.character)}n.is=i})(k||(k={}));var m;(function(n){function r(t,e,a,u){if(o.uinteger(t)&&o.uinteger(e)&&o.uinteger(a)&&o.uinteger(u))return{start:k.create(t,e),end:k.create(a,u)};if(k.is(t)&&k.is(e))return{start:t,end:e};throw new Error("Range#create called with invalid arguments["+t+", "+e+", "+a+", "+u+"]")}n.create=r;function i(t){var e=t;return o.objectLiteral(e)&&k.is(e.start)&&k.is(e.end)}n.is=i})(m||(m={}));var V;(function(n){function r(t,e){return{uri:t,range:e}}n.create=r;function i(t){var e=t;return o.defined(e)&&m.is(e.range)&&(o.string(e.uri)||o.undefined(e.uri))}n.is=i})(V||(V={}));var J;(function(n){function r(t,e,a,u){return{targetUri:t,targetRange:e,targetSelectionRange:a,originSelectionRange:u}}n.create=r;function i(t){var e=t;return o.defined(e)&&m.is(e.targetRange)&&o.string(e.targetUri)&&(m.is(e.targetSelectionRange)||o.undefined(e.targetSelectionRange))&&(m.is(e.originSelectionRange)||o.undefined(e.originSelectionRange))}n.is=i})(J||(J={}));var z;(function(n){function r(t,e,a,u){return{red:t,green:e,blue:a,alpha:u}}n.create=r;function i(t){var e=t;return o.numberRange(e.red,0,1)&&o.numberRange(e.green,0,1)&&o.numberRange(e.blue,0,1)&&o.numberRange(e.alpha,0,1)}n.is=i})(z||(z={}));var Y;(function(n){function r(t,e){return{range:t,color:e}}n.create=r;function i(t){var e=t;return m.is(e.range)&&z.is(e.color)}n.is=i})(Y||(Y={}));var Z;(function(n){function r(t,e,a){return{label:t,textEdit:e,additionalTextEdits:a}}n.create=r;function i(t){var e=t;return o.string(e.label)&&(o.undefined(e.textEdit)||x.is(e))&&(o.undefined(e.additionalTextEdits)||o.typedArray(e.additionalTextEdits,x.is))}n.is=i})(Z||(Z={}));var R;(function(n){n.Comment="comment",n.Imports="imports",n.Region="region"})(R||(R={}));var K;(function(n){function r(t,e,a,u,s){var g={startLine:t,endLine:e};return o.defined(a)&&(g.startCharacter=a),o.defined(u)&&(g.endCharacter=u),o.defined(s)&&(g.kind=s),g}n.create=r;function i(t){var e=t;return o.uinteger(e.startLine)&&o.uinteger(e.startLine)&&(o.undefined(e.startCharacter)||o.uinteger(e.startCharacter))&&(o.undefined(e.endCharacter)||o.uinteger(e.endCharacter))&&(o.undefined(e.kind)||o.string(e.kind))}n.is=i})(K||(K={}));var O;(function(n){function r(t,e){return{location:t,message:e}}n.create=r;function i(t){var e=t;return o.defined(e)&&V.is(e.location)&&o.string(e.message)}n.is=i})(O||(O={}));var ee;(function(n){n.Error=1,n.Warning=2,n.Information=3,n.Hint=4})(ee||(ee={}));var ne;(function(n){n.Unnecessary=1,n.Deprecated=2})(ne||(ne={}));var te;(function(n){function r(i){var t=i;return t!=null&&o.string(t.href)}n.is=r})(te||(te={}));var W;(function(n){function r(t,e,a,u,s,g){var l={range:t,message:e};return o.defined(a)&&(l.severity=a),o.defined(u)&&(l.code=u),o.defined(s)&&(l.source=s),o.defined(g)&&(l.relatedInformation=g),l}n.create=r;function i(t){var e,a=t;return o.defined(a)&&m.is(a.range)&&o.string(a.message)&&(o.number(a.severity)||o.undefined(a.severity))&&(o.integer(a.code)||o.string(a.code)||o.undefined(a.code))&&(o.undefined(a.codeDescription)||o.string((e=a.codeDescription)===null||e===void 0?void 0:e.href))&&(o.string(a.source)||o.undefined(a.source))&&(o.undefined(a.relatedInformation)||o.typedArray(a.relatedInformation,O.is))}n.is=i})(W||(W={}));var P;(function(n){function r(t,e){for(var a=[],u=2;u<arguments.length;u++)a[u-2]=arguments[u];var s={title:t,command:e};return o.defined(a)&&a.length>0&&(s.arguments=a),s}n.create=r;function i(t){var e=t;return o.defined(e)&&o.string(e.title)&&o.string(e.command)}n.is=i})(P||(P={}));var x;(function(n){function r(a,u){return{range:a,newText:u}}n.replace=r;function i(a,u){return{range:{start:a,end:a},newText:u}}n.insert=i;function t(a){return{range:a,newText:""}}n.del=t;function e(a){var u=a;return o.objectLiteral(u)&&o.string(u.newText)&&m.is(u.range)}n.is=e})(x||(x={}));var C;(function(n){function r(t,e,a){var u={label:t};return e!==void 0&&(u.needsConfirmation=e),a!==void 0&&(u.description=a),u}n.create=r;function i(t){var e=t;return e!==void 0&&o.objectLiteral(e)&&o.string(e.label)&&(o.boolean(e.needsConfirmation)||e.needsConfirmation===void 0)&&(o.string(e.description)||e.description===void 0)}n.is=i})(C||(C={}));var w;(function(n){function r(i){var t=i;return typeof t=="string"}n.is=r})(w||(w={}));var E;(function(n){function r(a,u,s){return{range:a,newText:u,annotationId:s}}n.replace=r;function i(a,u,s){return{range:{start:a,end:a},newText:u,annotationId:s}}n.insert=i;function t(a,u){return{range:a,newText:"",annotationId:u}}n.del=t;function e(a){var u=a;return x.is(u)&&(C.is(u.annotationId)||w.is(u.annotationId))}n.is=e})(E||(E={}));var N;(function(n){function r(t,e){return{textDocument:t,edits:e}}n.create=r;function i(t){var e=t;return o.defined(e)&&U.is(e.textDocument)&&Array.isArray(e.edits)}n.is=i})(N||(N={}));var F;(function(n){function r(t,e,a){var u={kind:"create",uri:t};return e!==void 0&&(e.overwrite!==void 0||e.ignoreIfExists!==void 0)&&(u.options=e),a!==void 0&&(u.annotationId=a),u}n.create=r;function i(t){var e=t;return e&&e.kind==="create"&&o.string(e.uri)&&(e.options===void 0||(e.options.overwrite===void 0||o.boolean(e.options.overwrite))&&(e.options.ignoreIfExists===void 0||o.boolean(e.options.ignoreIfExists)))&&(e.annotationId===void 0||w.is(e.annotationId))}n.is=i})(F||(F={}));var T;(function(n){function r(t,e,a,u){var s={kind:"rename",oldUri:t,newUri:e};return a!==void 0&&(a.overwrite!==void 0||a.ignoreIfExists!==void 0)&&(s.options=a),u!==void 0&&(s.annotationId=u),s}n.create=r;function i(t){var e=t;return e&&e.kind==="rename"&&o.string(e.oldUri)&&o.string(e.newUri)&&(e.options===void 0||(e.options.overwrite===void 0||o.boolean(e.options.overwrite))&&(e.options.ignoreIfExists===void 0||o.boolean(e.options.ignoreIfExists)))&&(e.annotationId===void 0||w.is(e.annotationId))}n.is=i})(T||(T={}));var D;(function(n){function r(t,e,a){var u={kind:"delete",uri:t};return e!==void 0&&(e.recursive!==void 0||e.ignoreIfNotExists!==void 0)&&(u.options=e),a!==void 0&&(u.annotationId=a),u}n.create=r;function i(t){var e=t;return e&&e.kind==="delete"&&o.string(e.uri)&&(e.options===void 0||(e.options.recursive===void 0||o.boolean(e.options.recursive))&&(e.options.ignoreIfNotExists===void 0||o.boolean(e.options.ignoreIfNotExists)))&&(e.annotationId===void 0||w.is(e.annotationId))}n.is=i})(D||(D={}));var X;(function(n){function r(i){var t=i;return t&&(t.changes!==void 0||t.documentChanges!==void 0)&&(t.documentChanges===void 0||t.documentChanges.every(function(e){return o.string(e.kind)?F.is(e)||T.is(e)||D.is(e):N.is(e)}))}n.is=r})(X||(X={}));var M=function(){function n(r,i){this.edits=r,this.changeAnnotations=i}return n.prototype.insert=function(r,i,t){var e,a;if(t===void 0?e=x.insert(r,i):w.is(t)?(a=t,e=E.insert(r,i,t)):(this.assertChangeAnnotations(this.changeAnnotations),a=this.changeAnnotations.manage(t),e=E.insert(r,i,a)),this.edits.push(e),a!==void 0)return a},n.prototype.replace=function(r,i,t){var e,a;if(t===void 0?e=x.replace(r,i):w.is(t)?(a=t,e=E.replace(r,i,t)):(this.assertChangeAnnotations(this.changeAnnotations),a=this.changeAnnotations.manage(t),e=E.replace(r,i,a)),this.edits.push(e),a!==void 0)return a},n.prototype.delete=function(r,i){var t,e;if(i===void 0?t=x.del(r):w.is(i)?(e=i,t=E.del(r,i)):(this.assertChangeAnnotations(this.changeAnnotations),e=this.changeAnnotations.manage(i),t=E.del(r,e)),this.edits.push(t),e!==void 0)return e},n.prototype.add=function(r){this.edits.push(r)},n.prototype.all=function(){return this.edits},n.prototype.clear=function(){this.edits.splice(0,this.edits.length)},n.prototype.assertChangeAnnotations=function(r){if(r===void 0)throw new Error("Text edit change is not configured to manage change annotations.")},n}(),re=function(){function n(r){this._annotations=r===void 0?Object.create(null):r,this._counter=0,this._size=0}return n.prototype.all=function(){return this._annotations},Object.defineProperty(n.prototype,"size",{get:function(){return this._size},enumerable:!1,configurable:!0}),n.prototype.manage=function(r,i){var t;if(w.is(r)?t=r:(t=this.nextId(),i=r),this._annotations[t]!==void 0)throw new Error("Id "+t+" is already in use.");if(i===void 0)throw new Error("No annotation provided for id "+t);return this._annotations[t]=i,this._size++,t},n.prototype.nextId=function(){return this._counter++,this._counter.toString()},n}();(function(){function n(r){var i=this;this._textEditChanges=Object.create(null),r!==void 0?(this._workspaceEdit=r,r.documentChanges?(this._changeAnnotations=new re(r.changeAnnotations),r.changeAnnotations=this._changeAnnotations.all(),r.documentChanges.forEach(function(t){if(N.is(t)){var e=new M(t.edits,i._changeAnnotations);i._textEditChanges[t.textDocument.uri]=e}})):r.changes&&Object.keys(r.changes).forEach(function(t){var e=new M(r.changes[t]);i._textEditChanges[t]=e})):this._workspaceEdit={}}return Object.defineProperty(n.prototype,"edit",{get:function(){return this.initDocumentChanges(),this._changeAnnotations!==void 0&&(this._changeAnnotations.size===0?this._workspaceEdit.changeAnnotations=void 0:this._workspaceEdit.changeAnnotations=this._changeAnnotations.all()),this._workspaceEdit},enumerable:!1,configurable:!0}),n.prototype.getTextEditChange=function(r){if(U.is(r)){if(this.initDocumentChanges(),this._workspaceEdit.documentChanges===void 0)throw new Error("Workspace edit is not configured for document changes.");var i={uri:r.uri,version:r.version},t=this._textEditChanges[i.uri];if(!t){var e=[],a={textDocument:i,edits:e};this._workspaceEdit.documentChanges.push(a),t=new M(e,this._changeAnnotations),this._textEditChanges[i.uri]=t}return t}else{if(this.initChanges(),this._workspaceEdit.changes===void 0)throw new Error("Workspace edit is not configured for normal text edit changes.");var t=this._textEditChanges[r];if(!t){var e=[];this._workspaceEdit.changes[r]=e,t=new M(e),this._textEditChanges[r]=t}return t}},n.prototype.initDocumentChanges=function(){this._workspaceEdit.documentChanges===void 0&&this._workspaceEdit.changes===void 0&&(this._changeAnnotations=new re,this._workspaceEdit.documentChanges=[],this._workspaceEdit.changeAnnotations=this._changeAnnotations.all())},n.prototype.initChanges=function(){this._workspaceEdit.documentChanges===void 0&&this._workspaceEdit.changes===void 0&&(this._workspaceEdit.changes=Object.create(null))},n.prototype.createFile=function(r,i,t){if(this.initDocumentChanges(),this._workspaceEdit.documentChanges===void 0)throw new Error("Workspace edit is not configured for document changes.");var e;C.is(i)||w.is(i)?e=i:t=i;var a,u;if(e===void 0?a=F.create(r,t):(u=w.is(e)?e:this._changeAnnotations.manage(e),a=F.create(r,t,u)),this._workspaceEdit.documentChanges.push(a),u!==void 0)return u},n.prototype.renameFile=function(r,i,t,e){if(this.initDocumentChanges(),this._workspaceEdit.documentChanges===void 0)throw new Error("Workspace edit is not configured for document changes.");var a;C.is(t)||w.is(t)?a=t:e=t;var u,s;if(a===void 0?u=T.create(r,i,e):(s=w.is(a)?a:this._changeAnnotations.manage(a),u=T.create(r,i,e,s)),this._workspaceEdit.documentChanges.push(u),s!==void 0)return s},n.prototype.deleteFile=function(r,i,t){if(this.initDocumentChanges(),this._workspaceEdit.documentChanges===void 0)throw new Error("Workspace edit is not configured for document changes.");var e;C.is(i)||w.is(i)?e=i:t=i;var a,u;if(e===void 0?a=D.create(r,t):(u=w.is(e)?e:this._changeAnnotations.manage(e),a=D.create(r,t,u)),this._workspaceEdit.documentChanges.push(a),u!==void 0)return u},n})();var ie;(function(n){function r(t){return{uri:t}}n.create=r;function i(t){var e=t;return o.defined(e)&&o.string(e.uri)}n.is=i})(ie||(ie={}));var ae;(function(n){function r(t,e){return{uri:t,version:e}}n.create=r;function i(t){var e=t;return o.defined(e)&&o.string(e.uri)&&o.integer(e.version)}n.is=i})(ae||(ae={}));var U;(function(n){function r(t,e){return{uri:t,version:e}}n.create=r;function i(t){var e=t;return o.defined(e)&&o.string(e.uri)&&(e.version===null||o.integer(e.version))}n.is=i})(U||(U={}));var ue;(function(n){function r(t,e,a,u){return{uri:t,languageId:e,version:a,text:u}}n.create=r;function i(t){var e=t;return o.defined(e)&&o.string(e.uri)&&o.string(e.languageId)&&o.integer(e.version)&&o.string(e.text)}n.is=i})(ue||(ue={}));var S;(function(n){n.PlainText="plaintext",n.Markdown="markdown"})(S||(S={}));(function(n){function r(i){var t=i;return t===n.PlainText||t===n.Markdown}n.is=r})(S||(S={}));var $;(function(n){function r(i){var t=i;return o.objectLiteral(i)&&S.is(t.kind)&&o.string(t.value)}n.is=r})($||($={}));var h;(function(n){n.Text=1,n.Method=2,n.Function=3,n.Constructor=4,n.Field=5,n.Variable=6,n.Class=7,n.Interface=8,n.Module=9,n.Property=10,n.Unit=11,n.Value=12,n.Enum=13,n.Keyword=14,n.Snippet=15,n.Color=16,n.File=17,n.Reference=18,n.Folder=19,n.EnumMember=20,n.Constant=21,n.Struct=22,n.Event=23,n.Operator=24,n.TypeParameter=25})(h||(h={}));var B;(function(n){n.PlainText=1,n.Snippet=2})(B||(B={}));var oe;(function(n){n.Deprecated=1})(oe||(oe={}));var se;(function(n){function r(t,e,a){return{newText:t,insert:e,replace:a}}n.create=r;function i(t){var e=t;return e&&o.string(e.newText)&&m.is(e.insert)&&m.is(e.replace)}n.is=i})(se||(se={}));var ce;(function(n){n.asIs=1,n.adjustIndentation=2})(ce||(ce={}));var de;(function(n){function r(i){return{label:i}}n.create=r})(de||(de={}));var fe;(function(n){function r(i,t){return{items:i||[],isIncomplete:!!t}}n.create=r})(fe||(fe={}));var H;(function(n){function r(t){return t.replace(/[\\`*_{}[\]()#+\-.!]/g,"\\$&")}n.fromPlainText=r;function i(t){var e=t;return o.string(e)||o.objectLiteral(e)&&o.string(e.language)&&o.string(e.value)}n.is=i})(H||(H={}));var ge;(function(n){function r(i){var t=i;return!!t&&o.objectLiteral(t)&&($.is(t.contents)||H.is(t.contents)||o.typedArray(t.contents,H.is))&&(i.range===void 0||m.is(i.range))}n.is=r})(ge||(ge={}));var le;(function(n){function r(i,t){return t?{label:i,documentation:t}:{label:i}}n.create=r})(le||(le={}));var he;(function(n){function r(i,t){for(var e=[],a=2;a<arguments.length;a++)e[a-2]=arguments[a];var u={label:i};return o.defined(t)&&(u.documentation=t),o.defined(e)?u.parameters=e:u.parameters=[],u}n.create=r})(he||(he={}));var I;(function(n){n.Text=1,n.Read=2,n.Write=3})(I||(I={}));var ve;(function(n){function r(i,t){var e={range:i};return o.number(t)&&(e.kind=t),e}n.create=r})(ve||(ve={}));var v;(function(n){n.File=1,n.Module=2,n.Namespace=3,n.Package=4,n.Class=5,n.Method=6,n.Property=7,n.Field=8,n.Constructor=9,n.Enum=10,n.Interface=11,n.Function=12,n.Variable=13,n.Constant=14,n.String=15,n.Number=16,n.Boolean=17,n.Array=18,n.Object=19,n.Key=20,n.Null=21,n.EnumMember=22,n.Struct=23,n.Event=24,n.Operator=25,n.TypeParameter=26})(v||(v={}));var pe;(function(n){n.Deprecated=1})(pe||(pe={}));var me;(function(n){function r(i,t,e,a,u){var s={name:i,kind:t,location:{uri:a,range:e}};return u&&(s.containerName=u),s}n.create=r})(me||(me={}));var we;(function(n){function r(t,e,a,u,s,g){var l={name:t,detail:e,kind:a,range:u,selectionRange:s};return g!==void 0&&(l.children=g),l}n.create=r;function i(t){var e=t;return e&&o.string(e.name)&&o.number(e.kind)&&m.is(e.range)&&m.is(e.selectionRange)&&(e.detail===void 0||o.string(e.detail))&&(e.deprecated===void 0||o.boolean(e.deprecated))&&(e.children===void 0||Array.isArray(e.children))&&(e.tags===void 0||Array.isArray(e.tags))}n.is=i})(we||(we={}));var _e;(function(n){n.Empty="",n.QuickFix="quickfix",n.Refactor="refactor",n.RefactorExtract="refactor.extract",n.RefactorInline="refactor.inline",n.RefactorRewrite="refactor.rewrite",n.Source="source",n.SourceOrganizeImports="source.organizeImports",n.SourceFixAll="source.fixAll"})(_e||(_e={}));var ke;(function(n){function r(t,e){var a={diagnostics:t};return e!=null&&(a.only=e),a}n.create=r;function i(t){var e=t;return o.defined(e)&&o.typedArray(e.diagnostics,W.is)&&(e.only===void 0||o.typedArray(e.only,o.string))}n.is=i})(ke||(ke={}));var be;(function(n){function r(t,e,a){var u={title:t},s=!0;return typeof e=="string"?(s=!1,u.kind=e):P.is(e)?u.command=e:u.edit=e,s&&a!==void 0&&(u.kind=a),u}n.create=r;function i(t){var e=t;return e&&o.string(e.title)&&(e.diagnostics===void 0||o.typedArray(e.diagnostics,W.is))&&(e.kind===void 0||o.string(e.kind))&&(e.edit!==void 0||e.command!==void 0)&&(e.command===void 0||P.is(e.command))&&(e.isPreferred===void 0||o.boolean(e.isPreferred))&&(e.edit===void 0||X.is(e.edit))}n.is=i})(be||(be={}));var Ee;(function(n){function r(t,e){var a={range:t};return o.defined(e)&&(a.data=e),a}n.create=r;function i(t){var e=t;return o.defined(e)&&m.is(e.range)&&(o.undefined(e.command)||P.is(e.command))}n.is=i})(Ee||(Ee={}));var xe;(function(n){function r(t,e){return{tabSize:t,insertSpaces:e}}n.create=r;function i(t){var e=t;return o.defined(e)&&o.uinteger(e.tabSize)&&o.boolean(e.insertSpaces)}n.is=i})(xe||(xe={}));var Ae;(function(n){function r(t,e,a){return{range:t,target:e,data:a}}n.create=r;function i(t){var e=t;return o.defined(e)&&m.is(e.range)&&(o.undefined(e.target)||o.string(e.target))}n.is=i})(Ae||(Ae={}));var ye;(function(n){function r(t,e){return{range:t,parent:e}}n.create=r;function i(t){var e=t;return e!==void 0&&m.is(e.range)&&(e.parent===void 0||n.is(e.parent))}n.is=i})(ye||(ye={}));var Ce;(function(n){function r(a,u,s,g){return new Be(a,u,s,g)}n.create=r;function i(a){var u=a;return!!(o.defined(u)&&o.string(u.uri)&&(o.undefined(u.languageId)||o.string(u.languageId))&&o.uinteger(u.lineCount)&&o.func(u.getText)&&o.func(u.positionAt)&&o.func(u.offsetAt))}n.is=i;function t(a,u){for(var s=a.getText(),g=e(u,function(y,L){var Q=y.range.start.line-L.range.start.line;return Q===0?y.range.start.character-L.range.start.character:Q}),l=s.length,d=g.length-1;d>=0;d--){var p=g[d],b=a.offsetAt(p.range.start),c=a.offsetAt(p.range.end);if(c<=l)s=s.substring(0,b)+p.newText+s.substring(c,s.length);else throw new Error("Overlapping edit");l=b}return s}n.applyEdits=t;function e(a,u){if(a.length<=1)return a;var s=a.length/2|0,g=a.slice(0,s),l=a.slice(s);e(g,u),e(l,u);for(var d=0,p=0,b=0;d<g.length&&p<l.length;){var c=u(g[d],l[p]);c<=0?a[b++]=g[d++]:a[b++]=l[p++]}for(;d<g.length;)a[b++]=g[d++];for(;p<l.length;)a[b++]=l[p++];return a}})(Ce||(Ce={}));var Be=function(){function n(r,i,t,e){this._uri=r,this._languageId=i,this._version=t,this._content=e,this._lineOffsets=void 0}return Object.defineProperty(n.prototype,"uri",{get:function(){return this._uri},enumerable:!1,configurable:!0}),Object.defineProperty(n.prototype,"languageId",{get:function(){return this._languageId},enumerable:!1,configurable:!0}),Object.defineProperty(n.prototype,"version",{get:function(){return this._version},enumerable:!1,configurable:!0}),n.prototype.getText=function(r){if(r){var i=this.offsetAt(r.start),t=this.offsetAt(r.end);return this._content.substring(i,t)}return this._content},n.prototype.update=function(r,i){this._content=r.text,this._version=i,this._lineOffsets=void 0},n.prototype.getLineOffsets=function(){if(this._lineOffsets===void 0){for(var r=[],i=this._content,t=!0,e=0;e<i.length;e++){t&&(r.push(e),t=!1);var a=i.charAt(e);t=a==="\r"||a===`
`,a==="\r"&&e+1<i.length&&i.charAt(e+1)===`
`&&e++}t&&i.length>0&&r.push(i.length),this._lineOffsets=r}return this._lineOffsets},n.prototype.positionAt=function(r){r=Math.max(Math.min(r,this._content.length),0);var i=this.getLineOffsets(),t=0,e=i.length;if(e===0)return k.create(0,r);for(;t<e;){var a=Math.floor((t+e)/2);i[a]>r?e=a:t=a+1}var u=t-1;return k.create(u,r-i[u])},n.prototype.offsetAt=function(r){var i=this.getLineOffsets();if(r.line>=i.length)return this._content.length;if(r.line<0)return 0;var t=i[r.line],e=r.line+1<i.length?i[r.line+1]:this._content.length;return Math.max(Math.min(t+r.character,e),t)},Object.defineProperty(n.prototype,"lineCount",{get:function(){return this.getLineOffsets().length},enumerable:!1,configurable:!0}),n}(),o;(function(n){var r=Object.prototype.toString;function i(c){return typeof c!="undefined"}n.defined=i;function t(c){return typeof c=="undefined"}n.undefined=t;function e(c){return c===!0||c===!1}n.boolean=e;function a(c){return r.call(c)==="[object String]"}n.string=a;function u(c){return r.call(c)==="[object Number]"}n.number=u;function s(c,y,L){return r.call(c)==="[object Number]"&&y<=c&&c<=L}n.numberRange=s;function g(c){return r.call(c)==="[object Number]"&&-2147483648<=c&&c<=2147483647}n.integer=g;function l(c){return r.call(c)==="[object Number]"&&0<=c&&c<=2147483647}n.uinteger=l;function d(c){return r.call(c)==="[object Function]"}n.func=d;function p(c){return c!==null&&typeof c=="object"}n.objectLiteral=p;function b(c,y){return Array.isArray(c)&&c.every(y)}n.typedArray=b})(o||(o={}));function A(n){if(!!n)return{character:n.column-1,line:n.lineNumber-1}}function qe(n){if(!!n)return{start:A(n.getStartPosition()),end:A(n.getEndPosition())}}function _(n){if(!!n)return new Pe(n.start.line+1,n.start.character+1,n.end.line+1,n.end.character+1)}function Qe(n){return typeof n.insert!="undefined"&&typeof n.replace!="undefined"}function Ge(n){var r=f.CompletionItemKind;switch(n){case h.Text:return r.Text;case h.Method:return r.Method;case h.Function:return r.Function;case h.Constructor:return r.Constructor;case h.Field:return r.Field;case h.Variable:return r.Variable;case h.Class:return r.Class;case h.Interface:return r.Interface;case h.Module:return r.Module;case h.Property:return r.Property;case h.Unit:return r.Unit;case h.Value:return r.Value;case h.Enum:return r.Enum;case h.Keyword:return r.Keyword;case h.Snippet:return r.Snippet;case h.Color:return r.Color;case h.File:return r.File;case h.Reference:return r.Reference}return r.Property}function q(n){if(!!n)return{range:_(n.range),text:n.newText}}function Je(n){return n&&n.command==="editor.action.triggerSuggest"?{id:n.command,title:n.title,arguments:n.arguments}:void 0}var Te=function(){function n(r){this._worker=r}return Object.defineProperty(n.prototype,"triggerCharacters",{get:function(){return[".",":","<",'"',"=","/"]},enumerable:!1,configurable:!0}),n.prototype.provideCompletionItems=function(r,i,t,e){var a=r.uri;return this._worker(a).then(function(u){return u.doComplete(a.toString(),A(i))}).then(function(u){if(!!u){var s=r.getWordUntilPosition(i),g=new Pe(i.lineNumber,s.startColumn,i.lineNumber,s.endColumn),l=u.items.map(function(d){var p={label:d.label,insertText:d.insertText||d.label,sortText:d.sortText,filterText:d.filterText,documentation:d.documentation,command:Je(d.command),detail:d.detail,range:g,kind:Ge(d.kind)};return d.textEdit&&(Qe(d.textEdit)?p.range={insert:_(d.textEdit.insert),replace:_(d.textEdit.replace)}:p.range=_(d.textEdit.range),p.insertText=d.textEdit.newText),d.additionalTextEdits&&(p.additionalTextEdits=d.additionalTextEdits.map(q)),d.insertTextFormat===B.Snippet&&(p.insertTextRules=f.CompletionItemInsertTextRule.InsertAsSnippet),p});return{isIncomplete:u.isIncomplete,suggestions:l}}})},n}();function Ye(n){return n&&typeof n=="object"&&typeof n.kind=="string"}function Re(n){return typeof n=="string"?{value:n}:Ye(n)?n.kind==="plaintext"?{value:n.value.replace(/[\\`*_{}[\]()#+\-.!]/g,"\\$&")}:{value:n.value}:{value:"```"+n.language+`
`+n.value+"\n```\n"}}function Ze(n){if(!!n)return Array.isArray(n)?n.map(Re):[Re(n)]}var De=function(){function n(r){this._worker=r}return n.prototype.provideHover=function(r,i,t){var e=r.uri;return this._worker(e).then(function(a){return a.doHover(e.toString(),A(i))}).then(function(a){if(!!a)return{range:_(a.range),contents:Ze(a.contents)}})},n}();function Ke(n){var r=f.DocumentHighlightKind;switch(n){case I.Read:return r.Read;case I.Write:return r.Write;case I.Text:return r.Text}return r.Text}var Se=function(){function n(r){this._worker=r}return n.prototype.provideDocumentHighlights=function(r,i,t){var e=r.uri;return this._worker(e).then(function(a){return a.findDocumentHighlights(e.toString(),A(i))}).then(function(a){if(!!a)return a.map(function(u){return{range:_(u.range),kind:Ke(u.kind)}})})},n}();function en(n){var r=f.SymbolKind;switch(n){case v.File:return r.Array;case v.Module:return r.Module;case v.Namespace:return r.Namespace;case v.Package:return r.Package;case v.Class:return r.Class;case v.Method:return r.Method;case v.Property:return r.Property;case v.Field:return r.Field;case v.Constructor:return r.Constructor;case v.Enum:return r.Enum;case v.Interface:return r.Interface;case v.Function:return r.Function;case v.Variable:return r.Variable;case v.Constant:return r.Constant;case v.String:return r.String;case v.Number:return r.Number;case v.Boolean:return r.Boolean;case v.Array:return r.Array}return r.Function}var Le=function(){function n(r){this._worker=r}return n.prototype.provideDocumentSymbols=function(r,i){var t=r.uri;return this._worker(t).then(function(e){return e.findDocumentSymbols(t.toString())}).then(function(e){if(!!e)return e.map(function(a){return{name:a.name,detail:"",containerName:a.containerName,kind:en(a.kind),tags:[],range:_(a.location.range),selectionRange:_(a.location.range)}})})},n}(),Me=function(){function n(r){this._worker=r}return n.prototype.provideLinks=function(r,i){var t=r.uri;return this._worker(t).then(function(e){return e.findDocumentLinks(t.toString())}).then(function(e){if(!!e)return{links:e.map(function(a){return{range:_(a.range),url:a.target}})}})},n}();function je(n){return{tabSize:n.tabSize,insertSpaces:n.insertSpaces}}var We=function(){function n(r){this._worker=r}return n.prototype.provideDocumentFormattingEdits=function(r,i,t){var e=r.uri;return this._worker(e).then(function(a){return a.format(e.toString(),null,je(i)).then(function(u){if(!(!u||u.length===0))return u.map(q)})})},n}(),Ne=function(){function n(r){this._worker=r}return n.prototype.provideDocumentRangeFormattingEdits=function(r,i,t,e){var a=r.uri;return this._worker(a).then(function(u){return u.format(a.toString(),qe(i),je(t)).then(function(s){if(!(!s||s.length===0))return s.map(q)})})},n}(),Ue=function(){function n(r){this._worker=r}return n.prototype.provideRenameEdits=function(r,i,t,e){var a=r.uri;return this._worker(a).then(function(u){return u.doRename(a.toString(),A(i),t)}).then(function(u){return nn(u)})},n}();function nn(n){if(!(!n||!n.changes)){var r=[];for(var i in n.changes)for(var t=Xe.parse(i),e=0,a=n.changes[i];e<a.length;e++){var u=a[e];r.push({resource:t,edit:{range:_(u.range),text:u.newText}})}return{edits:r}}}var He=function(){function n(r){this._worker=r}return n.prototype.provideFoldingRanges=function(r,i,t){var e=r.uri;return this._worker(e).then(function(a){return a.getFoldingRanges(e.toString(),i)}).then(function(a){if(!!a)return a.map(function(u){var s={start:u.startLine+1,end:u.endLine+1};return typeof u.kind!="undefined"&&(s.kind=tn(u.kind)),s})})},n}();function tn(n){switch(n){case R.Comment:return f.FoldingRangeKind.Comment;case R.Imports:return f.FoldingRangeKind.Imports;case R.Region:return f.FoldingRangeKind.Region}}var Ve=function(){function n(r){this._worker=r}return n.prototype.provideSelectionRanges=function(r,i,t){var e=r.uri;return this._worker(e).then(function(a){return a.getSelectionRanges(e.toString(),i.map(A))}).then(function(a){if(!!a)return a.map(function(u){for(var s=[];u;)s.push({range:_(u.range)}),u=u.parent;return s})})},n}();function un(n){var r=new Fe(n),i=function(){for(var e=[],a=0;a<arguments.length;a++)e[a]=arguments[a];return r.getLanguageServiceWorker.apply(r,e)},t=n.languageId;f.registerCompletionItemProvider(t,new Te(i)),f.registerHoverProvider(t,new De(i)),f.registerDocumentHighlightProvider(t,new Se(i)),f.registerLinkProvider(t,new Me(i)),f.registerFoldingRangeProvider(t,new He(i)),f.registerDocumentSymbolProvider(t,new Le(i)),f.registerSelectionRangeProvider(t,new Ve(i)),f.registerRenameProvider(t,new Ue(i)),t==="html"&&(f.registerDocumentFormattingEditProvider(t,new We(i)),f.registerDocumentRangeFormattingEditProvider(t,new Ne(i)))}function on(n){var r=[],i=[],t=new Fe(n);r.push(t);var e=function(){for(var u=[],s=0;s<arguments.length;s++)u[s]=arguments[s];return t.getLanguageServiceWorker.apply(t,u)};function a(){var u=n.languageId,s=n.modeConfiguration;ze(i),s.completionItems&&i.push(f.registerCompletionItemProvider(u,new Te(e))),s.hovers&&i.push(f.registerHoverProvider(u,new De(e))),s.documentHighlights&&i.push(f.registerDocumentHighlightProvider(u,new Se(e))),s.links&&i.push(f.registerLinkProvider(u,new Me(e))),s.documentSymbols&&i.push(f.registerDocumentSymbolProvider(u,new Le(e))),s.rename&&i.push(f.registerRenameProvider(u,new Ue(e))),s.foldingRanges&&i.push(f.registerFoldingRangeProvider(u,new He(e))),s.selectionRanges&&i.push(f.registerSelectionRangeProvider(u,new Ve(e))),s.documentFormattingEdits&&i.push(f.registerDocumentFormattingEditProvider(u,new We(e))),s.documentRangeFormattingEdits&&i.push(f.registerDocumentRangeFormattingEditProvider(u,new Ne(e)))}return a(),r.push(Ie(i)),Ie(r)}function Ie(n){return{dispose:function(){return ze(n)}}}function ze(n){for(;n.length;)n.pop().dispose()}export{on as setupMode,un as setupMode1};
