
## 第二章 Java 程序设计环境

Java 术语

| 术语名 | 缩写 | 解释 |
|----|----|----|
| Java Development Kit | JDK | 编写Java程序的程序员使用的软件 |
| Java Runtime Environment | JRE | 运行Java程序的用户使用的软件 |
| Server JRE | - | 在服务器上运行Java程序的软件 |
| Standard Edition | SE | 用于桌面或简单服务器应用的Java平台 |
| Enterprise Edition | EE | 用于复杂服务器应用的Java平台 |
| Micro Edition | ME | 用于手机和其他小型设备的Java平台 |
| Java FX | - | 用于图形化用户界面的一个替代工具包 |
| OpenJDK | - | Java SE 的一个免费开源实现，不包含浏览器集成或JavaFX |
| NetBeans | - | Oracle的集成开发环境 |

JDK（Java SE开发包）是Java Development Kit的缩写，这个工具包的版本1.2 ~ 1.4被称为
java SDK（软件开发包，Software Development Kit）。另外，还有一个术语是
Java运行时环境（JRE），它包含虚拟机但不包含编译器。这并不是开发者想要的
环境，而是专门为不需要编译器的用户而提供。

接下来，Java SE会大量出现，相对于Java EE（Enterprise Edition）和Java ME
（Micro Edition）它是Java的标准版。

Java SE对于Windows或Linux，需要在x86（32位）和x64（64位）版本之间作出选择，
应当选择与你的操作系统体系结构匹配的版本。

安装JDK，可以如下测试是否正确安装
```
javac -version // javac 1.8.0_231
```

编译并运行第一个Java程序
```
javac Welcome.java
java Welcome
```
javac程序是一个Java编译器，它将文件Welcome.java编译成Welcome.class。
java程序启动java虚拟机，虚拟机执行编译器放在class文件中的字节码。

本章中，我们学习了有关编译和运行Java程序的机制。

















