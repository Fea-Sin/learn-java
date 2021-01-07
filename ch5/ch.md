
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

有一个用来判断是否应该设计为继承关系的简单规则，这就是"is-a"规则，它表明子类的每个对象也是
超类的对象。"is-a"规则的另一种表述法是置换法则，它表明程序中出现超类对象的任何地方都可以用
子类对象置换。
例如，可以将一个子类的对象赋给超类的变量
```
Employee e;
e = new Employee(...)  // Employee object expected
e = new Manager(...)   // OK，Manager can be used as well
```

在Java程序设计语言中，对象变量是多态的。

```
Manager boss = new Manager(...);
Employee[] staff = new Employee[3];
staff[0] = boss;
```
在这个例子中，变量`staff[0]`与boss引用同一个对象，但编译器将`staff[0]`看成Employee对象。
```
boss.setBonus(5000); // ok

// 但不能这样调用
staff[0].setBonus(5000) // Error
```

不能将一个超类的引用赋给子类变量。例如，下面的赋值是非法的
```
Manager m = staff[i]; // Error
```

#### 理解方法调用

弄清楚如何在对象上应用方法调用非常重要。下面假设要调用x.f(args)，隐式参数x声明为类C的一个对象。
下面是调用过程的详细描述

1）编译器查看对象的声明类型和方法名。假设调用x.f(param)，且隐式参数x声明为C类的对象，需要注意的是，
有可能存在多个名字为f，但参数类型不一样的方法，例如可能存在方法f(int)和方法f(String)。编译器将会
一一列举所有C类中名为f的方法和超类中访问属性为public且名为f的方法（超类的私有方法不可访问）。
至此，编译器已经获得所有可能被调用的候选方法。

2）加下来，编译器将查看调用方法时提供的参数类型。如果在所有名为f的方法中存在一个与提供的参数类型完全
匹配，就选择这个方法。这个过程被称为重载解析（overloading resolution）。例如，对于调用
x.f("Hello")来说，编译器将会挑选f(String)。

由于允许类型转换（int可以转换成double，Manager可以转换成Employee），所以这个过程可能很复杂。
如果编译器没有找到与参数类型匹配的方法，或者发现经过类型转换后有多个方法与之匹配，就会报告一个错误。
至此，编译器已经获得需要调用的方法名字和参数类型。

**注释：**前面曾经说过，方法的名字和参数列表称为方法的签名。例如，f(int)和f(String)是两个具有相同
名字，不同签名的方法。如果在子类中定义了一个与超类签名相同的方法，那么类中的这个方法就覆盖了超类中的
这个相同签名的方法。
不过，返回类型不是签名的一部分，因此，在覆盖方法时，一定要保证返回类型的兼容性。

3）如果是private方法、static方法、final方法或者构造器，那么编译器将可以准确地知道应该调用
哪个方法，我们将这种调用方式称为静态绑定（static binding）。与此对应的是，调用的方法依赖于
隐式参数的实际类型，并且在运行是实现动态绑定。
至此，编译器采用动态绑定的方法生成一条调用f(String)的指令。

4）当程序运行时，并且采用动态绑定调用方法时，虚拟机一定调用与x所引用对象的实际类型最合适的那个类的方法。
每次调用方法都要进行搜索，时间开销相当大。因此，虚拟机预先为每个类创建了一个方法表（method table）,
其中列出了所有方法的签名和实际调用的方法。

动态绑定有一个非常重要的特性，无需对现存的代码进行修改，就可以对程序扩展。假设增加一个新类Executive，
并且变量e有可能引用这个类的对象，我们不需要对包含调用e.getSalary的代码进行重新编译。如果e恰好引用
一个Executive类的对象，就会自动地调用Executive.getSalary()方法。

**警告：**在覆盖一个方法的时候，子类方法不能低于超类方法的可见性。

#### 阻止继承：final类和方法

有时候，可能希望阻止人们利用某个类定义子类，不允许扩展的类被称为final类。如果在定义类的时候使用了
final修饰符就表明这个类是final类。例如，假设希望阻止人们定义Executive类的子类，就可以在定义
这个类的时候，使用final修饰符声明
```
public final class Executive extends Manager
{
    ...
}
```
类中的方法也可以被声明为final。如果这样做，子类就不能覆盖这个方法（final类中的所有方法自动
地成为final方法）。
```
public class Employee
{
    ...
    public final String getName()
    {
        return name;
    }
}
```
前面曾将说过，域也可以被声明为final。对于final域来说，构造对象之后不允许改变它们的值了。
不过，如果将一个类声明为final，只有其中的方法自动地成为final而不包括域。

有些程序员认为，除非有足够的理由使用多态性，应该将所有的方法都声明为final。事实上，在C++和C#
中，如果没有特别地说明，所有的方法都不具有多态性。这两种做法可能都有些偏激。我们提倡在设计
类层次时，仔细地思考应该将哪些方法和类声明为final。

如果一个方法没有被覆盖且很短，编译器就能够对它进行优化处理，这个过程称为内联（inlining）。例如，
内联调用e.getName()将被替换为访问e.name域。

#### 强制类型转换

在第3章曾经讲过，将一个类型强制转换称另外一个类型的过程被称为类型转换。Java程序设计提供了一种
专门进行类型转换的表示法。例如
```
double x = 3.405;
int nx = (int)x;
```
将表达式x的值转换成整数类型，舍弃了小数部分。

正像有时候将浮点型数值转换成整型数值一样，有时候也可能需要将某个类的对象引用转换成另外一个类
的对象引用。例如
```
Manager boss = (Manager)staff[0];
```

如果试图在继承链上进行向下的类型转换，并且"谎报"有关对象包含的内容，会发生什么情况？
```
Manager boss = (Manager) staff[1]; // Error
```
运行这个程序时，Java运行时系统将报告这个错误，并产生一个ClassCastException异常。如果没有
捕获这个异常，那么程序就会终止。因此，应该养成一个良好的程序设计习惯，在进行类型转换之前，
先查看一下是否成功转换。这个过程简单地使用instanceof操作符就可以实现。
```
if (staff[1] instanceof Manager)
{
    boss = (Manager)staff[1];
}
```
最后，如果这个类型转换不成功，编译器就不会进行转换。

- 只能在继承层次内进行类型转换
- 在将超类转换成子类之前，应该使用instanceof 进行检查

实际上，通过类型转换调整对象的类型并不是一种好的做法。大多数情况下并不需要将Employee对象转换成Manager
对象，两个类的对象都能够正确调用getSalary方法，这是因为实现多态性的动态绑定机制能够自动地找到
相应的方法。
只有在使用Manager中特有的方法时才需要进行类型转换。

> Java使用的类型转换语法来源于C语言"以往糟糕的日子"

#### 抽象类

如果自下而上在类的继承层次结构中上移，位于上层的类更具有通用性，甚至可能更加抽象。从某种角度看，
祖先类更加通用，人们只将它作为派生其他类的基类，而不作为想使用的特定的实用类。

包含一个或多个抽象方法的类必须是抽象类。
除了抽象方法外，抽象类还可以包括具体数据和具体方法。例如
```
public abstract class Person
{
    private String name;
    
    public Person(String name)
    {
        this.name = name;
    }
    public abstract String getDescription();
    
    public String getName()
    {
        return name;
    }
}
```
抽象方法充当着占位的角色，它们的具体实现在子类中。类即使不含有抽象方法，也可以将类声明为抽象类。
抽象类不能被实例化。也就是说，将一个类声明为abstract，就不能创建这个类的对象。但可以创建一个
具体子类的对象。

需要注意，可以定义一个抽象类的对象变量，但是它只能引用非抽象子类的对象。
```
Person p = new Student("Vince Vu", "Econmics");
```

**注释：**在C++中，有一种在尾部用 =0 标记的抽象方法，称为纯虚函数，例如
```
// C++
class Person
{
    virtual string getDescription() =0;
    
    ...
}
```
只要有一个纯虚函数，这个类就是抽象类，在C++中，没有提供用于表示抽象类的特殊关键字。

下面定义一个扩展抽象类Person的具体子类Student
```
public class Student extends Person
{
    private String major;
    
    public Student(String name, String major)
    {
        super(name);
        this.major = major;
    }
    public String getDescription()
    {
        return "a student majoring in " + major;
    }
}
```

有些人可能对下面这个调用感到困惑
```
Person[] people = new Person[1]
people[0] = new Student(...)
for (Person p : people)
    System.out.println(p.getName() + "," + p.getDescription());
```
p这不是调用了一个没有定义的方法吗，由于不能构造抽象类Person的对象，所以变量p永远不会引用Person对像，
而是引用诸如Student这样的具体子类对象，而这些对象都定义了getDescription方法。

是否可以省略Person超类中的抽象方法，而仅在Student子类中定义getDescription方法呢？如果这样的话，
就不能通过变量p调用getDescription方法了。编译器只允许调用在类中声明的方法。

在Java程序设计语言中，抽象方法是一个重要的概念。在接口（interface）中将会看到更多的抽象方法。


#### 受保护访问

归纳一下Java用于控制可见性的4个访问修饰符

1）仅本类可见 private

2）对所有类可见 public

3）对本包和所有子类可见 protected

4）对本包可见 默认，不需要修饰符






















