import logo from "@/assets/images/logo.png";
import { connect } from "react-redux";
import {useEffect, useState} from "react";
import {apiGetGlobalConfig, goGlobalConfig} from "@/api/modules/config";

const Logo = (props: any) => {
	const { isCollapse } = props;
	const [globalConfig, setGlobalConfig] = useState({
		title: ""
	});

	useEffect(() => {
		goGlobalConfig(setGlobalConfig);
	}, []);
	return (
		<div className="logo-box">
			<img src={logo} alt="logo" className="logo-img" />
			{!isCollapse ? <h2 className="logo-text">{globalConfig.title}</h2> : null}
		</div>
	);
};

const mapStateToProps = (state: any) => state.menu;
export default connect(mapStateToProps)(Logo);
