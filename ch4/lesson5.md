
## 包

Java允许使用包（package）将类组织起来。借助于包可以方便地组织自己的代码，并将自己的代码
与别人提供的代码库分开管理。
标准的Java类库分布在多个包中，包括java.lang、java.util和java.net等。标准的Java包具有一个
层次结构，如同硬盘的目录嵌套一样，也可以使用嵌套层次组织包。所有标准的Java包都处于java和javax
包层次中。

使用包的主要原因是确保类名的唯一性。假如两个程序员不约而同地建立了Employee类，只要将这些类放置
在不同的包中，就不会产生冲突。事实上，为了保证包名的绝对唯一性，Sun公司建议将公司的因特网域名以逆序
的形式作为包名，并且对于不同的项目使用不同的子包。例如，horstmann.com逆序形式为com.horstmann,
这个包还可以被进一步划分为子包，如com.horstmann.corejava。

从编译器的角度来看，嵌套的包之间没有任何关系。例如 java.util包与java.util.jar包毫无关系。

#### 类的导入

一个类可以使用所属包中的所有类，以及其他包中的公有类（public class）。

import语句是一种引用包含在包中的类的简明描述，一旦使用了import语句，在使用类时，就不必写出包的
全名了。
可以使用import语句导入一个特定的类或者整个包，import语句应该位于源文件的顶部（但位于package语句后面）。
可以使用下面这条语句导入java.util包中的所有的类。
```
import java.time.*;
```
然后就可以使用
```
LocalDate tody = LocalDate.now();
```
还可以导入一个包中的特定类
```
import java.time.LocalDate;
```
java.time.*的语法比较简单，对代码的大小也没有任何负面影响。当然，如果能够明确地指出所导入的类，
将会使代码的读者更加准确地知道加载了哪些类。

但是，需要注意的是，只能使用星号*导入一个包，而不能使用import java.*或import java.*.*
导入以java为前缀的所有包。

在大多数情况下，只导入所需的包，并不必过多地理睬它们。但在发生命名冲突的时候，就不能不注意包的名字了。
例如java.util和java.sql包都有日期Date类，如果在程序中导入了这两个包
```
import java.util.*;
import java.sql.*;
```
在程序使用Date类的时候，就会出现一个编译错误
```
Date tody; // Error java.util.Date or java.sql.Date?
```
此时编译器无法确定程序使用的是哪一个Date类，可以采用增加一个特定的import语句来解决这个问题
```
import java.util.*;
import java.sql.*;
import java.util.Date;
```
如果这两个Date类都需要使用，在每个类的名字前面加上完整的包名
```
java.util.Date deadline = new java.util.Date(...);
java.sql.Date tody = new java.sql.Date(...);
```
在包中定位类是编译器（compiler）的工作，类文件中的字节码肯定使用完整的包名类引用其他类。

C++程序员经常将import和#include弄混，实际上，这两者之间没有共同之处。在C++中必须
使用#include将外部特性的声明加载进来，这是因为C++编译器无法查看任何文件的内部，除了正在编译的
文件以及在头文件中明确包含的文件。Java编译器可以查看其他文件的内部，只要告诉它到哪里去查看就可以了。

在C++中，与包机制类似的是命名空间（namespace），在Java中package与import语句类似于C++中的
namespace和using指令。

#### 静态导入

import语句不仅可以导入类，还增加了导入静态方法和静态域的功能。

例如在源文件的顶部添加一条指令
```
import static java.lang.System.*;
```
就可以使用System类的静态方法和静态域，而不必加类名前缀
```
out.println("Goodbye World!"); // System.out
exit(0); // System.exit
```
另外，还可以导入特定的方法或域
```
import static java.lang.System.out;
```

#### 将类放入包中

要想将一个类放入包中，就必须将包的名字放在源文件的开头，类代码之前。
```
package com.horstmann.corejava;

public class Employee
{
    ...
}
```
如果没有在源文件中放置package语句，这个源文件中的类就被放置在一个默认包中（default package）。
默认包是一个没有名字的包。
将包中的文件放到与完整的包名匹配的子目录中，例如，com.horstmann.corejava包中的所有源文件
应该被放置在子目录com/horstmann/corejava中，编译器将类文件也放在相同的目录结构中。

如果包与目录不匹配，虚拟机就找不到类。

#### 包作用域

前面已经接触过访问修饰符public和private，标记为public的部分可以被任意的类使用，标记为private
的部分只能被定义它们的类使用。如果没有指定public或private这个部分（类、方法、变量）可以被同一个
包中的所有的方法访问。这对于类来说这种默认是合乎情理的，但是对于变量来说就有些不合适宜了，因此
变量必须显式地标记为private，不然的话默认为包可见，显然这样做会破坏封装性。

在默认情况下，包不是一个封闭的实体，也就是说，任何人都可以向包中添加更多的。当然，有敌意或低水平
的程序员很可能利用包的可见性添加一些具有修改变量功能的代码。

从1.2版本开始，JDK的实现者修改了类加载器，明确地禁止加载用户自定义的包名以".java"开始的类。
但是，用户自定义的类无法从这种保护中受益。然而，可以通过包密封（package sealing）机制来解决
将各种包混杂在一起的问题。

## 类路径

在前面已经看到，类存储在文件系统的子目录中，类的路径必须与包名匹配。

另外，类文件也可以存储在JAR（Java归档）文件中。在一个JAR文件中，可以包含多个压缩形式的类文件
和子目录，这样既可以节省又可以改善性能。在程序中用到第三方的库文件时，通常会给出一个或多个需要包含
的JAR文件。JDK也提供了许多的JAR文件，例如在jre/lib/rt.jar中包含了数千个类库文件。

JAR文件使用ZIP格式组织文件和子目录，可以使用所有ZIP实用程序查看内部的rt.jar以及其他的JAR文件。

为了使类能够被多个程序共享，需要做到下面几点：

1）把类放到一个目录中，例如/home/user/classdir，需要注意，这个目录是包树状结构的基目录。
如果希望将com.horstmann.corejava.Employee类添加到其中，这个Employee.class类文件就必须
位于子目录/home/user/classdir/com/horstmann/corejava中。

2）将JAR文件放在一个目录中。例如 /home/user/archives。

3）设置类路径。类路径是所有包含类文件的路径集合，在UNIX环境中，类路径中的不同项目之间采用`:`分隔
``/home/user/classdir:.:/home/user/archives/archive.jar``。（而在Windows环境中，则以
`;`分隔）。
上述中`.`表示当前目录。

类路径包括

- 基目录 /home/user/classdir
- 当前目录`.`
- JAR文件 /home/user/archives/archive.jar

从JavaSE6开始，可以在JAR文件目录中指定通配符
```
/home/user/archives/'*'
```
在archives目录中的所有JAR文件（但不包括.class文件）都包含在类路径中。
由于运行时库文件（rt.jar、jre/lib和jre/lib/ext目录下的JAR文件）会被自动地搜索，所以
不必将它们显式地列在类路径中。

**警告：**javac编译器总是在当前的目录中查找文件，但Java虚拟机仅在类路径中又'.'目录的时候
才查看当前目录。如果没有设置类路径，那也并不会产生问题，默认的类路径包含'.'目录。然而如果
设置了类路径却忘记了包含'.'目录，则程序能通过编译，但不能运行。

假定虚拟机要搜寻com.horstmann.corejava.Employee类文件，它首先要查看存储在jre/lib和
jre/lib/ext目录下的归档文件，然后再查看类路径。

编译器定位文件要比虚拟机复杂得多。如果引用了一个类，而没有指出这个类所在的包，那么编译器首先查找
包含这个类的包，并查询所有的import指令，确定其中是否包含了被引用的类（java.lang包被默认导入源文件）。
对这个类路径的所有位置中import包每一个类进行逐一查看，如果找到一个以上的类，就产生编译错误。
因为类必须是唯一的，而import语句的次序却无关紧要。

编译器的任务不止这些，它还要查看源文件是否比类文件新，如果是这样的话，那么源文件就会被自动地
重新编译。

前面我们知道，仅可以导入其他包中的公有类。一个源文件只能包含一个公有类，并且文件名必须与共有类匹配。
因此，编译器很容易定位公有类所在的源文件。当然，也可以从当前包中导入非公有类，这些类有可能定义
在与类名不同的源文件中。如果从当前包中导入一个类，编译器就要搜索当前包中的所有源文件，以便确定
哪个源文件定义了这个类。

#### 设置类路径

最好采用-classpath（或-cp）指定类路径
```
$ java -classpath /home/user/classdir:.:/home/user/archives/'*' 
```





























