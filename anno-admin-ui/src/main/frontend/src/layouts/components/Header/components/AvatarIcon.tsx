import React, {useRef} from "react";
import {Avatar, Dropdown, Menu, message, Modal} from "antd";
import {ExclamationCircleOutlined, UserOutlined} from "@ant-design/icons";
import {useNavigate} from "react-router-dom";
import {HOME_URL} from "@/config/config";
import {connect} from "react-redux";
import {setToken} from "@/redux/modules/global/action";
import PasswordModal from "./PasswordModal";
import InfoModal from "./InfoModal";
import avatar from "@/assets/images/avatar.png";
import {apiLogout, apiRefreshUserInfo} from "@/api/modules/login";

const AvatarIcon = (props: any) => {
	const { setToken } = props;
	const navigate = useNavigate();

	interface ModalProps {
		showModal: (params: { name: number }) => void;
	}
	const passRef = useRef<ModalProps>(null);
	const infoRef = useRef<ModalProps>(null);

	// 退出登录
	const logout = () => {
		Modal.confirm({
			title: "温馨提示 🧡",
			icon: <ExclamationCircleOutlined />,
			content: "是否确认退出登录？",
			okText: "确认",
			cancelText: "取消",
			onOk: () => {
				setToken("");
				window.localStorage.removeItem("anno-token");
				apiLogout().then((res) => {
					message.success("退出登录成功！");
					navigate("/login");
				});
			}
		});
	};

	// Dropdown Menu
	const menu = (
		<Menu
			items={[
				{
					key: "1",
					label: <span className="dropdown-item">首页</span>,
					onClick: () => navigate(HOME_URL)
				},
				{
					key: "2",
					label: <span className="dropdown-item">个人信息</span>,
					onClick: () => infoRef.current!.showModal({ name: 11 })
				},
				{
					key: "3",
					label: <span className="dropdown-item">修改密码</span>,
					onClick: () => passRef.current!.showModal({ name: 11 })
				},
				{
					key: "1001",
					label: <span className="dropdown-item">清除缓存</span>,
					onClick: () => {
						apiRefreshUserInfo().then((res) => {
							message.success("清除缓存成功！");
						});
					}
				},
				{
					type: "divider"
				},
				{
					key: "4",
					label: <span className="dropdown-item">退出登录</span>,
					onClick: logout
				}
			]}
		></Menu>
	);
	// 获取上层组件传递的头像对象 headAvatar
	const { headAvatar } = props;
	const finalHeadAvatar = headAvatar || avatar;
	return (
		<>
			<Dropdown overlay={menu} placement="bottom" arrow trigger={["click"]}>
				<Avatar icon={<UserOutlined/>} src={finalHeadAvatar}/>
			</Dropdown>
			<InfoModal innerRef={infoRef}></InfoModal>
			<PasswordModal innerRef={passRef}></PasswordModal>
		</>
	);
};

const mapDispatchToProps = { setToken };
export default connect(null, mapDispatchToProps)(AvatarIcon);
