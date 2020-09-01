package com.zizhizhan.legacy.pattern.proxy.simple;

public class SimpleSubject implements Subject{

	Subject subject;		

	public SimpleSubject() {		
	}
	
	public SimpleSubject(Subject subject) {		
		this.subject = subject;
	}

	public void request() {
		preRequest();
		
		if(subject == null){
			subject = new RealSubject();			
		}
		subject.request();
		
		postRequest();		
	}

	private void postRequest() {
		
		System.out.println("pre Request");
				
	}

	private void preRequest() {
		
		System.out.println("post Request");
	}
	
	
	
}
