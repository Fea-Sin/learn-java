

## 用户自定义类

在第3章中，已经开始编写了一些简单的类。但是，那些类都只包含一个简单的main方法。现在开始学习如何
设计复杂应用所需要的各种主力类（workhorse class）。通常，这些类没有main方法，却有自己的的实例域
和实例方法。要想创建一个完整的程序，应该将若干类组合在一起，其中只有一个类有main方法。

### Employee类

在Java中，最常见的类定义形式：
```
class ClassName
{
    field1
    field2
    ...
    constructor1
    constructor2
    ...
    method1
    method2
    ...
}
```
下面看一个非常简单的Employee类
```
class Employee
{
    // instance fields
    private String name;
    private double salary;
    private LocalDate hireDay
    
    // constructor
    public Employee(String n, double s, int year, int month, int day)
    {
        name = n;
        salary = s;
        hireDay = LocalDate.of(year, month, day);
    }
    // a method
    public String getName()
    {
        return name;
    }
    public double getSalary()
    {
        return salary;
    }
    public LocalDate getHireDay()
    {
        return hireDay;
    }
    public void raiseSalary(double byPercent)
    {
        double raise = salary * byPercent / 100;
        salary += raise;
    }
}
```
```
public class EmployeeTest
{
    public static void main(String[] args)
    {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
    }
}
```
示例程序中包含两个类，Employee类和带有public访问修饰符的EmployeeTest类，EmployeeTest
类包含了main方法。
源文件名必须与public类的名字相匹配，在一个源文件中，只能有一个共有类，但可以有任意数量的非公有类。

### 多个源文件的使用

上述示例一个源文件包含了两个类。许多程序员习惯于将每一个类存在一个单独的源文件中，例如，将Employee类
存放在文件Employee.java中，将EmployeeTest类存放在文件EmployeeTest.java中。
如果这样组织文件，将可以有两种编译源程序的方法。一种是使用通配符调用Java编译器
```
javac Employee*.java
```
所有于通配符匹配的源文件都将被编译成类文件。或者使用如下命令
```
javac EmployeeTest.java
```
当Java编译器发现EmployeeTest.java使用了Employee类时会查找名为Employee.class文件，如果没有
找到这个文件，就会自动地搜索Employee.java，然后对它进行编译。更重要的是，如果Employee.java版本
较已有的Employee.class文件版本新，Java编译器就会自动地重新编译这个文件。Java编译器内置了"make"功能。

Employee类的所有方法都被标记为public，关键字public意味着任何类的任何方法都可以调用这些方法。
需要注意在Employee类的实例中有三个实例域用来存放将要操作的数据
```
private String name;
private double salary;
private LocalDate hireDay;
```
关键字private确保只有Employee类自身的方法能够访问这些实例域，而其他类的方法不能够读写这些域。

> 可以用public标记实例域，但这是一种极不提倡的做法。public数据域允许程序中的任何方法
> 对其进行读取和修改，这就完全破坏了封装。

请注意，有两个实例域本身就是对象，name域是String类对象，hireDay域是LocalDate类对象，
这种情形十分常见。

### 从构造器开始

可以看到，构造器与类同名。在构造Employee类的对象时，构造器会运行，以便将实例域初始化为所希望
的状态。

构造器与其他方法有一个重要的不同，构造器总是伴随着new操作符的执行被调用，而不能对一个已经存在的
对象调用构造器来达到重新设置实例域的目的。

- 构造器与类同名
- 每个类可以有一个以上的构造器
- 构造器可以有0个、1个或多个参数
- 构造器没有返回值
- 构造器总是伴随着new操作符一起调用

> 要记住所有的Java对象都是在堆中构造的，构造器总是伴随着new操作符一起使用

请注意，不要在构造器中定义与实例域重名的局部变量
```
public Employee(String n, double s)
{
    String name = n;   // Error
    double salary = s; // Error
}
```
这个构造器声明了局部变量name和salary，这些变量只能在构造器内部访问，这些变量
屏蔽了同名的实例域。

### 隐式参数与显式参数

方法用于操作对象以及存取它们的实例域
```
public void raiseSalary(double byPercent)
{
    double raise = salary * byPercent / 100;
    salary += raise;
}
```
```
number007.raiseSalary(5);
```
将调用这个方法的对象的salary实例域设置为新值，它的结果将number007.salary的值增加5%。
具体的说，这个调用将执行下列命令
```
double raise = number007.salary * 5 / 100;
number007.salary += raise;
```
raiseSalary方法有两个参数，第一个参数称为隐式（implicit）参数，是出现在方法名前的Employee类对象。
第二个参数位于方法名后面括号中的数值，这是一个显式（explicit）参数。（有人把隐式参数称为方法调用
的目标或者接收者）

可以看到，显式参数是明显地列在方法声明中的（double byPercent），隐式参数没有出现在方法声明中。
关键字this表示隐式参数，如果需要，可以用下列方式编写raiseSalary方法。
```
public void raiseSalary(double byPercent)
{
    double raise = this.salary * byPercent / 100;
    this.salary += raise;
}
```
有些程序员更偏爱这样的风格，因为这样可以将实例域与局部变量明显地区分开来。

> 在C++中，通常在类的外面定义方法。在Java中，所有的方法都必须在类的内部定。

### 封装的优点

在Employee类中的getName方法、getSalary方法和getHireDay方法，这些都是典型的访问器方法。
由于它们只返回实例域的值，因此又称为域访问器。

name是一个只读域，一旦在构造器中设置完毕，就没有任何一个办法对它进行修改，这样来确保name域
不会受到外界的破坏。
虽然salary不是只读域，但是它只能用raiseSalary方法修改，特别是一旦这个域值出现了错误，
只要调试这个方法就可以了。如果salary域是public的，破坏这个值域的捣乱者可能出没在任何地方。

在有些时候，需要获得或设置实例域的值，应该提供下面三项内容

- 一个私有的数据域
- 一个公有的域访问器方法
- 一个公有的域更改器方法

这样做要比提供一个简单的公有数据域复杂一些，但有着明显好处。
好处一，可以改变数据域的内部实现，除了该类的方法之外，不会影响其他代码。

第二点好处，更改器方法可以执行错误检查，然而直接对数据域进行赋值将很难进行处理。

> 注意不要编写返回引用可变对象的访问器方法，这会破坏封装性！

如果需要返回一个可变对象的引用，应该首先对它进行克隆（clone）

### 基于类的访问权限

从前面已经知道，方法可以访问所调用对象的私有数据。一个方法可以访问所属类的所有对象的私有数据。

例如，比较两个雇员的equals方法
```
class Employee
{
    ...
    public boolean equals(Employee other)
    {
        return name.equals(other.name);
    }
}
```
调用方式是
```
if (harry.equals(boss)) ...
```
这个方法访问harry的私有域，这点并不会让人奇怪，然而，它还访问了boss的私有域，这是合法的。
其原因是boss是Employee类对象，而Employee类的方法可以访问Employee类的任何一个对象的私有域。

#### 私有方法

在实现一个类时，由于公有数据非常危险，所以应该将所有的数据域设置为私有的。然而方法又应该如何设计，
尽管绝大多数方法都被设计为公有的，但在某些特殊情况下，也可能将它们设计为私有的。有时，可能希望
将一个计算代码划分成若干个独立的辅助方法等。
在Java中，为了实现一个私有的方法，只需要将关键字public改为private即可。

#### final 实例域

可以将实例域定义为final。构建对象时必须初始化这样的域，也就是说，必须确保在每一个构造器执行之后，
这个域的值被设置，并且在后面的操作中，不能够再对它进行修改。

final修饰符大都应用于基本（primitive）类型域，或不可变（immutable）类的域（如果类中的每个方法
都不会改变其对象，这种类就是不可变的类）


















