package com.zizhizhan.legacies.pattern.proxy.hybrid;

public class SimpleSubject implements Subject{

	Subject subject;		

	public SimpleSubject() {		
	}
	
	public SimpleSubject(Subject subject) {		
		this.subject = subject;
	}

	public void request() {
		preRequest();
		try {
			if (subject == null) {
				subject = new RealSubject();
			}
			subject.request();
		} finally {
			postRequest();
		}
	}

	private void preRequest() {
		System.out.println("PreRequest");
	}

	private void postRequest() {
		System.out.println("PostRequest");
	}
	
}
