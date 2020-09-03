package com.zizhizhan.legacies.thirdparty.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name="HelloService")
@SOAPBinding(parameterStyle= SOAPBinding.ParameterStyle.BARE)
public interface HelloService {
		
	@WebMethod(operationName="echoMethod")
	@WebResult(header=true, name="echoResponse", partName="echoResponsePartName")
	String echo(@WebParam(name="echoRequest", partName="echoRequestPartName") String msg);
	
	@WebMethod(operationName="helloMethod")
	@WebResult(name="helloResponse", partName="helloResponsePartName")
	String hello(@WebParam(name="helloRequest", partName="helloRequestPartName")String msg);
	
	@WebMethod(operationName="callMethod")
	@WebResult(name="callResponse", partName="callResponsePartName")
	String callService(@WebParam(name="callRequest", partName="callRequestPartName")String request);
	
	@WebMethod(operationName="throwMethod")
	void throwException() throws Exception;

}
