

## 约束与局限性

下面几节中，将阐述使用Java泛型时需要考虑的一些限制。大多数限制都是由类型擦除引起的。

### 不能用基本类型实例化类型参数

不能用类型参数代替基本类型。因此，没有Pair<double>，只有Pair<Double>。当然，
其原因是类型擦除。擦除之后，Pair类含有Object类型的域，而Object不能存储double值。

这的确令人烦恼。但是，这样做与Java语言中基本类型的独立状态相一致。这并不是一个致命的缺陷，
只有8中基本类型，当包装器类型（wrapper type）不能接受替换时，可以使用独立的类和方法
处理它们。

### 运行时类型查询只适用于原始类型

虚拟机中对象总有一个特定的非泛型类型。因此，所有的类型查询只产生原始类型。

试图查询一个对象是否属于某个泛型类型时，倘若使用instanceof会得到一个编译错误，如果
使用强制类型转换会得到一个警告。
```
if (a instanceof Pair<String>) // Error

if (a instanceof Pair<T>)  // Error
```

### 不能创建参数化类型的数组

不能实例化参数类型的数组
```
Pair<String>[] table = new Pair<String>[10];  // Error
```
这有什么问题呢？擦除之后，table的类型是Pair[]。可以把它转换为Object[]
```
Object[] objarray = table;
```
数组会记住它的元素类型，如果试图存储其他类型的元素，就会抛出一个Array-StoreException异常。
不过对于泛型类型，擦除会使这种机制无效。以下赋值
```
objarray[0] = new Pair<Employee>();
```
能够通过数组存储检查，不过仍会导致一个类型错误。出于这个原因，不允许创建参数化类型的数组。

需要说明的是，只是不允许创建这些数组，而声明类型为Pair<String>的变量仍是合法的。不过
不能用`new Pair<String>[10]`初始化这个变量。

### Varargs 警告

上一节已经了解到，Java不支持泛型类型的数组。这一节中我们在来讨论一个相关的问题，
向参数个数可变的方法传递一个泛型类型的实例
```
public static <T> void addAll(Collection<T> coll, T... ts)
{
    for(t : ts) coll.add(t)
}
```
应该记得，实际上参数ts是一个数组，包含提供的所有实参。
```
Collection<Pair<String>> table = ...;
Pair<String> pair1 = ...;
Pair<String> pair2 = ...;
addAll(table, pair1, pair2)
```
为了调用这个方法，Java虚拟机必须建立一个Pair<String>数组，这就违反了前面的规则。
不过，对于这种情况，规则有所放松，你只会得到一个警告，而不是错误。
可以采用两种方法来抑制这个警告。一种方法是为包含addAll调用的方法增加注解@SupperssWarnings("unchecked")。
还可以用@SafeVarargs直接标注addAll方法
```
@SafeVarargs
public static <T> void addAll(Collection<T> coll, T... ts)
```

### 不能实例化类型变量

不能使用像new T(...)，new T[...]或T.class这样的表达式中的类型变量。
类型擦数将T改变成Object，而且，本意肯定不希望调用new Object()。在Java SE 8之后，
最好的解决办法是让调用者提供一个构造器表达式。
```
Pair<String> p = Pair.makePair(String::new);
```
makePair方法接收一个Supplier<T>，这是一个函数式接口，表示一个无参数而且返回类型为T的函数
```
public static <T> Pair<T> makePair(Supplier<T> constr)
{
    return new Pair<>(constr.get(), constr.get());
}
```

这样设计API以便得到一个Class对象
```
public static <T> Pair<T> makePair(Class<T> cl)
{
    try { return new Pair<>(cl.newInstance(), cl.newInstance()); }
    catch(Exception ex) { return null; }
}
```
这个方法可以按照下列方式调用
```
Pair<String> p = Pair.makePair(String.class)
```
注意，Class类本身是泛型。例如，String.class是一个Class<String>的实例（事实上，它是唯一的实例）。
因此，makePair方法能够推断出Pair的类型。

### 不能构造泛型数组

就像不能实例化一个泛型实例一样，也不能实例化数组。不过原因有所不同，毕竟数组会填充null值，
构造时看上去是安全的，不过，数组本身也有类型，用来监控存储在虚拟机中的数组。这个类型会被擦除
```
public static <T extends Comparable> T[] minmax(T[] a)
{
    T[] mm = new T[2];  // Error
}
```
类型擦除会让这个方法永远构造Comparable[2]数组。

### 泛型类的静态上下文中类型变量无效

不能在静态域或方法中引用类型变量。例如
```
public class Singleton<T>
{
    private static T singleInstance; // Error
    
    public static T getSingleInstance()  // Error
    {
        if (singleInstance == null) construct new instance of T
        return singleInstance;
    }
}
```
如果这个程序能够运行，就可以声明一个Singleton<Random>共享随机数生成器，声明一个
Singleton<JFileChooser>共享文件选择对话框。但是，这个程序无法工作。类型擦除
之后，只剩下Singleton类，它只包含一个singleInstance域。因此，禁止使用带有
类型变量的静态域和方法。

### 不能抛出或捕获泛型类的实例

既不能抛出也不能捕获泛型类对象。实际上，甚至泛型类扩展Throwable都是不合法的。例如，
以下不能正常编译
```
public class Problem<T> extends Exception {...} // Error can't extend Throwable
```
catch 子句中不能使用类型变量
```
publice static <T extends Throwable> void doWork(Class<T> t)
{
    try
    {
        do work
    }
    catch(T e)  // Error can't catch type variable
    {
        Logger.global.info(...)
    }
}
```

**注意擦除后的冲突**

## 泛型类型的继承规则



















