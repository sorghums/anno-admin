import {connect} from "react-redux";
import {setLanguage} from "@/redux/modules/global/action";

const Language = () => {
	return <div></div>;
};

const mapStateToProps = (state: any) => state.global;
const mapDispatchToProps = { setLanguage };
export default connect(mapStateToProps, mapDispatchToProps)(Language);
