package com.zizhizhan.framework.mock;

import com.zizhizhan.test.service.hello.HelloClient;
import com.zizhizhan.test.service.hello.HelloService;

import org.junit.Test;
import static com.zizhizhan.framework.mock.MockControl.*;

public class MockSystemTests {
	
	@Test
	public void testMockSystem() {
		HelloService helloService = createMock(HelloService.class);
		HelloClient helloClient = new HelloClient();
		helloClient.setHelloService(helloService);

		helloService.sayHello("sayHello");
		expect(helloService.echo("Hello")).andReturn("Hello World!");
		expect(helloService.echo("Hello")).andReturn("This is message is returned by MockedObject.");
		expect(helloService.echo("Hello")).andReturn("MyTest.");
		expect(helloService.echo("Hello")).andReturn("Invoke HelloService echo.");
		expect(helloService.echo("Hello")).andReturn("Just test for fun.");
		expect(helloService.echo("Hello")).andReturn("Test ending.");

		replay(helloService);

		helloClient.exec();
		verify(helloService);
	}

}
