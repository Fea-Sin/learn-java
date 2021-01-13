

## 接口、lambda表达式与内部类

- 接口

- 接口示例

- lambda 表达式

- 内部类

- 代理

首先，介绍一下接口（interface）技术，这种技术主要用来描述类具有什么功能，而并不是给出每个
功能的具体实现。一个类可以实现（implement）一个或多个接口，并在需要接口的地方，随便使用
实现了相应接口的对象。了解接口以后，在继续介绍lambda表达式，这是一种表示可以在将来某个时间
点执行的代码块的简洁方法。使用lambda表达式，可以用一种精巧而简洁的方式表示使用回调或变量行为
的代码。

内部类（inner class）机制。理论上讲，内部类有些复杂，内部类定义在另外一个类的内部，其中
的方法可以访问包含它们的外部类的域。内部类技术主要用于设计具有相互协作的类集合。

在本章的最后还将介绍代理（proxy），这是一种实现任意接口的对象。代理是一种非常专业的构造工具，
它可以用来构建系统级的工具。

### 接口

在Java程序设计语言中，接口不是类，而是对类的一组需求描述，这些类要尊从接口描述的统一格式进行定义。

"如果类尊从某个特定的接口，那么就履行这项服务"。下面给出一个具体的示例。Arrays类中的sort方法
承诺可以对对象数组进行排序，但要求满足下列前提：对象所属的类必须实现了Comparable接口。

下面是Comparable接口的代码：
```
public interface Comparable
{
    int compareTo(Object other);
}
```
这就是说，任何实现Comparable接口的类都需要包含compareTo方法，并且这个方法的参数必须是一个
Object对象，返回一个整型数值。

接口中的所有方法自动地属于public。因此，在接口中声明方法时，不必提供关键字public。

上面这个接口只有一个方法，而有些接口可能包含多个方法。稍后可以看到，在接口中还可以定义常量。
然而，更为重要的是要知道接口不能提供哪些功能。接口绝不能含有实例域，在Java SE 8之前，
也不能在接口中实现方法。现在已经可以在接口中提供简单方法了。当然，这些方法不能引用实例域，
接口没有实例。

提供实例域和方法实现的任务应该由实现接口的那个类来完成。因此，可以将接口看成是没有实例域的
抽象类。

现在，假设希望使用Arrays类的sort方法对Employee对象数组进行排序，Employee类就必须实现
Comparable接口。

为了让类实现一个接口，通常需要下面两个步骤：

1）将类声明为实现实现给定的接口
2）对接口中的所有方法进行定义

要将类声明为实现某个接口，需要使用关键字implements:
```
class Employee implements Comparable
```
当然，这里的Employee类需要提供compareTo方法。假设希望根据雇员的薪水进行比较。
```
public int compareTo(Object otherObject)
{
    Employee other = (Employee) otherObject;
    return Double.compare(salary, other.salary);
}
```
这里，我们使用了静态Double.compare方法，如果第一个参数小于第二个参数，它会返回一个负值，
如果二者相等则返回0，否则返回一个正值。

在接口声明中，没有将compareTo方法声明为public，这是因为在接口中的所有方法都自动地是public。
不过，在实现接口时，必须把方法声明为public。

我们可以做得更好一些。可以为泛型Comparable接口提供一个类型参数
```
class Employee implements Comparable<Employee>
{
    public int compareTo(Employee other)
    {
        return Double.compare(salary, other.salary);
    }
    ...
}
```

### 接口的特性

接口不是类，尤其不能使用new运算符实例化一个接口。然而，尽管不能构造接口的对象，却能声明
接口的变量：
```
Comparable x;  // OK
```
接口变量必须引用实现了接口的类的对象
```
x = new Employee(...); // OK provided Employee implements Comparable
```
接下来，如同使用instanceof检查一个对象是否属于某个特定类一样，也可以使用instanceof检查一个
对象是否实现了某个特定的接口
```
if (anObject instanceof Comparable) {...}
```

与可以建立类的继承关系一样，接口也可以被扩展。这里允许存在多条从具有较高通用性的接口到较高专用
性的接口的链。例如，假设有一个称为Moveable的接口：
```
public interface Moveable
{
    void move(double x, double y);
}
```
然后，可以以它为基础扩展一个叫做Powered的接口：
```
public interface Powered extends Moveable
{
    double milesPerGallon();
}
```
虽然在接口中不能包含实例域或静态方法，但却可以包含常量。
```
public interface Powered extends Moveable
{
    double milesPerGallon();
    double SPEED_LIMIT = 95; // a public static final constant
}
```
与接口中的方法都自动地被设置为public一样，接口中的域将自动设为public static final。

尽管每个类只能够拥有一个超类，但却可以实现多个接口。这就为定义类的行为提供了极大的灵活性。
例如，Java程序设计语言有一个非常重要的内置接口，称为Cloneable。如果某个类实现了这个Cloneable
接口，Object类中的clone方法就可以创建类对象的一个拷贝。如果希望自己设计的类拥有克隆和比较
的能力，只要实现这两个接口就可以了。
```
class Employee implements Cloneable, Comparable
```

### 接口与抽象类

为什么Java程序设计语言还要不辞辛苦地引入接口概念？为什么不将Comparable直接设计成抽象类
```
abstract class Comparable
{
    public abstract int compareTo(Object other);
}
```
然后，Employee类再直接扩展这个个抽象类，并提供compareTo方法的实现
```
class Employee extends Comparable
{
    public int compareTo(Object other){...}
}
```
每个类只能扩展于一个类。假设Employee类已经扩展于一个类，例如Person，它就不能再扩展第二类了
```
class Employee extends Person, Comparable // Error
```
但类可以实现多个接口
```
class Employee extends Person implements Comparable // OK
```

有些程序设计语言允许一个类有多个超类。例如C++，我们将此特性称为多重继承（multiple inheritance）。
而Java的设计者选择了不支持多继承，其主要原因是多继承会让语言本身变得非常复杂，效率也会降低。
实际上，接口可以提供多重继承的大多数好处，同时还能避免多重继承的复杂性和低效性。

### 静态方法

在Java SE 8中，允许在接口中增加静态方法。从理论上讲，没有任何理由认为这是不合法的。
只是这有违于接口作为抽象规范的初衷。

### 默认方法

可以为接口方法提供一个默认实现。必须用default修饰符标记这样一个方法。
public interface Comparable<T>
{
    default int compareTo(T other) { return 0; }
    // By default, all elements are the same
}
当然，这并没有太大用处，因为Comparable的每一个实际实现都要覆盖这个方法。不过有些情况下，
默认方法可能很有用。可以让程序员只需要为他们真正关心的方法覆盖相应的处理方法。

默认方法的一个重要用法是"接口演化"（interface evolution）。以Collection接口为例，
这个接口作为Java的一部分已经很多年了。假设很久以前提供了这样一个类
```
public class Bag implements Collection
```
后来，在Java SE 8中，又为这个接口增加了一个stream方法。
假设stream方法不是一个默认方法。那么Bag类将不能编译，因为它没有实现这个新方法。
为接口增加一个非默认方法不能保证"源代码兼容"（source compatible）。

不过，假设不重新编译这个类，而只是使用原先的一个包含这个类的JAR文件。这个类仍能正常加载，
尽管没有这个新方法。程序仍然可以正常构造Bag实例，不会又意外发生。不过，如果程序在一个Bag
实例上调用stream方法，就会出现一个AbstractMethodError。
将方法实现为一个默认方法就可以解决这两个问题。Bag类又能正常编译了。另外如果没有重新编译
而直接加载这个类，并在一个Bag实例上调用stream方法，将调用Collection.stream方法。

### 解决默认方法冲突

如果先在一个接口中将一个方法定义为默认方法，然后又在超类或另一个接口中定义了同样的方法，
会发生什么情况？诸如Scala和C++等语言对于解决这种二义性有一些复杂的规则。幸运的是，
Java的相应规则要简单得多。

1）超类优先。如果超类提供了一个具体方法，同名而且有相同参数类型的默认方法会被忽略。

2）接口冲突。如果一个超接口提供了一个默认方法，另一个接口提供了一个同名而且参数类型
（不论是否是默认参数）相同的方法，必须覆盖这个方法来解决冲突。

下面来看第二个规则。考虑另一个包含getName方法的接口
```
interface Named
{
    default String getName()
    {
        return getClass().getName() + "_" + hashCode();
    }
}
```
如果有一个类同时实现了这两个接口会怎么样呢
```
class Student implements Person, Named
```
类会继承Person和Named接口提供的两个不一致的getName方法。并不是从中选择一个，Java编译器
会报告一个错误，让程序员来解决这个二义性。只需要在Student类中提供一个getName方法。在这个方法中，
可以选择两个冲突方法中的一个，如下所示：
```
class Student implements Person, Named
{
    public String getName()
    {
        return Person.super.getName();
    }
}
```
现在假设Named接口没有为getName提供默认实现
```
interface Named
{
    String getName();
}
```
Student类会从Person接口继承默认方法吗？这好像挺有道理，不过，Java设计者更强调一致性。
两个接口如何冲突并不重要。如果至少有一个接口提供了一个实现，编译器就会报告错误，而程序员
就必须解决这个二义性。

当然，如果两个接口都没有为共享方法提供默认实现，那么就于Java SE 8之前的情况一样，这里不存在冲突。
实现类可以有两个选择：实现这个方法，或者干脆不实现。如果是后一种情况，这个类本身就是抽象的。

我们只讨论两个接口的命名冲突。现在来考虑另一种情况，一个类扩展了一个超类，同时实现了一个接口，
并从超类和接口继承了相同的方法。例如，假设Person是一个类
```
class Student extends Person implements Named {...}
```
在这种情况下，只会考虑超类方法，接口的对应默认方法都会忽略。这正是"类优先"规则。



















