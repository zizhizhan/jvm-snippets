
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>repackage</id>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <mainClass>me.jameszhan.spring.boot.Application</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

如果不指定 `mainClass`，`spring-boot-maven-plugin` 插件则会自动找寻项目中唯一的一个被 `@SpringBootApplication` 
标注的类并且带有 `main` 入口函数的类作为 `mainClass`。

最终生成的样例如下：

`META-INF/MAINIFEST.MF`

```ini
Manifest-Version: 1.0
Built-By: james
Start-Class: me.jameszhan.spring.boot.Application
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Spring-Boot-Version: 2.1.1.RELEASE
Created-By: Apache Maven 3.6.0
Build-Jdk: 1.8.0_45
Main-Class: org.springframework.boot.loader.JarLauncher
```


```bash
mvn package && java -jar target/spring-boot-1.0-SNAPSHOT.jar
```

或者

```bash
mvn spring-boot:run
```