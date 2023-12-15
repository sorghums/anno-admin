import {message} from "antd";

/**
 * @description: 校验网络请求状态码
 * @param {Number} status
 * @param msg 错误提示信息
 * @return void
 */
export const checkStatus = (status: number, msg: string): void => {
	switch (status) {
		case 400:
			msg = msg ? msg : "请求参数错误！请您稍后重试";
			message.error(msg);
			break;
		case 401:
			msg = msg ? msg : "登录失效！请您重新登录";
			message.error(msg);
			break;
		case 403:
			msg = msg ? msg : "当前账号无权限访问！";
			message.error(msg);
			break;
		case 404:
			msg = msg ? msg : "你所访问的资源不存在！";
			message.error(msg);
			break;
		case 405:
			msg = msg ? msg : "请求超时！请您稍后重试";
			message.error(msg);
			break;
		case 408:
			msg = msg ? msg : "请求超时！请您稍后重试";
			message.error(msg);
			break;
		case 500:
			msg = msg ? msg : "服务异常！";
			message.error(msg);
			break;
		case 502:
			msg = msg ? msg : "网关错误！";
			message.error(msg);
			break;
		case 503:
			msg = msg ? msg : "服务不可用！";
			message.error(msg);
			break;
		case 504:
			msg = msg ? msg : "网关超时！";
			message.error(msg);
			break;
		default:
			msg = msg ? msg : "请求失败！";
			message.error(msg);
	}
};
