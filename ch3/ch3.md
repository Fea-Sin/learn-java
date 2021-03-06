

## 第三章 Java 的基本程序设计结构

- 注释
- 数据类型
- 变量
- 运算符
- 字符串
- 输入输出
- 控制流
- 大数值
- 数组

本章主要介绍程序设计的基本概念（如数据类型、分支以及循环）在Java中的实现方式。

非常遗憾，使用Java编写GUI应用程序并不是一件很容易的事情，编程者需要掌握很多相关的知识才能
够创建窗口、添加文本框以及能响应的按钮等。本章给出的所有示例都是为了说明一些相关的概念而
设计的"玩具式"程序，它们仅仅使用终端窗口提供输入输出。

一个简单的Java应用程序[示例1](FirstSample.java)

Java区分大小写，如果出现了大小写拼写错误（例如，将main拼写成Main）程序
将无法运行。
关键字public称为访问修饰符（access modifier），这些修饰符用于控制程序的其他部分对这段代码
的访问级别。关键字class表明Java程序中的全部内容都包含在类中，这里只需要将类作为一个加载程序逻辑
的容器，程序逻辑定义了应用程序的行为。类是构建所有Java应用程序的构建块，Java应用程序中的全部
内容都必须放置在类中。

关键字class后面紧跟类名，Java中定义类名的规则很宽松，名字必须以字母开头，后面可以跟字母和
数字的任意组合，长度没有限制，但是不能使用Java保留字。
标准的命名规范为，类名是以大写字母开头的名次，如果名字由多个单词组成，每个单词的第一个字母都
应该大些（这种在一个单词中间使用大写字母的方式称为驼峰命名法，例如CamelCase）。
源代码的文件名必须与公共类的名字相同，并用.java作为扩展名。

```
java FirstSample
```
运行已编译的程序时，Java虚拟机将从指定类中的main方法开始执行，因此为了代码能够执行，在类的
源文件中必须包含一个main方法，当然也可以将用户自定义的方法添加到类中，并且在main方法中调用
它们。
根据Java语言规范，main方法必须声明为public，需要注意源代码中的括号{}，在Java中，像在
C/C++中一样，用大括号划分程序的各个部分（通常称为块），Java中任何方法的代码都用"{"开始
用"}"结束。每个Java应用程序都必须有一个main方法。

Java中的所有函数都属于某个类的方法，因此Java中的main方法必须有一个外壳类。
在Java中，每个句子必须用分号结束，特别需要说明，回车不是语句的结束标志，如果需要可以将
一条语句写在多行上。

### 注释
与大多数程序设计语言一样，Java中的注释也不会出现在可执行程序中，因此可以在源程序中根据
需要添加任意多的注释，而不必担心可执行代码会膨胀。

Java中有3种标记注释的方式

- 1 使用//，其注释内容从//开头到本行结束
- 2 当需要长篇注释时，可以使用/* 和 */将一段比较长的注释扩起来
- 3 这种以/**开始，以*/结束的注释，可以自动地生成文档

### 数据类型

Java是一种强类型语言，这就意味着必须为每一个变量声明一种类型。在Java种一共有8种基本类型
（primitive type），其中有4种整型，2种浮点型，1种用于表示Unicode编码的字符单元的字符类型
char和1种用于表示真值的boolean类型。

Java有一个能够表示任意精度的算术包，通常称为大数值（big number），虽然称为大数值，但它
并不是一种新的Java类型，而是一个Java对象。


### 整型

整型用于表示没有小数部分的数值，它允许是负数

| 类型 | 存储需求 | 取值范围 |
|----|----|----|
| int | 4字节 | -2147483648 ～ 2147483647 |
| short | 2字节 | -32768 ~ 32767 |
| long | 8字节 | 很大 |
| byte | 1字节 | -128 ~ 127 |

在Java中，整型的范围与运行Java代码的机器无关，这就解决了软件从一个平台移植到另一个平台，
或者在同一个平台中的不同操作系统之间进行移植带来的诸多问题。于此相反，C和C++程序需要针对
不同的处理器选择最为高效的整型，这样就可能造成一个在32位处理器上运行很好的C程序在16位
系统上运行却发生整数溢出。Java没有任何无符号（unsigned）形式的int、long、short或
byte类型。

### 浮点类型

浮点类型用于表示有小数部分的数值，在Java中有两种浮点类型，float类型4个字节，double
类型8个字节。

double表示这种数据类型的数值精度是float类型的两倍，绝大部分应用都采用double类型。
float类型的数值有一个后缀F或f，没有后缀F的浮点数值如3.14默认为double类型。

所有的浮点数值计算都遵循IEEE 754规范，具体来说，下面是用于表示溢出和出错情况的三个
特殊的浮点数值

- 正无穷大
- 负无穷大
- NaN（不是一个数字）

一个正数除以0的结果为正无穷大，计算0/0或者负数的平方根结果为NaN

### char类型

char类型的字面量值要用单引号括起来，例如`'A'`是编码值为65所对应的字符常量，它与`"A"`不同，
`"A"`是包含一个字符A的字符串。

在Java中，char类型描述了UTF-16编码中的一个代码单元，我们强烈建议不要在程序中使用char类型，
除非确实需要处理UTF-16代码单元，最好将字符串作为抽象数据类型处理。

### boolean 类型

boolean类型有两个值，false和true 用来判定逻辑条件，整型值和布尔值之间不能进行相互转换。

在C++中，树值甚至指针可以代替boolean值，值0相当于布尔值false，非0值相当对于true。

### 变量

在Java中，每个变量都有一个类型（type），在声明变量时，变量的类型位于变量名之前
```
double salary;
int vacationDays;
long earthPopulation;
boolean done;
```
可以看到，每个声明以分号结束，由于声明是一条完整的Java语句，所以必须以分号结束。
变量名必须是一个字母开头并由字母或数字构成的序列，需要注意的是与大多数程序设计语言相比，
Java中"字母"和"数字"的范围更大，字母包括'A' ～ 'Z'、'a' ~ 'z'、'_'、'$'或在某种
语言中表示字母的任何Unicode字符，同样，数字包括'0' ~ '9'和在某种语言中表示数字的任何
Unicode字符。变量名中所有的字符都是有意义的，且大小写敏感，变量名的长度基本上没有限制。

尽管$是一个合法的Java字符，但不要在你自己的代码中使用这个字符，它只用在Java编译器或
其他工具生成的名字中。另外，不能使用Java保留子作为变量名。
可以在一行中声明多个变量
```
int i, j;
```
不过，不提倡使用这种风格，逐一声明一个变量可以提高程序的可读性。

### 变量初始化

声明一个变量之后，必须用赋值语句对变量进行显示初始化，千万不要使用未初始化的变量，也可以
将变量的声明和初始化放在同一行中
```
int vacationDays = 12
```
在Java中可以将变量声明放在代码中的任何地方，变量的声明尽可能的靠近第一次使用的地方，这是一种
良好的程序编写风格。
```
double salary = 65000.0
System.out.println(salary)
int vacationDays = 12
```

### 常量

在Java中，利用关键字final指示常量，关键字final表示这个变量只能赋值一次，一旦被赋值之后，就不能再
改了。习惯上常量使用大写
```
final double CM_PER_INCH = 2.54
```
在Java中，经常希望某个常量可以在一个类中的多个方法中使用，通常将这些常量称为类常量，可以使用关键字
static final设置一个类常量。
```
public class Constants
{
    public static final double CM_PER_INCH = 2.54;
    
    public static void main(String[] args)
    {
        double pageWidth = 8.5;
        double pageHeight = 11;
        System.out.println("Paper size in centimeters:
            + pageWidth * CM_PER_INCH + "by " + pageHeight * CM_PER_INCH");
    }
}
```
需要注意，类常量的定义位于main方法的外部，在同一个类的其他方法中也可以使用这个常量，而且，
如果一个常量被声明为public，那么其他类的方法也可以使用这个常量，Constants.CM_PER_INCH就是
这样一个常量。

> const 是Java保留的关键字，但目前并没有使用，在Java中，必须使用final定义常量

### 运算符

在Java中，使用算术运算符`+、-、*、/`表示加、减、乘、除运算。当参与 / 运算的两个操作数都是
整数时，表示整数除法，否则表示浮点除法，整数的求余操作（有时称为取模）用 % 表示
```
15 / 2 // 7
15 % 2 // 1
15.0 / 2 // 7.5
```
需要注意，整数被0除将会产生一个异常，而浮点数被0除将会得到无穷大或NaN结果。

### 数学函数与常量

在Math类中，包含了各种各样的数学函数，想要计算一个数值的平方根，使用sqrt方法。在Java中，
没有幂运算，因此需要借助Math类的pow方法，pow方法有两个double类型的参数。
```
double x = 4;
double y = Math.sqrt(x); // 2.0

double z = Math.pow(2, 10)

Math.sin
Math.cos
Math.exp
Math.log
Math.log10

// Java提供了用于表示排和e的近似值
Math.PI
Math.E
```

### 数值类型之间的转换

经常需要将一种数值类型转换称另一种数值类型，有时无信息丢失的转换，有时可能会有精度损失。
```
int n = 123456789;
float f = n;

n + f
```
当使用上面两个数值进行二元操作时，先要将两个操作数转换为同一种类型，然后再进行计算。

- 如果两个操作数中有一个是double类型，另一个操作数就会转换为double类型
- 否则，如果其中一个操作数是float类型，另一个操作数将会转换为float类型
- 否则，如果其中一个操作数是long类型，另一个操作数将会转换为long类型
- 否则，两个操作数都将被转换为int类型

### 强制类型转换

在必要的时候，int类型的值将会自动地转换为double类型，另一方面，有时也需要将double转换称int，在Java
中，允许进行这种数值之间的类型转换。当然，有可能会丢失一些信息。这种情况下，需要通过强制类型转换cast实现
这个操作。
```
double x = 9.997;
int nx = (int) x; // nx 值为 9

int nx = (int) Math.round(x) // nx 值为 10
```
强制类型转换通过截断小数部分将浮点数值转换为整数，如果想对浮点数进行舍入运算，以便得到最接近的整数，
需要使用Matn.round方法，round方法返回的结果为long类型，所以还需要强制类型转换才能转换为int类型。

如果试图将一个数值从一种类型强制转换为另一种类型，而又超出了目标类型的表示范围，结果就会截断称为一个
完全不同的值，例如(byte)300的实际值是44。

运算结合赋值时会发生强制类型转换
```
int x = 1;
x += 3.5 // x 值为（int）(x + 3.5)
```
如果运算符得到一个值，其类型与左侧操作数的类型不同，就会发生强制类型转化。

### 位运算符

处理整型类型时，可以直接对组成整型数值的各个位完成操作，这意味着可以使用掩码技术得到整数中的各个位，
位运算包括：&（and）、|（or）、^（xor）、～（not）

应用在布尔值上时，&和|运算符也会得到一个布尔值，这些运算符与&&和||运算符很类似，不过&和｜运算不采用
短路方式来求值，也就是说，得到计算结果之前两个操作数都需要计算。

还有>>和<<运算符，右移和左移运算。移位运算符的右操作数要完成模32的运算（除非左操作数是long类型，
在这种情况下需要对右操作数模64），例如 1 << 35 等同于 1 << 3 值为 8。

### 括号于运算符级别

表达式中，如果不使用圆括号，就按照给出的运算符优先级次序进行计算，同一个级别的运算符按照从左到右
的次序进行运算（除了右结合运算符）。
&& 的优先级比 ||的优先级高，所以表达式 a && b || c 等价于 （a && b）|| c

因为`+=`是右结合运算符，所以表达式 a += b += c 等价于 a += (b += c)

#### 右结合运算符
?:、=、+=、-=、*=、/=、%=、&=、|=、^=、<<=、>>=、>>>=

### 枚举类型

有时候，变量的取值只在一个有限的集合内。例如：销售的服装或比萨饼只有小、中、大、超大这四种尺寸，
当然，可以将这些尺寸分别编码为1、2、3、4或S、M、L、X但这样存在着一定的隐患，在变量中很可能保存的
是一个错误的值（如0或m）。
针对这种情况，可以自定义枚举类型，枚举类型包括有限个命名的值。例如

```
enum Size { SMALL, MEDIUM, LARGE, EXTRA_LARGE }
```

现在可以声明这种类型的变量`Size s = Size.MEDIUM`，Size类型的变量只能存储这个类型声明中给定的
某个枚举值，或者`null`值，`null`值表示这个变量没有设置任何值。

## 字符串

从概念上讲，Java字符就是Unicode字符序列，Java没有内置的字符串类型，而是在标准Java类库中提供了
一个预定义类（String），每个用双引号括起来的字符串都是String类的一个实例。
```
String e = "";
String greeting = "Hello";
```

String类的sunstring方法可以从一个较大的字符串提取出一个子串。
```
String greeting = "Hello";
String s = greeting.substring(0, 3); // s值为 Hel，其长度为3-0=3
```
substring方法参数是左闭右开获取子串，substring(a, b)的长度为b-a

与绝大多数的程序设计语言一样，Java语言允许使用 + 号连接两个字符串。当将一个字符串与一个非字符串的值
进行拼接时，后者被转换成为字符串，任何一个Java对象都可以转换成字符串。

这种特性通常用在输出语句中
```
System.out.println("The answer is " + answer)
```

#### 不可变字符串

String类没有提供用于修改字符串的方法，如果希望将greeting的内容修改为"Help!"，不能直接地将greeting
的最后两个位置的字符修改为"p!"，这对于C程序员来说，将会感到无从下手。在Java中实现这项操作非常容易，
首先提取需要的字符，再拼接上替换的字符串
```
greeting = greeting.substring(0, 3) + "p!"
```

由于不能修改Java字符串中的字符，所以Java中String类对象称为不可变字符串，字符串"Hello"永远包含字符
H、e、l、l和o的代码单元序列，而不能修改其中的任何一个字符。当然，可以修改字符串变量greeting，让它
引用另外一个字符串。这样做是否会降低运行效率呢？不可变字符串的优点是：编译器可以让字符串共享。

Java的设计者认为共享带来的高效率远远胜于提取、拼接字符串所带来的低效率。

在C程序员第一次接触Java字符串的时候，常常会感到迷惑，因为它们总是将字符串认为是字符型数组
`char greeting[] = "Hello"`

在Java中将greeting赋予另外一个值，会不会产生内存遗漏呢，毕竟，原始字符串放置在堆中，十分幸运，Java
将自动地进行垃圾回收，如果一块内存不再使用了，系统最终会将其收回。

C++字符串是可以修改的，也就是说，可以修改字符串中的单个字符。

#### 检测字符串是否相等

可以使用equals方法检测两个字符串是否相等`s.equals(t)`，字符串s与t相等返回true，否则返回false，
s和t可以是字符串变量，也可以是字符串字面量。
```
"Hello".equals(greeting) // true
```

不要使用==运算符检测两个字符串是否相等，这个运算只能够确定两个字符串是否放置在同一个位置上。

#### 空串与Null串

空串""是长度为0的字符串，可以调用以下代码检查一个字符串是否为空
```
if (str.length() == 0)
或
if (str.equals(""))
```
空串是一个Java对象，有自己的长度0和内容空，不过，String变量还可以存放一个特殊的值，名为null，这表示
目前没有任何对象与该变量关联。要检查一个字符串是否为null，要使用以下条件
```
if (str == null)
```
有时要检查一个字符串既不是null也不为空串，使用以下代码
```
if (str != null && str.length() != 0)
```






















