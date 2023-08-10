import { Layout } from "antd";
import AvatarIcon from "./components/AvatarIcon";
import CollapseIcon from "./components/CollapseIcon";
import BreadcrumbNav from "./components/BreadcrumbNav";
import AssemblySize from "./components/AssemblySize";
import Theme from "./components/Theme";
import "./index.less";
import {useEffect, useState} from "react";
import { apiMe } from "@/api/modules/login";

const LayoutHeader = () => {
	const { Header } = Layout;
	const [userName, setUserName] = useState("AnnoAdmin");
	const [headAvatar, setHeadAvatar] = useState("https://solon.noear.org/img/solon/favicon.png");
	// 发送请求获取用户信息
	useEffect(() => {
		apiMe().then(
            (res) => {
                setUserName(res.data?.name as string);
                setHeadAvatar(res.data?.avatar as string);
			}
		)
	}, []);
	return (
		<Header>
			<div className="header-lf">
				<CollapseIcon />
				<BreadcrumbNav />
			</div>
			<div className="header-ri">
				<AssemblySize />
				<Theme />
				<span className="username">
					{userName}
				</span>
				<AvatarIcon headAvatar = {headAvatar}/>
			</div>
		</Header>
	);
};

export default LayoutHeader;
