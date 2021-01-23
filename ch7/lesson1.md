
## 泛型代码和虚拟机

虚拟机没有泛型类型对象，所有对象都属于普通类。

### 类型擦除

无论何时定义了一个泛型类型，都自动提供了一个相应都原始类型（raw type）。原始类型的名字
就是删去类型参数后的泛型类型名。擦除（erased）类型变量，并替换为无限定类型（无限定的变量用Object）

例如，Pair<T>的原始类型如下
```
public class Pair
{
    private Object first;
    private Object second;
    
    public Pair(Object first, Object second)
    {
        this.first = first;
        this.second = second;
    }
    
    public Object getFirst() {return first;}
    public Object getSecond() {return second;}
    
    public void setFirst(Object newValue) {first = newValue;}
    public void setSecond(Object newValue) {second = newValue;}
}
```
因为T是一个无限定的变量，所以直接用Object替换。

原始类型用第一个限定的类型变量来替换，如果没有给定限定就用Oject替换。例如类Pair<T>
中的类型变量没有显式的限定，因此，原始类型用Object替换T。假定声明了一个不同的类型
```
public class Interval<T extends Comparable & Serializable> implements Serializable
{
    private T lower;
    private T upper;
    ...
    public Interval(T first, T second)
    {
        if (first.compareTo(second) <= 0) { lower = first; upper = second; }
        else {lower = second; upper = first;}
    }
}
```
原始类型Interval如下所示
```
public class Interval implements Serializable
{
    private Comparable lower;
    privata Comparable upper;
    ...
    public Interval(Comparable first, Comparable second) {...}
}
```

### 翻译泛型表达式

当程序调用泛型方法时，如果擦除返回类型，编译器插入强制类型转换。例如，下面这个语句序列
```
Pair<Employee> buddies = ...;
Employee buddy = buddies.getFirst();
```
擦除getFirst的返回类型将返回Object类型。编译器自动插入Employee的强制类型转换。
也就是说，编译器把这个方法调用翻译为两条虚拟机指令

- 对原始方法Pair.getFirst的调用
- 将返回的Object类型强制转换为Employee类型

### 翻译泛型方法














