import React, {useEffect, useState} from "react";
import {Input} from "antd";
import {SafetyCertificateOutlined} from "@ant-design/icons";
import {apiGetCaptcha} from "@/api/modules/login";

interface CaptchaInputValue {
	captchaCode?: string;
	captchaKey?: string;
}

interface CaptchaInputProps {
	value?: CaptchaInputValue;
	onChange?: (value: CaptchaInputValue) => void;
}

const CaptchaInput: React.FC<CaptchaInputProps> = ({
	value = {
		captchaCode: "",
		captchaKey: ""
	},
	onChange
}) => {
	const [captchaCode, setCaptchaCode] = useState<string>("");
	const [captchaKey, setCaptchaKey] = useState<string>("");
	const [imageData, setImageData] = useState<string>("");

	// 触发改变
	const triggerChange = (changedValue: { captchaCode?: string; captchaKey?: string }) => {
		if (onChange) {
			onChange({ captchaCode, captchaKey, ...value, ...changedValue });
		}
	};

	useEffect(() => {
		apiGetCaptcha().then(res => {
			// @ts-ignore
			const { image, key } = res.data;
			setCaptchaKey(key);
			setImageData(image);
			triggerChange({ captchaKey: key });
		});
	}, []);

	// 输入框变化
	const onChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
		const code = e.target.value || "";
		if (code != "") {
			setCaptchaCode(code);
		}
		triggerChange({ captchaCode: code });
	};

	// 时间类型变化
	const onClickImage = () => {
		apiGetCaptcha().then(res => {
			// @ts-ignore
			const { image, key } = res.data;
			setCaptchaKey(key);
			setImageData(image);
			triggerChange({ captchaKey: key });
		});
	};

	return (
		<span>
			<Input.Group compact>
				<Input
					prefix={<SafetyCertificateOutlined />}
					placeholder={"请输入验证码"}
					onChange={onChangeInput}
					style={{ width: "75%", marginRight: 5, padding: "6.5px 11px 6.5px 11px", verticalAlign: "middle" }}
				/>
				<img
					style={{ width: "23%", height: "35px", verticalAlign: "middle", padding: "0px 0px 0px 0px" }}
					src={imageData}
					onClick={onClickImage}
				/>
			</Input.Group>
		</span>
	);
};
export default CaptchaInput;
