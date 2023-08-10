// * 请求响应参数(不包含data)
export interface Result {
	code: string;
	msg: string;
}

// * 请求响应参数(包含data)
export interface ResultData<T = any> extends Result {
	data?: T;
}

// * 分页响应参数
export interface ResPage<T> {
	datalist: T[];
	pageNum: number;
	pageSize: number;
	total: number;
}

// * 分页请求参数
export interface ReqPage {
	pageNum: number;
	pageSize: number;
}

// * 登录
export namespace Login {
	export interface ReqLoginForm {
		mobile: string;
		password: string;
		captcha: {
			captchaCode: string;
			captchaKey: string;
		};
		code: string;
		codeKey: string;
	}
	export interface ResLogin {
		access_token: string;
	}
	export interface ResAuthButtons {
		[propName: string]: any;
	}

	export interface CaptchaRes {
		image: string;
		key: string;
	}

	export interface ResMe {
		avatar: string;
		name: string;
	}
}
