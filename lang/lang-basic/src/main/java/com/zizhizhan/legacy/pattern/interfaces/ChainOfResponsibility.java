package com.zizhizhan.legacy.pattern.interfaces;

public interface ChainOfResponsibility<Request> {
	void HandleRequest(Request request);
}
