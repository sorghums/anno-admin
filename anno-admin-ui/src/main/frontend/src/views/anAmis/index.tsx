import React from "react";

import "amis/lib/themes/antd.css";
import "amis/lib/helper.css";
import "amis/sdk/iconfont.css";
import "./index.less";
import "/src/styles/amisCommon.less";
import axios from "axios";
import copy from "copy-to-clipboard";

import { render as renderAmis, Schema } from "amis";
import { apiGetAmisJson } from "@/api/modules/login";
import { message } from "antd";

// amis 环境配置
const env: any = {
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
			message.info(msg);
			return;
		}
	},
	alert: (msg: string) => {
		message.error(msg);
	},
	confirm,
	theme: "antd"
};

export class AMISComponentV2 extends React.Component<any, any> {
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
			env
		);
	}
}
