import {Renderer} from "amis";
import React from "react";
import {AMISComponentV2} from "@/views/anAmis";

export class AnAmis extends React.Component<any, any> {
	render() {
		const { targetClass, param, defaultProps } = this.props;
		const key = targetClass + "-an-amis-" + Math.random();
		return <AMISComponentV2 key={key} targetClass={targetClass} param={param} defaultProps={defaultProps} anAmis={true} />;
	}
}

Renderer({
	type: "an-amis",
	autoVar: true
})(AnAmis);
