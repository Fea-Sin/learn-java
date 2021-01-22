

## 第7章 泛型程序设计

- 为什么要使用泛型程序设计
- 定义简单泛型类
- 泛型方法
- 泛型变量的限定
- 泛型代码和虚拟机
- 约束与局限性
- 泛型类型的继承规则
- 通配符类型
- 反射和泛型

从Java程序设计语言1.0版本发布以来，变化最大的部分就是泛型。
泛型正是我们需要的程序设计手段。使用泛型机制编写的程序代码要比那些杂乱地使用Object变量，
然后再进行强制类型转换的代码具有更好的安全性和可读性。泛型对于集合类尤其有用。例如，ArrayList
就是一个无处不在的集合类。

至少在表面上看来，泛型很像C++中的模版。与Java一样，在C++中，模版也是最先被添加到语言中支持
强类型集合的。

### 为什么要使用泛型程序设计

泛型程序设计（Generic programming）意味着编写的代码可以被很多不同类型的对象所重用。
例如，我们并不希望为聚集String和File对象分别设计不同的类。实际上，也不需要这样做，因为
一个ArrayList类可以聚合任意类型的对象。这是一个泛型程序设计的实例。

#### 类型参数的好处

在Java中增加泛型之前，泛型程序设计是用继承实现的。ArrayList类只维护一个Object引用的数组
```
public class ArrayList
{
    private Object[] elementData;
    ...
    public Object get(int i) {...}
    public void add(Object o) {...}
}
```
这种方法有两个问题，当获取一个值时必须进行强制类型转换。
```
ArrayList files = new ArrayList();
String filename = (String) files.get(O);
```
此外，这里没有错误检查。可以向数组列表中添加任何类的对象。
```
files.add(new File("..."));
```
对于这个调用，编译和运行都不会出错。然而在其他地方，如果将get的结果强制类型转换为String类型，
就会产生一个错误。

泛型提供了一个更好的解决方案，类型参数（type parameters）。ArrayList类有一个类型参数用来
指示元素的类型
```
ArrayList<String> files = new ArrayList<String>();
```
这使得代码具有更好的可读性。人们一看就知道这个数组列表中包含的是String对象。
编译器也可以很好的利用这个信息。当调用get的时候，不需要进行强制类型转换，编译器就
知道返回值类型为String，而不是Object
```
String filename = files.get(O);
```
编译器还知道ArrayList<String>中add方法有一个类型为String的参数。这将比使用Object类型
的参数安全一些。现在，编译器可以进行检查，避免插入错误类型的对象。例如
```
files.add(new File("...")); // can only add String objects to an ArrayList<String>
```
是无法通过编译的。出现编译错误比类在运行时出现的强制类型转换异常要好得多。

类型参数的魅力在于：使得程序具有更好的可读性和安全性。

#### 谁想成为泛型程序员

使用像ArrayList的泛型类很容易。大多数Java程序员都使用ArrayList<String>这样的类型，
就好像它们已经构建在语言之中，像String[]数组一样。（当然，数组列表比数组要好一些，因为
它可以自动扩展）。

但是，实现一个泛型类并没有那么容易，对于类型参数，使用这段代码的程序员可能想要内置所有
的类。他们希望在没有过多的限制以及混乱的错误消息的状态下，做所有的事情。因此，一个泛型
程序员的任务就是预测出所有类的未来可能有的所有用途。

这一任务难到什么程度呢？ArrayList类有一个方法addAll用来添加另一个集合的全部元素。程序员
可能想要将ArrayList<Manager>中的所有元素添加到ArrayList<Employee>中去。然而，
返回来就不行了。如果只能允许前一个调用，而不能允许后一个调用呢？Java语言的设计者发明了一个
具有独创的新概念，通配符类型（wildcard type），它解决了这个问题，通配符类型非常抽象，
然而，它们能让库的构建者编写出尽可能灵活的方法。

泛型程序设计划分为3个能力级别。基本级别是，仅仅使用泛型类--典型的是像ArrayList这样的集合，
必须考虑它们的工作方式与原因，大多数应用程序员将停留在这一级别上，直到出现了什么问题。
当把不同的泛型类混合在一起时，或是在与对类型参数一无所知的遗留的代码进行衔接时，可能
会看到含混不清的错误消息。如果这样的话，就需要学习Java泛型来系统地解决这些问题，而不要
胡乱地猜测。当然，最终可能要实现自己的泛系与泛系方法。

### 定义简单泛型类

一个泛型类（generic class）就是具有一个或多个类型变量的类。本章使用一个简单的Pair类作为
例子。对于这个类来说，我们只关注泛型，而不会为数据存储的细节烦恼。
```
public class Pair<T>
{
    private T first;
    private T second;
    
    public Pair() { first = null; second = null; }
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }
    
    public T getFirst() { return first; }
    public T getSecond() { return second; }
    
    public void setFirst(T newValue) { first = newValue; }
    public void setSecond(T newValue) { second = newValue;}
}
```
Pair类引入了一个类型变量T，用尖括号<>括起来，并放在类名的后面。泛型类可以有多个
类型变量。例如，可以定义Pair类，其中第一域和第二个域使用不同的类型
```
public class Pair<T, U> {...}
```

类型变量使用大写形式，且比较短。在Java库中，使用变量E表示集合的元素类型，K和V分别代表
关键字与值的类型。T、U和S表示任意类型。

用具体的类型替换类型变量就可以实例化泛型类型，
```
Pair<String>
```
可以将结果想象成带有构造器的普通类
```
Pair<String>();
Pair<String>(String, String);
```
换句话说，泛型类可看作普通类的工厂。

### 泛型方法

前面已经介绍了如何定义一个泛型类。实际上，还可以定义一个带有类型参数的简单方法。
```
class ArrayAlg
{
    public static <T> getMiddle(T a)
    {
        return a[a.length / 2]
    }
}
```
这个方法是在普通类中定义的，而不是在泛型类中定义的。然而，这是一个泛型方法，可以从尖括号
和类型变量看出这一点。注意，类型变量放在修修饰符（public static）的后面，返回类型的前面。
泛型方法可以定义在普通类中，也可以定义在泛型类中。
当调用一个泛型方法时，在方法名前的尖括号中放入具体类型
```
String middle = ArrayAlg.<String>getMiddle("John", "Q.", "Public");
```
在这样情况下，方法调用中可以省略<String>类型参数。编译器有足够的信息能够推断出所调用的方法。
```
String middle = ArrayAlg.getMiddle("John", "Q.", "Public");
```

### 类型变量的限定

有时，类或方法需要对类型变量加以约束。下面是一个典型例子
```
class ArrayAlg
{
    public static <T> T min(T[] a)
    {
        if (a == null || a.length == 0) return null;
        T smallest = a[0];
        for (int i = 1; i < a.length; i++)
            if (smallest.compareTo(a[i] > 0)) smallest = a[i];
        return smallest;
    }
}
```
但是，这里有一个问题。变量smallest类型为T，这意味着它可以是任何一个类的对象。怎么才能
确信T所属的类有compareTo方法呢？

解决这个问题的方案是将T限制为实现了Comparable接口的类。可以通过对类行变量T设置限定（bound）
实现这一点
```
public static <T extends Comparable> T min(T[] a)...
```
现在，泛型min方法只能被实现了Comparable接口的类的数组调用。

在C++中不能对模版参数的类型加以限制。如果程序员用一个不适当的类型实例化一个模版，将会在
模版代码中报告一个通常含糊不清的错误消息。

读者或许会感到奇怪，在此为什么使用关键字extends而不是implements？毕竟Comparable是
一个接口。下面记法
```
<T extends BoundingType>
```
表示T应该是绑定类型的子类型（subtype）。T和绑定类型可以是类，也可以是接口。选择关键字
extends的原因是更接近子类的概念。

一个类型变量或通配符可以有多个限定
```
T extends Comparable & Serializable
```
限定类型用&分隔，而逗号用来分隔类型变量。

```
class ArrayAlg
{
    public static <T extends Comparable> Pair<T> minmax(T[] a)
    {
        if (a == null || a.length == 0) return null;
        T min = a[0];
        T max = a[0];
        for (int i = 1; i < a.length; i++)
        {
            if (min.compareTo(a[i]) > 0) min = a[i];
            if (max.compareTo(a[i]) < 0) max = a[i];
        }
        return new Pair<>(min, max)
    }
}
```



















