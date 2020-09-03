package com.zizhizhan.legacies.pattern.pipesandfilters;

public interface Filter<Data> {
	
	void process(Exchange<Data> exchange);

}
