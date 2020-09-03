package com.zizhizhan.legacies.thirdparty.books.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookRequest;
import com.zizhizhan.legacies.thirdparty.books.valuetypes.BookResponse;

@WebService
public interface BookService {
	
	@WebMethod
	@WebResult(name="bookResponse")
	@SOAPBinding(parameterStyle=ParameterStyle.BARE)
	BookResponse entryPoint(@WebParam(name="bookRequest") BookRequest request);
	
	@WebMethod
	@WebResult(name="bookResponse")
	BookResponse search(@WebParam(name="bookRequest") BookRequest request);

}
