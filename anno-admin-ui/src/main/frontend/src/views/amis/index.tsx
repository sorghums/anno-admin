import React from "react";

import "amis/lib/themes/antd.css";
import "amis/lib/helper.css";
import "amis/sdk/iconfont.css";
import "./index.less";
import "/src/styles/amisCommon.less";
import "@fortawesome/fontawesome-free/css/all.css";
import axios from "axios";
import copy from "copy-to-clipboard";

import { render as renderAmis, Schema } from "amis";
import { useParams, useSearchParams } from "react-router-dom";
import { apiGetAmisJson } from "@/api/modules/login";
import { message, Modal } from "antd";
import { ExclamationCircleOutlined } from "@ant-design/icons";

// amis 环境配置
export const amisEnv: any = {
	// 下面三个接口必须实现
	fetcher: ({
		url, // 接口地址
		method, // 请求方法 get、post、put、delete
		data, // 请求数据
		responseType,
		config, // 其他配置
		headers // 请求头
	}: any) => {
		url = import.meta.env.VITE_API_URL + url;
		config = config || {};
		config.withCredentials = true;
		responseType && (config.responseType = responseType);

		if (config.cancelExecutor) {
			config.cancelToken = new (axios as any).CancelToken(config.cancelExecutor);
		}

		config.headers = headers || {};
		let annoTokenKey = window.localStorage.getItem("anno-token-key") || "anno-token";
		// 手动设置token请求头
		config.headers[annoTokenKey] =window.localStorage.getItem("anno-token") || "";

		if (method !== "post" && method !== "put" && method !== "patch") {
			if (data) {
				config.params = data;
			}
			return (axios as any)[method](url, config);
		} else if (data && data instanceof FormData) {
			config.headers = config.headers || {};
			config.headers["Content-Type"] = "multipart/form-data";
		} else if (data && typeof data !== "string" && !(data instanceof Blob) && !(data instanceof ArrayBuffer)) {
			data = JSON.stringify(data);
			config.headers = config.headers || {};
			config.headers["Content-Type"] = "application/json";
		}

		return (axios as any)[method](url, data, config);
	},
	isCancel: (value: any) => (axios as any).isCancel(value),
	copy: (content: string) => {
		copy(content);
		message.success("内容已复制到粘贴板");
	},
	notify: (type: "error" | "success" /**/, msg: string /*提示内容*/) => {
		if (type === "success") {
			message.success(msg);
			return;
		}
		if (type === "error") {
			message.error(msg);
			return;
		} else {
			message.error(msg);
			return;
		}
	},
	alert: (msg: string) => {
		message.error(msg);
	},
	confirm: (msg: string) => {
		// eslint-disable-next-line @typescript-eslint/no-unused-vars
		return new Promise((resolve, reject) => {
			Modal.confirm({
				icon: <ExclamationCircleOutlined />,
				content: msg,
				okText: "确认",
				okType: "danger",
				cancelText: "取消",
				onOk() {
					return resolve(true);
				},
				onCancel() {
					return resolve(false);
				}
			});
		});
	},
	theme: "antd"
};

export class AMISComponent extends React.Component<any, any> {
	state = {
		amisSchema: {} as Schema,
		properties: {}
	};
	refreshData = (targetClass?: any, param?: object, defaultProps?: object) => {
		apiGetAmisJson(targetClass, param).then((res: any) => {
			this.setState({
				amisSchema: res.data.amisJSON,
				properties: { ...res.data.properties, ...defaultProps }
			});
		});
	};

	constructor(props: any) {
		super(props);
	}

	componentDidMount() {
		const { targetClass, param, defaultProps } = this.props;
		this.refreshData(targetClass, param || {}, defaultProps || {});
	}

	render() {
		const renderAmisJson = { ...this.state.amisSchema };
		renderAmisJson.data = this.state.properties;
		return renderAmis(
			// 这里是 amis 的 Json 配置。
			renderAmisJson,
			{
				data: this.state.properties
			},
			amisEnv
		);
	}
}

const AMIS = () => {
	const { id } = useParams();
	let [searchParams, setSearchParams] = useSearchParams();
	let params = Object.fromEntries(searchParams);
	let tokenKey = searchParams.get("tokenKey") || "anno-token";
	let tokenValue = searchParams.get("tokenValue") || "";
	if (tokenValue != "") {
		window.localStorage.setItem("anno-token", tokenValue);
	}
  window.localStorage.setItem("anno-token-key",tokenKey);
	return <AMISComponent key={id} targetClass={id} param={params} defaultProps={params} />;
};

// 直接渲染页面，加上路由参数，区分不同的页面
export default AMIS;
