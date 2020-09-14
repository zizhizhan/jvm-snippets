package com.zizhizhan.groovy;

import com.google.common.io.ByteStreams;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 7/8/15
 *         Time: 12:32 AM
 */
public class GroovyLoaderTests {

    @Test
    public void useGroovyShell() throws Exception {
        Binding binding = new Binding();
        binding.setVariable("x", 2.3);
        binding.setVariable("y", 8);
        GroovyShell shell = new GroovyShell(binding);
        Object value = shell.evaluate("x + y");
        Assert.assertEquals(10.3, (double)value, 0.000001);

        binding.setVariable("a", Arrays.asList(1, 2, 3));
        value = shell.evaluate("a.each{ println it }");
        Assert.assertTrue(value instanceof List);

        List<Integer> list = (List<Integer>)value;

        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals((i + 1), list.get(i).intValue());
        }

        try (Reader reader = new InputStreamReader(
                GroovyLoaderTests.class.getResourceAsStream("/groovy/features.groovy"),
                StandardCharsets.UTF_8)) {
            String[] args = new String[0];
            shell.run(reader, "fileName", args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void useGroovyClassLoader() {
        GroovyClassLoader loader = new GroovyClassLoader();
        try {
            byte[] bytes = ByteStreams.toByteArray(GroovyLoaderTests.class.getResourceAsStream("/groovy/CalculateMax.groovy"));
            Class<?> groovyClass = loader.parseClass(new String(bytes, StandardCharsets.UTF_8));
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

            ArrayList<Integer> numbers = new ArrayList<>();
            numbers.add(1);
            numbers.add(10);
            numbers.add(16);
            Object[] arguments = { numbers };

            Object value = groovyObject.invokeMethod("getMax", arguments);
            Assert.assertEquals(16, value);
        } catch (CompilationFailedException | IOException | InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void useGroovyScriptEngine() {
        try {
            URL[] roots = new URL[] { GroovyLoaderTests.class.getResource("/groovy/") };
            GroovyScriptEngine gse = new GroovyScriptEngine(roots);

            Binding binding = new Binding();
            binding.setVariable("args", Collections.singletonList("Gweneth"));

            Object output = gse.run("Hello.groovy", binding);

            System.out.println(output);
            Assert.assertEquals("Hello Gweneth", output.toString());
        } catch (ResourceException | ScriptException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void useJSR223() throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        scriptEngineManager.registerEngineMimeType("groovy", new GroovyScriptEngineFactory());
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("groovy");

        scriptEngine.eval(new InputStreamReader(GroovyLoaderTests.class.getResourceAsStream("/groovy/CalculateMax.groovy")));
    }
}
