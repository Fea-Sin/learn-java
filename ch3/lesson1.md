

## Java基本程序设计结构二


### 码点与代码单元

Java字符串由char值序列组成，char类型是一个采用UTF-16编码表示Unicode码点的代码单元，大多数的常用
Unicode字符使用一个代码单元表示，有些需要一对代码单元表示。

length方法将返回采用UTF-16编码表示的给定字符串所需要的代码单元数量。

一个字符串的码点数不一定等于代码单元数，因为有的字符包含两个代码单元（UTF-16编码），要想
得到实际的码点长度，可以调用
```
String greeting = "Hello";
int cpCount = greeting.codePointCount(0, greeting.length());
// 此时 cpCount = greeting.length() is 5
```
调用s.charAt(n)将返回位置n的代码单元，n介于0 ~ s.length()-1之间，如果字符串中有的字符包含两个代码单元，如果
此时使用chart返回字符串中的字符，可能会出现问题。

如果想要遍历一个字符串，并且一次查看每一个码点（相应字符Unicode十进制值），可以使用以下语句
```
int cp = sentence.codePointAt(i);
if (Character.isSupplementaryCodePoint(cp)) i += 2;
else i++;
```
显然这很麻烦，使用codePoints方法，它会生成一个int值的流，每个int值对应一个码点，可以将它转换成
为一个数组
```
int[] codePoints = str.codePoints().toArray();

// 此时str字符串真实字符个数为 codePoints.length
// str.length() 是代码单元个数
```
把一个码点数组转换为一个字符串，可以使用构造函数
```
String str = new String(codePoints, 0, codePoints.length)
```


### String API

- int indexOf(String str)
- int indexOf(String str, int fromIndex)
- int indexOf(int cp)
- int indexOf(int cp, int fromIndex)
返回与字符串str或代码点cp匹配的第一个子串的开始位置，这个位置从索引0或fromIndex开始计算，如果在原始
串中不存在str返回-1

- int lastIndexOf(String str)
- int lastIndexOf(String str, int fromIndex)
- int lastIndexOf(int cp)
- int lastIndexOf(int cp)
- int lastIndexOf(int cp, int fromIndex)

Returns the index within this string of the last occurrence of the specified substring, 
searching backward starting at the specified index.
返回与字符串str或代码点cp匹配的最后一个子串的开始位置，从fromIndex位置反向搜索，也就是从fromeIndex
反向搜索到的第一个子串匹配的位置。


[阅读联机API](https://docs.oracle.com/javase/8/docs/api/)


## 输入输出

打印输出到"标准输出流"（即控制台窗口）是一件非常容易的事情，只要调用System.out.println即可。然而，
读取"标准输入流"System.in就没那么简单了。要想通过控制台进行输入，首先需要构建一个Scanner对象，并
与"标准输入流"System.in关联。
```
Scanner in = new Scanner(System.in);
```
现在可以使用Scanner类的各种方法实现输入操作了。
[实例](InputTest/InputTest.java)

### 格式化输出

可以使用System.out.print(x)将数值输出到控制台上，这条命令将以x对应的数据类型所允许的最大非0数字
数字位数打印输出x
```
double x = 10000.0 / 3.0;
System.out.print(x);  // 3333.333333333335
```

在早期的Java版本中，格式化数值曾引起过一些争议，庆幸的是，Java SE 5.0沿用了C语言库函数的printf
方法。例如`System.out.printf("%8.2f", x)`
可以用8个字符的宽度和小数点后两个字符的精度打印x。

每一个以%字符开始的格式说明符都用相应的参数替换，格式转换符将指示被格式化的数值类型

| 转换符 | 类型 | 举例 |
|----|----|----|
| d | 十进制整数 | 159 |
| x | 十六进制整数 | 9f |
| o | 八进制整数 | 237 |
| f | 定点浮点数 | 15.9 |
| e | 指数浮点数 | 1.59e+1 |
| g | 通用浮点数 | - |
| a | 十六进制浮点数 | ox1.f3 |
| s | 字符串 | Hello |
| c | 字符 | H |
| b | 布尔 | True |

另外，还可以给出控制格式化输出的各种标志，例如逗号标志增加了分组的分隔符
```
System.out.printf("%,.2f", 10000.0 / 3.0)  // 3,333.33
```

| 标志 | 目的 | 举例 |
|----|----|----|
| + | 打印正数和负数的符号 | +3333.33 |
| 空格 | 在正数之前添加空格 | - |
| 0 | 数字前面补0 | 003333.33 |
| - | 左对齐|  |
| , | 添加分组分隔符 | 3,333.33 |
| $ | 给定被格式化的参数索引，例如%1$d, %1$x将以十进制和十六进制格式打印第一个参数 | 159 9F |

> 说明，%1$ 对第一个参数格式化，参数索引值从1开始，而不是从0开始。

可以使用s转换格式符格式化任意的对象，对于任意实现了Formattable接口的对象都将调用formatTo方法，否则
则将调用toString方法，它可以将对象转换为字符串。

可以使用静态的 String.format 方法创建一个格式化的字符串，而不是打印输出
```
String message = String.format("Hello, %s. Next year, you'll be %d", name, age);
```

### 文件输入于输出

想要对文件进行读取，就需要一个用File对象构造一个Scanner对象
```
Scanner in = new Scanner(Paths.get("myfile.txt"), "UTF-8")
```
在这里指定了UTF-8字符编码，这对于互联网上的文件很常见，读取一个文本文件时，要知道它的字符编码，如果
省略字符编码，则会使用运行这个Java程序的机器的"默认编码"，这不是一个好主意，如果在不同的机器上运行
这个程序，可能会有不同的表现。

要想写入文件，就需要构造一个PrintWriter对象，在构造器中，只需要提供文件名
```
PrintWriter out = new PrintWriter("myfile.txt", "UTF-8")
```
如果文件不存在，创建该文件，可以像输出到System.out一样使用print、println、printf命令。

当指定一个相对文件名时，例如"myfile.txt"，"mydirectory/myfile.txt"或"../myfile.txt",
文件位于Java虚拟机启动路径的相对位置，如果在命令行方式下用下列命令启动程序`java MyProg`启动
路径就是命令解释器的当前路径。如果觉得定位文件比价烦恼，则可以使用绝对路径`/home/me/mydirectory/myfile.txt`。

正如读者所看到的，访问文件于使用System.in和System.out一样容易。要记住一点，如果用一个不存在的文件
构造一个Scanner，或者用一个不能被创建的文件名构造一个PrintWriter，那么就会发生异常。Java编译器
认为这些异常比"被除零"异常更严重。




















