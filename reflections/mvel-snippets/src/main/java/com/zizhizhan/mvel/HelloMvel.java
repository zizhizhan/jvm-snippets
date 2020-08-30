package com.zizhizhan.mvel;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.ast.ASTNode;
import org.mvel2.integration.Interceptor;
import org.mvel2.integration.VariableResolverFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HelloMvel {

    public static void main(String[] args) {
        ParserContext context = new ParserContext();

        Map<String, Interceptor> interceptors = new HashMap<>();
        Interceptor logInterceptor = new Interceptor() {
            public int doBefore(ASTNode node, VariableResolverFactory factory) {
                log.info("Before {}.", node);
                return 0;
            }
            public int doAfter(Object value, ASTNode node, VariableResolverFactory factory) {
                log.info("After {}.", node);
                return 0;
            }
        };

        interceptors.put("logging", logInterceptor);
        context.setInterceptors(interceptors);

        String expression = "@logging (a + b * c)";

        Serializable compiledExpression = MVEL.compileExpression(expression, context);

        Map vars = ImmutableMap.of("a", 3, "b", 7, "c", 5);
        int value = (int)MVEL.executeExpression(compiledExpression, vars);
        System.out.println(value);
    }
}


