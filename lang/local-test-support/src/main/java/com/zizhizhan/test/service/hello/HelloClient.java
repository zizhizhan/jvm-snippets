package com.zizhizhan.test.service.hello;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloClient {
	
	private HelloService helloService;
	
	public void setHelloService(HelloService helloService){
		this.helloService = helloService;
	}	
	
	public void exec(){
		log.info("helloService#sayHello:");
		helloService.sayHello("XP");
		log.info("helloService#echo(XP) = {}.", helloService.echo("XP"));
		log.info("helloService#echo(Test) = {}.", helloService.echo("Test"));
		log.info("helloService#echo(XP) = {}.", helloService.echo("XP"));
		log.info("helloService#echo(Test) = {}.", helloService.echo("Test"));
		log.info("helloService#echo(XP) = {}.", helloService.echo("XP"));
		log.info("helloService#echo(Test) = {}.", helloService.echo("Test"));
	}

}
