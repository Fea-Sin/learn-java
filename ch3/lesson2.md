
## 控制流程

与任何程序设计语言一样，Java使用条件和循环结构确定控制流程，当需要对某个表达式的多个值进行检测时，
可以使用switch语句。

Java的控制流程结构与C和C++的控制流程结构一样，只有很少的例外情况，没有goto语句，但break语句可以
带标签，可以利用它实现从内层循环跳出的目的。

### 块作用域

在深入学习控制结构之前，需要了解块（block）的概念。

块是指由一对大括号括起来若干条简单的Java语句。块确定了变量的作用域，一个块可以嵌套在另一个块中。
不能在嵌套的两个块中声明同名的变量，下面的代码就有错误，无法通过编译
```
public static void main(String[] args)
{
    int n;
    ...
    {
        int k;
        int n; // redefine n in inner block
    }
}
```

> 在C++中，可以在嵌套的块中重定义一个变量，在内层定义的变量会覆盖在外层定义的变量。
> 这样有可能会导致程序设计错误，因此在Java中不允许这样做

### 条件语句

在Java中，条件语句的格式为
```
if (condition) statement
```
这里的条件必须用括号括起来。在某个条件为真时执行多条语句，这种情况下，应该使用块语句
```
if (yourSales >= target)
{
    performance = "Satisfactory";
    ...
}
```
在Java中，更一般的条件语句格式如下所示
```
if (condition) statement1 else statement2
```
其中else部分是可选的，else子句与最邻近的if构成一组。
重复地交替出现if...else if...是一种很常见的情况
```
if (yourSales >= 2*target)
{
    performance = "Excellent";
    ...
}
else if (yourSales >= 1.5*target)
{
    performance = "Fine"
    ...
}
else if (yourSales >= target)
{
    performance = "Satisfactory"
    ...
}
else
{
    System.out.println("You're fired")
}
```

### 循环

当条件为true时，while循环执行一条语句（也可以是一个语句块），一般格式为
```
while (condition) statement

// or 

while (balance < goal)
{
    balance += payment;
    double interest = balance * interestRate / 100;
    balance += interest;
    years++;
}
```
如果开始循环条件的值为false，则while循环体一次也不执行。如果希望循环体至少执行一次，
则应该将检测条件放在最后，使用do/while循环语句可以实现这种操作方式。
```
do statement while (condition);

// or

do
{
    balance += payment;
    double interest = balance * interestRate / 100;
    balance += interest;
    year++;
    ...
}
while (input.equals("N"));
```

### 确定循环

for循环语句是支持迭代的一种通用结构，利用每次迭代之后更新的计数器或类似的变量来控制迭代次数。
当在for语句的第1部分中声明了一个变量之后，这个变量的作用域就为for循环的整个循环体，如果在for
语句内部定义一个变量，这个变量就不能在循环体之外使用。
```
for(int i = 1; i <= 10; i++)
{
    ...
}
// i no longer defined here

for (int i = 11; i <= 20; i++) // OK to defined another variable named i
{
    ...
}
```

### 多重选择：switch 语句

在处理多个选项时，使用if/else结构显得有点笨拙，Java有一个与C/C++完全一样的switch语句。
switch语句是"直通式"行为，有可能触发多个case分支，因此我们在每个分支添加break语句。如果没有
相匹配的case标签，而有default子句，就执行这个子句。
```
Scanner in = new Scanner(System.in)
int choice = in.nextInt();

switch (choice)
{
    case 1:
        ...
        break;
    case 2:
        ...
        break;
    case 3:
        ...
        break;
    case 4:
        break;
    default:
        ...
        break;
}
```

### 中断控制流程语句

尽管Java的设计者将goto作为保留字，但实际上并没有打算在语言中使用它，通常使用goto语句被认为是一种
拙劣的程序设计风格。无限制地使用goto语句确实是导致错误的根源，但在有些情况下，偶尔使用goto跳出循环
还是有益处的，Java设计者同意这种看法，在Java语言中增加了一条带标签的break，以此来支持这种程序
设计风格。

下面看一下不带标签的break语句
```
while (years <= 100)
{
    balance += payment;
    double interest = balance * interestRate / 100;
    balance += interest;
    if (balance >= goal) break;
    years++
}
```
Java还提供了一种带标签的break语句，用于跳出多重嵌套的循环语句，请注意，标签必须放在希望跳出的最外层
循环之前，且必须紧跟一个冒号。
```
Scanner in = new Scanner(System.in);
int n;
read_data:
while (...)
{
    for (...)
    {
        System.out.print("Enter a number >= 0;");
        n = in.nextInt();
        if (n < 0)
        {
            ...
            break read_data;
        }
    }
    
}
if (n < 0)
{
    // deal with bad sitution
}
else
{
    // carry out normal processing
}
```
如果输入有误，通过执行带标签的break跳转到带标签的语句块末尾。对于任何使用break语句的代码都
需要检测循环是正常结束的，还是由break跳出。

还有一个continue语句，与break语句一样，它将中断正常的控制流程，continue语句将控制转移到
最内层循环的首部。
```
Scanner in = new Scanner(System.in)
while (sum < goal)
{
    System.out.print("Enter a number");
    n = in.nextInt();
    if (n < 0) continue;
    sum += n; // not executed if n < 0
}
```
如果n<0，则continue语句越过了当前循环体的剩余部分，立即跳到循环首部。

如果将continue语句用于for循环中，就可以跳到for循环的"更新"部分。
```
for (count = 1; count <= 100; count++)
{
    System.out.print("Enter a number")
    n = in.nextInt();
    if (n < 0) continue;
    sum += n;
}
```
如果n<0，则continue语句跳到count++语句。

### 大数值

如果基本的整数和浮点数精度不能够满足需求，那么可以使用java.math包中的两个很有用的类BigInterge和
BigDecimal。这两个类可以处理包含任意长度数字序列的数值，BigInteger类实现了任意精度的整数运算，
BigDecimal实现了任意精度的浮点数运算。
使用静态的valueOf方法可以将普通的数值转换为大数值
```
BigInteger a = BigInteger.valueOf(100);
```
遗憾的是不能使用人们熟悉的算术运算符（如+和*等）处理大数值，而需要使用大数值中的add和multiply方法。
```
BigInteger c = a.add(b); // c = a + b
BigInteger d = c.multiply(b.add(BigInteger.valueOf(2))); // d = c * (b + 2)
```
与C++不同，Java没有提供运算符重载功能，程序员无法重定义+和*运算符，使其应用于BigInteger类的add和
multiply运算。Java语言的设计者确实为字符串的连接重载了+运算符，但没有重载其它的运算符，也没给Java
程序员在自己的类中重载运算符的机会。

### 数组

数组是一种数据结构，用来存储同一类型值的集合，通过一个整型下标可以访问数组中的每一个值。

在声明数组变量时，需要指出数组类型和数组变量的名字，下面声明了整型数组a
```
int[] a;
```
不过，这条语句只声明了变量a，并没有将a初始化为一个真正的数组，应该使用new运算符创建数组
```
int[] a = new int[100]
```
创建一个数组时，所有元素都初始化为0，boolean数组的元素会初始为false。对象数组的元素则初始化为一个
特殊值null，这表示这些元素还未存放任何对象。
```
String[] names = new String[10]
```
会创建一个包含10个字符串的数组，所有字符串都为null，如果希望这个数组包含空串，可以为元素指定空串
```
for (int i = 0; i < 10; i++) names[i] = "";
```

如果创建了一个100个元素的数组，并且试图访问元素`a[100]`，程序就会引发"array index out of bounds"
异常而终止执行。

一旦创建了数组，就不能再改变它的大小（尽管可以改变每一个数组元素），如果经常需要在运行过程中扩展数组的
大小，就应该使用另一种数据结构--数组列表（array list）。

#### for each 循环

Java有一种功能很强的循环结构，可以用来以次处理数组中的每个元素，而不必为指定下标而分心。
```
for (variable: collection) statement
```
定义一个变量用于暂存集合中的每一个元素，并执行相应的语句（当然，也可以是语句块），collection这一
集合表达式必须是一个数组或者是一个实现了Iterable接口的类对象。
```
for (int element: a)
{
    System.out.println(element);
}
```
Java语言的设计者认为应该使用诸如foreach、in这样的关键字，但这种循环语句并不是最初就包含在Java语言中，
而是后来添加进入的。

有个更加简单的方式打印数组中的所有值，即利用Arrays类的toString方法，调用Arrays.toString(a),
返回一个包含数组元素的字符串，这些元素被放置在括号内，并用逗号分隔，例如`"[2, 3, 5, 7, 11]"`

#### 数组初始化以及匿名数组

在Java中，提供了一种创建数组对象并同时赋予初始值的简化书写形式
```
int[] smallPrimes = {2, 3, 5, 7, 11, 13};
```
请注意，在使用这种语句时，不需要调用new。甚至可以初始化一个匿名的数组
```
new int[] {17, 19, 23, 25, 27, 29}
```
这种表示法将创建一个新数组并利用括号中提供的值进行初始化，数组的大小就是初始值的个数。使用这种形式语法
可以在不创建变量的情况下重新初始化一个数组
```
smallPrimes = new int[] {17, 19, 23, 25, 27, 29}
```

#### 数组拷贝

Java中，允许将一个数组变量拷贝给另一个数组变量，这时两个变量将引用同一个数组
```
int[] luckyNumbers = samllPrimes;
luckNumbers[5] = 12; now smallPrimes[5] is also 12
```
如果希望将一个数组的所有值拷贝到一个新的数组中去，就要使用Arrays类的copyOf方法
```
int[] copiedLuckyNumbers = Arrays.copyOf(luckyNumbes, luckyNumbers.length)
```
第2个参数是新数组的长度，这个方法通常用用来增加数组的大小。如果数组元素是数值型，
那么多余的元素将被赋值为0；如果数组是布尔型，则将赋值为false，相反，如果长度小于原始数组的长度，
则只拷贝前面的数据元素。


























