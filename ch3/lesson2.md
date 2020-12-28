
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










