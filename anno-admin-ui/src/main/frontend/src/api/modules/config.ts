import {System} from "@/api/interface";
import {PORT1} from "@/api/config/servicePort";

import http from "@/api";


// * 获取按钮权限
export const apiGetGlobalConfig = () => {
	return http.get<System.GlobalConfig>(PORT1 + `/api/global/config`, {}, { headers: { noLoading: true } });
};

// @ts-ignore
export const goGlobalConfig= (setGlobalConfig) => {
	if (window.localStorage.getItem("globalConfig")){
		let tempJson = JSON.parse(window.localStorage.getItem("globalConfig") || "{}");
		setGlobalConfig(tempJson);
	}
	apiGetGlobalConfig().then((res) => {
		if (res.data) {
			window.localStorage.setItem("globalConfig", JSON.stringify(res.data));
			setGlobalConfig(res.data);
		}
	});
}

