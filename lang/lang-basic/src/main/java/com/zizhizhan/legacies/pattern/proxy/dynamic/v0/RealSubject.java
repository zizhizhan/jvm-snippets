package com.zizhizhan.legacies.pattern.proxy.dynamic.v0;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealSubject implements Subject{

	@Override
	public void handleRequest(Request req) {
		log.info("handleRequest {}", req);
	}

	@Override
	public void handleRequest2(Request req) {
		log.info("handleRequest2 {}", req);
	}

	@Override
	public void handleRequest3(Request req) {
		log.info("handleRequest3 {}", req);
	}

	@Override
	public void handleRequest4(Request req) {
		log.info("handleRequest4 {}", req);
	}

	@Override
	public void handleRequest5(Request req) {
		log.info("handleRequest5 {}", req);
	}
}
