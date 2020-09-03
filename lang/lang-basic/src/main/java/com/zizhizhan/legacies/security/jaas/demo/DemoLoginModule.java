package com.zizhizhan.legacies.security.jaas.demo;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class DemoLoginModule implements LoginModule {
	
	Subject subject;
	CallbackHandler callbackHandler;
	Map<String, ?> sharedState;
	Map<String, ?> options;
	boolean valid;
	String name;
	String password;
	TestPrincipal namePrincipal;
	TestPrincipal emailPrincipal;
	
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;
	}

	@Override
	public boolean login() throws LoginException {
		valid = false;
		try{
			Callback[] callbacks = new Callback[2];
			callbacks[0] = new NameCallback("name: ");
			callbacks[1] = new PasswordCallback("password:", false);
			
			callbackHandler.handle(callbacks);
			
			name = ((NameCallback)callbacks[0]).getName();
			password = new String(((PasswordCallback)callbacks[1]).getPassword());
			if((name.equals("art")) && (password.equals("yes"))){
				valid = true;
			}else{
				valid = false;
			}
			
		}catch(IOException ex){
			ex.printStackTrace();
		} catch (UnsupportedCallbackException e) {			
			e.printStackTrace();
		}
		
		return valid;
	}
	
	@Override
	public boolean commit() throws LoginException {

		if (valid) {
			namePrincipal = new TestPrincipal();
			namePrincipal.setName(name);
			subject.getPrincipals().add(namePrincipal);

			emailPrincipal = new TestPrincipal();
			emailPrincipal.setName("zhiqiangzhan@gmail.com");
			subject.getPrincipals().add(emailPrincipal);
		}
		name = null;
		password = null;
		return valid;
	}
	
	@Override
	public boolean abort() throws LoginException {
		boolean retVal = false;
		if(valid){
			subject.getPrincipals().remove(namePrincipal);
			subject.getPrincipals().remove(emailPrincipal);	
			retVal = true;
		}else{			
			retVal = false;
		}
		name = null;
		password = null;
		return retVal;
	}	
	
	@Override
	public boolean logout() throws LoginException {
		if(valid){
			subject.getPrincipals().remove(namePrincipal);
			subject.getPrincipals().remove(emailPrincipal);
		}
		name = null;
		password = null;
		valid = false;
		return true;
	}

}
