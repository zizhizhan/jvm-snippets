package com.zizhizhan.legacies.pattern.interfaces;

public interface ChainOfResponsibility<Request> {
	void HandleRequest(Request request);
}
