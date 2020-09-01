package com.zizhizhan.legacy.pattern.prototype.pipesandfilters;

public interface Filter<Data> {
	
	void process(Exchange<Data> exchange);

}
