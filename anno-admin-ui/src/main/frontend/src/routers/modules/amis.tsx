import React from "react";
import lazyLoad from "@/routers/utils/lazyLoad";
import {RouteObject} from "@/routers/interface";
import {LayoutIndex} from "@/routers/constant";

// amis页面模块
const amisRouter: Array<RouteObject> = [
	{
		element: <LayoutIndex />,
		meta: {
			title: "AMIS"
		},
		children: [
			{
				path: "/amisDemo/index/:id",
				element: lazyLoad(React.lazy(() => import("@/views/amis/index"))),
				meta: {
					requiresAuth: true,
					title: "amis示例",
					key: "amisDemo",
					keepAlive: true
				}
			}
		]
	}
];

export default amisRouter;
