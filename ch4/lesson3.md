

## 静态域与静态方法

#### 静态域

如果将域定义为static，每个类中只有一个这样的域，这就是**静态域**。而每一个对象对于所有的**实例域**却有自己的一份拷贝。

给Employee类添加一个实例域和一个静态域
```
class Employee
{
    private static int nextId = 1;
    private int id;
}
```
现在每一个雇员对象都有一个自己的id域，但这个类的所有实例将共享一个nextId域。换句话说，如果有1000个
Employee类的对象，则有1000个实例域，但是只有一个静态域nextId，即使没有一个雇员对象，静态域nextId
也是存在，它属于类，而不属于任何对立的对象。

在绝大多数的面向对象程序设计语言中，静态域被称为类域。术语"static"只是沿用可C++的叫法。

下面实现一个简单的方法
```
public void setId()
{
    id = nextId;
    nextId++;
}
```
`harry.setId()`harry的id域被设置为静态域nextId当前的值，并且静态域nextId的值加1。

#### 静态常量

静态变量使用得比较少，但静态常量却使用得比较多。例如，在Math类中定义了一个静态常量
```
public class Math
{
    ...
    public static final double PI = 3.14159265358979323846
}
```
在程序中可以采用`Math.PI`的形式获得这个常量。

如果关键字static被省略，PI就变成了Math类的一个实例域。需要通过Math类的对象访问PI，并且每一个
Math对象都有它自己的一份PI拷贝。

另一个多次使用的静态常量是System.out。它在System类中声明
```
public class System
{
    ...
    public static final PrintStream out = ...;
}
```
前面曾曾经提到过，由于每个类对象都可以对公有域进行修改，所以最好不要将域设计为public。然而，
公有常量（即final域）却没有问题，因为out被声明为final，所以不允许再将其他打印流赋给它。
```
System.out = new PrintStream(...); // Error out is final
```

### 静态方法

静态方法是一种不能向对象实施操作的方法。例如，Math类的pow方法就是一个静态方法。`Math.pow(x, a)`
计算幂x的a次方。在计算时，不使用任何Math对象（换句话说，没有隐式的参数）。
可以认为静态方法是没有this参数的方法，在一个非静态的方法中，this参数表示这个方法的隐式参数。

Employee类的静态方法不能访问Id实例域，因为它不能操作对象，但是静态方法可以访问自身类中的静态域。
```
public static int getNextId()
{
    return nextId;
}
```
可以通过类名调用这个方法
```
int n = Employee.getNextId();
```
这个方法可以省略关键字static吗，答案是可定的。但是，需要通过Employee类**对象**的引用调用这个方法。

可以使用对象调用静态方法，可以用harry.getNextId()代替Employee.getNextId()，不过这种方式很容易
造成混淆，其原因是getNextId方法计算的结果与harry毫无关系。我们建议使用类名，而不是对象来调用静态方法。

在下面两种情况下使用静态方法

- 一个方法不需要访问对象状态，其所需参数都是通过显式参数提供（例如：Math.pow）
- 一个方法只需要访问类的静态域（例如：Employee.getNextId）

Java中的静态域域静态方法在功能上与C++相同。但是，语法与书写上却稍有不同。在C++中使用::操作符
访问自身作用域之外的静态域和静态方法，如Math::PI。

#### 工厂方法

静态方法还有另外一种常见的用途，类似LocalDate和NumberFormat的类使用静态工厂方法（factory method）
来构造对象。你已经见过工厂方法LocalDate.now和LocalDate.of等
```
NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
NumberFormat percentFormatter = NumberFormat.getPercentInstance();
double x = 0.1
System.out.println(currencyFormatter.format(x)); // $0.10
System.out.println(percentFormatter.format(x));  // 10%
```

为什么NumberFormat类不利用构造器完成这些操作呢？主要有两个原因

- 无法命名构造器，构造器的名字必须与类名相同，但是这里希望得到的货币实例和百分比实例采用不同的名字
- 当使用构造器时，无法改变所构造的对象类型，而Factory方法将返回一个DecimalFormat类对象，这是
NumberFormat的子类。

#### main 方法

需要注意，不需要使用对象调用静态方法。例如，不需要构造Math类就可以调用Math.pow。
同理，main方法也是一个静态方法
```
public class Application
{
    public static void main(String args)
    {
        // construct object here
        ...
    }
}
```

每一个类可以有一个main方法，这是一个常用于对类进行单元测试的技巧。
```
class Employee
{
    ...
    public static void main(String[] args)  // unit test
    {
        Employee e = new Employee("Romeo", 50000, 2003, 3, 31);
        e.raiseSalary(10);
        System.out.println(e.getName() + " " + e.getSalary());
    }
}
```
如果想要独立的测试Employee类，只需执行
```
java Employee
```

## 方法参数

首先回顾一下在程序设计语言中有关将参数传递给方法（函数）的一些专业术语。按值调用（call by value）
表示方法接收的是调用者提供的值。而按引用调用（call by reference）表示方法接收的是调用者提供的
变量地址。一个方法可以修改传递引用所对应的变量值，而不能修改修改传递值调用所对应的变量值。

"按...调用"是一个标准的计算机科学术语，它用来描述各种程序设计语言中方法参数的传递方式。
Java程序设计语言总是采用按值调用。也就是说，方法得到的是所有参数值的一个拷贝。

方法参数有两种类型

- 基本数据类型（数字、布尔值）
- 对象引用

一个方法不可能修改一个基本数据类型的参数，而对象引用作为参数就不同了，实现一个改变对象参数状态的方法
并不是一件难事，方法得到的是对象引用的拷贝，对象引用及其拷贝同时引用同一个对象。

> C++提供了两种参数传递方式，值调用和引用调用。引用参数标有&符号，可以轻松实现修改它们引用
> 参数的目的 void swap(Employee& x, Employee& y)

下面总结一下Java中方法参数的使用情况

- 一个方法不能修改一个基本数据类型的参数。
- 一个方法可以改变一个对象参数的状态
- 一个方法不能让对象参数引用一个新的对象


















