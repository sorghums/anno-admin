import { Login } from "@/api/interface/index";
import { PORT1 } from "@/api/config/servicePort";

import http from "@/api";
import LoginForm from "@/views/login/components/LoginForm";

/**
 * @name 登录模块
 */
// * 用户登录接口
export const loginApi = (params: Login.ReqLoginForm) => {
	params.code = params.captcha.captchaCode;
	params.codeKey = params.captcha.captchaKey;
	return http.post<String>(PORT1 + `/system/auth/login`, params);
};

// * 获取按钮权限
export const getAuthorButtons = () => {
	return http.get<Login.ResAuthButtons>(PORT1 + `/system/config/anButton`);
};

// * 获取菜单列表
export const getMenuList = () => {
	return http.get<Menu.MenuOptions[]>(PORT1 + `/system/config/anMenu`);
};

export const apiGetCaptcha = () => {
	return http.get<Login.CaptchaRes>(PORT1 + `/system/common/captcha`);
};

export const apiGetAmisJson = (clazz?: string, param?: object) => {
	return http.get<any>(PORT1 + `/system/config/amisJson/` + clazz, param, { headers: { noLoading: true } });
};

export const apiLogout = () => {
	return http.post<String>(PORT1 + `/system/auth/logout`);
}

export const apiMe = () => {
	return http.get<Login.ResMe>(PORT1 + `/system/auth/me`);
}

export const apiRefreshUserInfo = () => {
	return http.post<String>(PORT1 + `/system/auth/clearSysUserCache`);
}
