<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 工作流节点状态：1-尚未运行；2-正在运行；3-运行成功；4-运行失败--%>
<c:set var="dic_workNodeStatus" value="{'NOTRUN':'尚未运行','RUNNING':'运行中','SUCC':'运行成功','FAIL':'运行失败'}" scope="request" />
