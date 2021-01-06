
## 文档注释

JDK包含一个很有用的工具，叫javadoc，它可以有源文件生成一个HTML文档。

如果在源代码中添加专用的定界符`/**`开始的注释，那么可以很容易地生成一个看上去具有专业水准的文档。
这是一种很好的方法，因为这种方式可以将代码与注释保存在一个地方。如果将文档存入一个独立的文件中，
就会随着时间的推移，出现代码和注释不一致的问题。由于文档注释与源代码在同一个文件中，在修改源代码
的同时，重新运行javadoc就可以轻而易举地保持两者的一致性。

### 注释的插入

javadoc实用程序从下面几个特性中抽取信息

- 包
- 公有类与接口
- 公有的和受保护的构造器及方法
- 公有的和受保护的域

应该为上面几部分编写注释，注释应该放置在所描述特性的前面。注释以`/**`开始，并以`*/`结束，
标记由@开始。注释的第一句应该是一个概要性的句子，javadoc实用程序自动地将这些句子抽取出来
形成概要页。

类注释必须放在import语句之后，类定义之前。

每一个方法注释必须放在所描述的方法之前，除了通用标记之外，还可以实用下面的标记

- @param 变量描述
这个标记将对当前方法的"param"（参数）部分添加一个条目。这个描述可以占据多行，并可以使用
HTML标记，一个方法的所有@param标记必须放在一起。

- @return 描述
这个标记对当前方法添加"return"（返回）部分。这个描述可以跨越多行，并可以使用HTML标记。

```
/**
 * Raises the salary of an employee
 * @param byPercent the percentage by which to raise the salary(e.g. 10means 10%)
 * @return the amount of the raise
 */
 public double raiseSalary(double byPercent)
 {
    double raise = salary * byPercent / 100;
    salary += raise;
    return raise;
 }
```

#### 域注释
只需要对公有域（通常指的是静态常量）建立文档
```
/**
 * The "Hearts" cart suit
 */
public static final int HEARTS = 1;
```

### 通用注释

- @author 姓名
这个标记将产生一个author（作者）条目。可以使用多个@author标记，每个@author标记对应一个作者

- @version 文本
这个标记将产生一个version（版本）条目。这里的文本可以是对当前版本的任何描述

- @since 文本
这个标记将产生一个since（始于）条目。这里的文本可以是对引入特性的版本描述。例如 @since version 1.7.1

- @deprecated 文本
这标记将对类、方法或变量添加一个不再使用的注释。文本中给出取代的建议。例如：
@deprecated xxx <code>setVisible(true)</code> instead




























