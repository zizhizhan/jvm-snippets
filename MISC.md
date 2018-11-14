### Java SPI 约定

- 当服务的提供者，提供了接口的一种具体实现后，在jar包的META-INF/services/目录中创建一个以“接口全限定名”为命名的文件，内容为实现类的全限定名。
- SPI所在的jar放在主程序的classpath中
- 外部程序通过java.util.ServiceLoader动态装载实现模块，它通过扫描META-INF/services目录下的配置文件找到实现类的全限定名，把类加载到jvm。
    * 注意：SPI的实现类必须带一个不带参数的构造方法