
## 遗留的集合

从Java第1版问世以来，在集合框架出现之前已经存在大量"遗留的"容器类。这些类已经集成到集合框架中。

### Hashtable 类

Hashtable类与hashMap类的作用一样，实际上，它们拥有相同的接口。与Vector类的方法一样。
Hashtable的方法也是同步的。如果对同步性或与遗留代码的兼容性没有任何要求，就应该使用
HashMap。如果需要并发访问，则要使用ConcurrentHashMap。

### 枚举

遗留集合使用Enumeration接口对元素序列进行遍历。Enumeration接口有两个方法，即
hasMoreElements和nextElement。这两个方法与Iterator接口的hasNext方法和next方法
十分类似。
例如，Hashtable类的elements方法将产生一个用于描述表中各个枚举值的对象
```
Enumeration<Employee> e = staff.elements();
while(e.hasMoreElements())
{
    Employee e = e.nextElement();
}
```
有时还会遇到遗留的方法，其参数是枚举类型的。静态方法Collections.enumeration将产生
一个枚举对象，枚举集合中的元素。例如
```
List<InputStream> streams = ...;
SequenceInputStream in = new SequenceInputStream(Collections.enumeration(streams));
// the SequenceInputStream constructor expects an enumeration
```

在C++中，用迭代器作为参数十分普遍。幸好，在Java的编程平台中，只有极少的程序员沿用这种习惯。
传递集合要比传递迭代器更为明智。集合对象的用途更大。当接受方如果需要时，总是可以从集合中获得
迭代器，而且，还可以随时地使用集合的所有方法。不过，可能在某些遗留代码中发现枚举接口，因为
这是在Java SE 1.2的集合狂简出现之前，它们是泛型集合唯一可以使用的机制。

### 属性映射

属性映射（property map）是一个类型非常特殊的映射结构。它有下面3个特性

- 键与值都是字符串
- 表可以保存到一个文件中，也可以从文件中加载
- 使用一个默认的辅助表

实现属性映射的Java平台类称为Properties。
属性映射通常用于程序的特殊配置选项。

### 栈

从1.0版开始，标准类库中就包含了Stack类，其中有大家熟悉的push方法和pop方法。但是，
Stack类扩展为Vector类，从理论角度看，Vector类并不太令人满意，它可以让栈使用
不属于栈操作的insert和remove方法，即可以在任何地方进行插入或删除操作，而不仅仅在
栈顶。

### 位集

Java平台的BitSet类用于存放一个位序列（它不是数学上的集，称为位向量或位数组更为合适）。
如果需要高效地存储位序列（例如，标志）就可以使用位集。由于位集将位包装在字节里，所以，
使用位集要比使用Boolean对象的ArrayList更加高效。

BitSet类提供了一个便于读取、设置或清除各个位的接口。使用这个接口可以避免屏蔽和其他麻烦
的位操作。如果将这些位存储在int或long变量中就必须进行这些繁琐的操作。例如，对一个名
为bucketOfBits的BitSet
```
bucketOfBits.get(i)
```
如果第i位处于开状态，就返回true，否则就返回false。同样地
```
bucketOfBits.set(i)
```
将第i位置位开状态，最后，
```
bucketOfBits.clear(i);
```
将第i位置位关状态。

C++中的bitset模板与Java平台的BitSet功能一样。

到此为止，Java集合框架的旅程就结束了。正如所看到的，Java类库提供了大量的集合类
以适应程序设计的需要。




























