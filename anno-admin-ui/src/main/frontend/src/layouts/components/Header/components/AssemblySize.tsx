import {setAssemblySize} from "@/redux/modules/global/action";
import {connect} from "react-redux";

const AssemblySize = () => {
	return <div></div>;
};

const mapStateToProps = (state: any) => state.global;
const mapDispatchToProps = { setAssemblySize };
export default connect(mapStateToProps, mapDispatchToProps)(AssemblySize);
