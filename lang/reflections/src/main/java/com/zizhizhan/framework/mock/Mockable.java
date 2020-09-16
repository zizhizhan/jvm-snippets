package com.zizhizhan.framework.mock;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM11:06
 * To change this template use File | Settings | File Templates.
 */
public interface Mockable {

	void replay();

	void andReturn(Object o);

	void verify();
}
