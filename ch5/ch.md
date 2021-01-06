
## 第五章 继承

- 类、超类和子类
- Object所有类的超类
- 泛型数组列表
- 对象包装器与自动装箱
- 参数数量可变的方法
- 枚举类
- 反射
- 继承的设计技巧

第4章主要阐述了类和对象的概念，本章将学习面向对象程序设计的另外一个基本概念：继承（inheritance）。
利用继承，人们可以基于已经存在的类构造一个新类，继承已经存在的类就是复用（继承）这些类的方法和域。
在此基础上，还可以添加一些新的方法和域，以满足需求。这是Java程序设计中一项核心技术。

另外，本章还阐述了反射（reflection）的概念。反射是指在程序运行期间发现更多的类及其属性的能力。
这是一个功能强大的特性，使用起来也比较复杂。主要是开发软件工具的人员，而不是编写应用程序的人员
对这项功能感兴趣。


### 类、超类和子类

以Employee类为例，假设某个公司，这个公司经理的待遇和普通员工的待遇存在一些差异，不过他们之间
也存在着很多相同的地方。例如，普通雇员在完成本职任务之后仅领取薪水，而经理在完成了预期的业绩之后
还能得到奖金。这种情形就需要使用继承，需要为经理定义一个新类Manager，以便更加一些新功能。

在Manager与Employee之间存在着明显的"is-a"关系，每个经理都是一名雇员，"is-a"关系是继承的
一个明显特征。

#### 定义子类

关键字 extends 表示继承
```
public class Manager extends Employee
{
    ...
}
```

Java与C++定义继承类的方式十分相似，Java用关键字extends代替了C++中的冒号`:`。在Java中，所有
的继承都是公有继承，而没有C++中的私有继承和保护继承。

关键字extends表明正在构造的新类派生于一个已经存在的类。已存在的类称为超类（superclass）、基类（base class）
或父类（parent class）。新类称为子类（subclass）、派生类（derived class）或孩子类（child class）。
超类和子类是Java程序员常用的术语，而其他程序员可能更加偏爱使用父类和孩子类，这些都是继承时使用的术语。

前缀"超"和"子"来源于计算机科学和数学理论中的集合语言的术语。雇员集合是经理集合的超集，经理集合是
雇员集合的子集。
```
public class Manager extends Employee
{
    private double bonus;
    ...
    public void setBonus(double bonus)
    {
        this.bonus = bonus;
    }
}
```
尽管在Manager类中没有显式地定义getName和getHireDay等方法，但属于Manager类的对象却可以使用
它们，这是因为Manager类自动地继承了超类Employee中的这些方法。
同样，从超类中还继承了name、salary和hireDay这3个域。这样一来，每个Manager类对象就包含了
4个域name、salary、hireDay和bonus。

在通过扩展超类定义子类的时候，仅需要指出子类与超类的不同之处。因此在设计类的时候，应该将通用的方法
放在超类中，而将具有特殊用途的方法放在子类中。这种将通用的功能放到超类的做法，在面向对象程序设计中
十分普遍。

#### 覆盖方法

超类中的有些方法对子类Manager并不一定适用。具体来说，Manager类中的getSalary方法应该返回薪水
和奖金的总和，因此，需要提供一个新的方法来覆盖（override）超类中的这个方法。

Manager类的getSalary是要返回salary和bonus域的总和。这里需要指出，我们需要调用超类Employee中
的getSalary方法，可以使用特定的关键字super解决这个问题。
```
public double getSalary()
{
    double baseSalary = super.getSalary();
    return baseSalary + bonus;
}
```

有人认为super与this引用是类似的概念，实际上，这样比较不太恰当。这是因为super不是一个对象
的引用，它只是一个指示编译器调用超类方法的特殊关键字。

在子类中可以增加域、增加方法或覆盖超类的方法，然而绝对不能删除继承的任何域和方法。

在Java中使用关键字super调用超类的方法，而在C++中则采用超类名加上`::`操作符的形式。应该将
super.getSalary替换为Employee::getSalary。

#### 子类构造器

我们来提供一个构造器
```
public Manager(String name, double salary, int year, int month, int day)
{
    super(name, salary, year, month, day);
    bonus = 0;
}
```
由于Manager类的构造器不能访问Employee类的私有域，所以必须利用Employee类的构造器对这部分
私有域进行初始化，我们可以通过super实现对超类构造器的调用。使用super调用构造器的语句必须是
子类构造器的第一条语句。

如果子类的构造器没有显式地调用超类的构造器，则将自动地调用超类默认（没有参数）的构造器。
如果超类没有不带参数的构造器，并且在子类的构造器中又没有显式地调用超类的其他构造器，则Java编译器
将报告错误。

在C++的构造函数中，使用初始化列表语法调用超类的构造函数，而不是调用super。
```
// C++
Manager::Manager(String name, double salary, int year, int month, int day)
: Employee(name, salary, year, month, day)
{
    bonus = 0
}
```

一个对象变量可以指示多种实际类型的现象被叫做多态（ploymorphism），在运行时能够自动地选择调用
哪个方法的现象称为动态绑定（dynamic binding）。

#### 继承层次

继承并不仅限于一个层次。例如，可以由Manager类派生Executive类。由一个公有超类派生出来的所有类
的集合被称为继承层次（inheritance hierarchy），在继承层次中，从某个特定的类到其祖先的路径被
称为该类的继承链（inheritance chain）。通常一个祖先类可以拥有多个子孙继承链。

#### 多态































