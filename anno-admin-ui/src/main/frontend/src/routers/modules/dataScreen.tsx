import React from "react";
import lazyLoad from "@/routers/utils/lazyLoad";
import { RouteObject } from "@/routers/interface";

// 数据大屏模块
const dataScreenRouter: Array<RouteObject> = [
	{
		path: "/dataScreen/index",
		element: lazyLoad(React.lazy(() => import("@/views/dataScreen/index"))),
		meta: {
			requiresAuth: false,
			title: "数据大屏",
			key: "dataScreen",
			keepAlive: true
		}
	}
];

export default dataScreenRouter;
