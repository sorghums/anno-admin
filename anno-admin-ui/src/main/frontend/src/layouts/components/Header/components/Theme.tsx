import {Divider, Drawer, Switch} from "antd";
import {useEffect, useState} from "react";
import {connect} from "react-redux";
import {FireOutlined, SettingOutlined} from "@ant-design/icons";
import {setThemeConfig} from "@/redux/modules/global/action";
import {updateCollapse} from "@/redux/modules/menu/action";

const Theme = (props: any) => {
	const [visible, setVisible] = useState<boolean>(false);
	const { setThemeConfig, updateCollapse } = props;
	const { isCollapse } = props.menu;
	const { themeConfig } = props.global;
	const { weakOrGray, breadcrumb, tabs } = themeConfig;

	const setWeakOrGray = (checked: boolean, theme: string) => {
		if (checked) return setThemeConfig({ ...themeConfig, weakOrGray: theme });
		setThemeConfig({ ...themeConfig, weakOrGray: "" });
	};

	const onChange = (checked: boolean, keyName: string) => {
		return setThemeConfig({ ...themeConfig, [keyName]: !checked });
	};

	useEffect(() => {
		console.log("themeConfig", themeConfig);
	});
	return (
		<>
			<i
				className="icon-style iconfont icon-zhuti"
				onClick={() => {
					setVisible(true);
				}}
			></i>
			<Drawer
				title="布局设置"
				closable={false}
				onClose={() => {
					setVisible(false);
				}}
				visible={visible}
				width={320}
			>
				{/* 全局主题 */}
				<Divider className="divider">
					<FireOutlined />
					全局主题
				</Divider>
				<div className="theme-item">
					<span>灰色模式</span>
					<Switch
						checked={weakOrGray === "gray"}
						onChange={e => {
							setWeakOrGray(e, "gray");
						}}
					/>
				</div>
				<div className="theme-item">
					<span>色弱模式</span>
					<Switch
						checked={weakOrGray === "weak"}
						onChange={e => {
							setWeakOrGray(e, "weak");
						}}
					/>
				</div>
				<br />
				{/* 界面设置 */}
				<Divider className="divider">
					<SettingOutlined />
					界面设置
				</Divider>
				<div className="theme-item">
					<span>折叠菜单</span>
					<Switch
						checked={isCollapse}
						onChange={e => {
							updateCollapse(e);
						}}
					/>
				</div>
				<div className="theme-item">
					<span>面包屑导航</span>
					<Switch
						checked={!breadcrumb}
						onChange={e => {
							onChange(e, "breadcrumb");
						}}
					/>
				</div>
				<div className="theme-item">
					<span>标签栏</span>
					<Switch
						checked={!tabs}
						onChange={e => {
							onChange(e, "tabs");
						}}
					/>
				</div>
			</Drawer>
		</>
	);
};
const mapStateToProps = (state: any) => state;
console.log(mapStateToProps);
const mapDispatchToProps = { setThemeConfig, updateCollapse };
export default connect(mapStateToProps, mapDispatchToProps)(Theme);
