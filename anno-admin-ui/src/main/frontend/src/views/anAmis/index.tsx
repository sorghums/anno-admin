import React from "react";

import "amis/lib/themes/antd.css";
import "amis/lib/helper.css";
import "amis/sdk/iconfont.css";
import "./index.less";
import "/src/styles/amisCommon.less";


import { render as renderAmis, Schema } from "amis";
import { apiGetAmisJson } from "@/api/modules/login";
import {amisEnv} from "@/views/amis";



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
			amisEnv
		);
	}
}
