import LoginForm from "./components/LoginForm";
import loginLeft from "@/assets/images/login_left.png";
import logo from "@/assets/images/logo.png";
import "./index.less";
import {useEffect, useState} from "react";
import {apiGetGlobalConfig, goGlobalConfig} from "@/api/modules/config";

const Login = () => {
	const [globalConfig, setGlobalConfig] = useState({
		title: ""
	});

	useEffect(() => {
		goGlobalConfig(setGlobalConfig);
	}, []);


	return (
		<div className="login-container">
			<div className="login-box">
				<div className="login-left">
					<img src={loginLeft} alt="login" />
				</div>
				<div className="login-form">
					<div className="login-logo">
						<img className="login-icon" src={logo} alt="logo" />
						<span className="logo-text">{globalConfig.title}</span>
					</div>
					<LoginForm />
				</div>
			</div>
		</div>
	);
};

export default Login;
