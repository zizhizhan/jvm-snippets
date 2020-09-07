package com.zizhizhan.legacies.pattern.springmvc.impl;

import com.zizhizhan.legacies.pattern.springmvc.View;
import com.zizhizhan.legacies.pattern.springmvc.ViewResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultViewResolver implements ViewResolver {
    private String prefix = "";
    private String suffix = "";
    private Class<View> viewClass;

    @Override
    public View resolveViewName(String viewName) throws Exception {
        InternalResourceView view = new InternalResourceView();
        view.setUrl(prefix + viewName + suffix);
        return view;
    }

    public void setViewClass(String viewClass) {
        try {
            this.viewClass = (Class<View>) this.getClass().getClassLoader().loadClass(viewClass);
        } catch (ClassNotFoundException e) {
            log.error("Can't load view class {}.", viewClass, e);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
