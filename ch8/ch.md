
## 第8章 集合

- Java集合框架
- 具体集合
- 映射
- 视图与包装器
- 算法
- 遗留的集合

在实现方法时，选择不同的数据结构会导致其实现风格以及性能存在着很大差异。需要快速地搜索成千上万个（甚至上百万）
有序的数据项吗？需要快速地在有序的序列中间插入元素或删除元素吗？需要建立键与值之间的关联吗？

本章将讲述如何利用Java类库帮助我们在程序设计中实现传统的数据结构。在大学的计算机科学课程中，
有一门叫做数据结构（Data Structures）的课程，通常要讲授一个学期，因此，有许许多多专门探讨这个重要主题
书籍。与大学课程所讲述的内部不同，这里，将跳过理论部分，仅介绍如何使用标准库中的集合类。

### Java 集合框架

Java 最初版本只为最常用的数据结构提供了很少的一组类，Vector、Stack、Hashtable、BitSet与Enumeration接口，
其中的Enumeration接口提供了一种用于访问任意容器中各个元素的抽象机制。这是一种很明智的选择，但要
想建立一个全面的集合类库还需要大量的时间和高超的技能。

随着Java SE 1.2的问世，设计人员感到是推出一组功能完善的数据结构的时机了。面对一个大堆相互矛盾的设计
策略，他们希望让类库规模小且易于学习，而不希望像C++的"标准模版库"（即STL）那样复杂，但却又希望能够得到
STL率先推出的"泛型算法"所具有的优点。他们希望将传统的类融入新的框架中。与所有的集合类库设计者一样，
他们必须做出一些艰难的选择，于是，在整个设计过程中，他们做出了一些独具特色的设计决定。本节将介绍Java
结合框架的基本设计，展示使用它们的方法，并解释一些颇具争议的特性背后的考虑。

### 将集合的接口与实现分离

与现代的数据结构类库的常见情况一样，Java集合类库也将接口（interface）与实现（implementation）分离。
首先，看一下人们熟悉的数据结构--队列（queue）是如何分离的。

队列接口指出可以在队列的尾部添加元素，在队列的头部删除元素，并且可以查看队列中元素的个数。当需要收集对象，
并按照"先进先出"的规则检索对象就应该使用队列。
队列接口的最简形式可能类似下面这样
```
public interface Queue<E> // a simplified form of the interface in the standard libaray
{
    void add(E element);
    E remove();
    int size();
}
```
这个接口并没有说明队列是如何实现的。队列通常有两种实现方式，一种是使用循环数组，另一种是使用链表。

每一个实现都可以通过一个实现了Queue接口的类表示
```
public class CircularArrayQueue<E> implements Queue<E> // not an actual library class
{
    private int head;
    private int tail;
    
    CircularArrayQueue(int capacity) {...}
    public void add(E element) {...}
    public E remove() {...}
    public int size() {...}
    private E[] elements;
}

public class LinkedListQueue<E> implements Queue<E> // not an actual library class
{
    private link head;
    private link tail;
    
    LinkedListQueue() {...}
    public void add(E element) {...}
    public E remove() {...}
    public int size() {...}
}
```
实际上，Java类库没有名为CircularArrayQueue和LinkedListQueue的类。这里这是以这些类作为示例，
解释一下集合接口与实现在概念上的不同。如果需要一个循环数组队列，就可以使用ArrayDeque类。如果需要
一个链表队列，就直接使用LinkedList类，这个类实现了Queue接口。

当在程序中使用队列时，一旦构建了集合就不需要知道究竟使用了那种实现。因此，只有在构建集合对象时，
使用具体的类才有意义。可以使用接口类型存放集合的引用。
```
Queue<Customer> expressLane = new CircularArrayQueue<>(100);
expressLane.add(new Customer("Harry"));
```

利用这种方式，一旦改变了想法，可以轻松使用另外一种不同的实现。只需要对程序的一个地方做出修改，
即调用构造器的地方。如果觉得LinkedListQueue是个更好的选择，就将代码修改为
```
Queue<Customer> expressLane = new LinkedListQueue<>();
expressLane.add(new Customer("Harry"));
```
循环数组是一个有界集合，即容量有限。如果程序中需要收集的对象数量没有上限，就最好使用链表来实现。

在研究API文档时，会发现另一组名字以Abstract开头的类，例如，AbstractQueue。这些类是为类库实现者而
设计的。如果想要实现自己的队列类，会发现扩展AbstractQueue类要比实现Queue接口中的所有方法轻松得多。

### Collection  接口

在Java类库中，集合类的基本接口是Collection接口。这个接口有两个基本方法
```
public interface Collection<E>
{
    boolean add(E element);
    Iterator<E> iterator();
    ...
}
```
除了这两个方法之外，还有几个方法，将在稍后介绍。
add方法用于向集合中添加元素。如果添加元素确实改变了集合就返回true，如果集合没有发生
变化就返回false。例如，如果试图向集合中添加一个对象，而这个对象已经在集合中存在，这个
添加请求就没有实效，因为集合中不允许有重复的对象。

iterator方法用于返回一个实现了Iterator接口的对象。可以使用这个迭代器对象依次访问集合中
的元素。

### 迭代器

Iterator接口包含4个方法
```
public interface Iterator<E>
{
    E next();
    boolean hashNext();
    void remove();
    default void forEachRemaining(Consumer<? super E> action);
}
```
通过反复调用next方法，可以逐个访问集合中的每个元素。但是，如果到达了集合的末尾，next方法将抛出一个
NoSuchElementException。因此，需要在调用next之前调用hashNext方法。如果迭代器对象还有多个供访问
的元素，这方法就返回true。如果想要查看集合中的所有元素，就请求一个迭代器，并在hashNext返回true时
反复地调用next方法。例如
```
Collection<String> c = ...;
Iterator<String> iter = c.iterator();
while(iter.hashNext())
{
    String element = iter.next();
    do something with element
}
```
用for each循环可以更加简练地表示同样的循环操作
```
for (String element : c)
{
    do something with element
}
```
编译器简单地将for each循环翻译为带有迭代器的循环。
for each循环可以与任何实现了 Iterable 接口的对象一起工作，这个接口只包含一个抽象方法
```
public interface Iterable<E>
{
    Iterator<E> iterator();
}
```
Collection接口扩展了Iterable接口。因此，对于标准类库中的任何集合都可以使用for each循环。

在Java SE 8中，甚至不用写循环。可以调用forEachRemaining方法并提供一个lambda表达式（它会处理每个元素）。将对
迭代器的每一个元素调用这个lambda表达式，直到再没有元素为止。

元素被访问的顺序取决于集合类型。如果对ArrayList进行迭代，迭代器将从索引0开始，每迭代一次，索引值加1。
然后，如果访问HashSet中的元素，每个元素将会按照某种随机的次序出现。虽然可以确定在迭代过程中能够遍历
到集合中的所有元素，但却无法预知元素被访问的次序。这对于计算总和或统计符合某个条件的元素个数这类与顺序
无关的操作来说，并不是什么问题。

Java集合类库中的迭代器与其他类库中的迭代器在概念上有着重要的区别。在传统的集合类库中，例如，C++的
标准模版库，迭代器是根据数组索引建模的。如果给定这样一个迭代器，就可以查看指定位置上的元素，就像知道
数组索引i就可以查看数组元素a[i]一样。不需要查找元素，就可以将迭代器向前移动一个位置。这与不需要执行
查找操作就可以通过i++将数组索引向前移动一样。但是，Java迭代器并不是这样操作的。查找操作与位置变更
是紧密相连的。查找一个元素的唯一方法是调用next，而在执行查找操作的同时，迭代器的位置随之向前移动。

因此，应该将Java迭代器认为是位于两个元素之间。当调用next时，迭代器就越过下一个元素，并返回刚刚越过的
那个元素的引用。

这里还有一个有用的推论。可以将Iterator.next与InputStream.read看作为等效的。从数据流中读取一个字节，
就会自动地消耗掉这个字节。下一次调用read将会消耗并返回输入的下一个字节。用同样的方式，反复地调用next就
可以读取集合中所有元素。

Iterator接口的remove方法将会删除上次调用next方法时返回的元素。在大多数情况下，在决定删除某个元素之前
应该看一下这个元素是很具有实际意义的。然而，如果想要删除指定位置上的元素，仍然需要越过这个元素。例如，
下面如何删除字符串集合中第一个元素的方法
```
Iterator<String> it = c.iterator();
it.next(); // skip over the first element
it.remove(); // now remove it
```
更重要的是，对next方法和remove方法的调用具有相互依赖性。如果调用remove之前没有调用next将是不合法的。

### 泛型实用方法

由于Collection和Iterator都是泛型接口，可以编写操作任何集合类型的实用方法。例如，下面是一个检测
任意集合是否包含指定元素的泛型方法
```
public static <E> boolean contains(Collection<E> c, Object obj)
{
    for (E element : c)
        if (element.equals(obj))
            return true;
    return false;
}
```
Java类库的设计者认为，这些实用方法中的某些方法非常有用，应该将它们提供给用户使用。这样，类库的
使用者就不必自己重新构建这些方法了。contains就是这样一个实用方法。
事实上，Collection接口声明了很多有用的方法，所有的实现类都必须提供这些方法。下面列举了其中的一部分
```
int size()
boolean isEmpty()
boolean contains(Object obj)
boolean equals(Object other)
boolean addAll(Collection<? extends E> from)
boolean remove(Object obj)
boolean removeAll(Collection<?> c)
void clear()
boolean retainAll(Collection<?> c)
Object[] toArray()
<T> T[] toArray(T[] arrayToFill)
```

当然，如果实现Collection接口的每一个类都要提供如此多的例行方法将是一件很烦人的事情。为了能够
让实现者更容易地实现这个接口，Java类库提供了一个类AbstractCollection，它将基础方法size和
iterator抽象化了，但是在此提供了例行方法
```
public abstract class AbstractCollection<E> implement Collection<E>
{
    ...
    public abstract Iterator<E> iterator();
    
    public boolean contains(Object obj)
    {
        for (E element : this) // calls iterator()
            if (element.equals(obj))
                return true;
        return false;
    }
    ...
}
```
此时，一个具体的集合类可以扩展AbstractCollection类了。现在要由具体的集合类提供iterator方法，而contains
方法已由AbstractCollection超类提供了。然而，如果子类有更加有效的方式实现contains方法，也可以由子类提供，
就这点而言，没有什么限制。

对于Java SE 8，这种方法有些过时了。如果这些方法是Collection接口的默认方法会更好。但实际上并不是这样，
不过，确实已经增加了很多默认方法。其中大部分方法都与流的处理有关。另外，还有一个很有用的方法
```
default boolean removeIf(Predicate<? super E> filter)
```
这个方法用于删除满足某个条件的元素。

### 集合框架中的接口

Java集合框架为不同类型的集合定义了大量接口。
集合有两个基本接口，Collection和Map。我们已经看到，可以用以下方法在集合中插入元素
```
boolean add(E element);
```
不过，由于映射包含键/值对，所以要用put方法来插入
```
V put(K key, V value)
```
要从集合读取元素，可以用迭代器访问元素。不过，从映射中读取值则要使用get方法。
```
V get(K key)
```
List是一个有序集合（ordered collection）。元素会增加到容器中的特定位置。可以采用两种方式访问元素，
使用迭代器访问，或者使用一个整数索引来访问。后一种方法称为随机访问（random access），因为这样可以
按任意顺序访问元素。与之不同，使用迭代器访问时，必须顺序地访问元素。

List接口定义了多个用于随机访问的方法
```
void add(int index, E element)
void remove(int index)
E get(int index)
E set(int index, E element)
```
ListIterator接口是Iterator的一个子接口。它定义了一个方法用于在迭代器位置前面增加一个元素
```
void add(E element)
```
坦率地讲，集合框架的这个方面设计得很不好。实际中有两种有序集合，其性能开销有很大差异。由数组支持的
有序集合可以快速地随机访问，因此适合使用List方法并提供一个整数索引来访问。与之不同，链表尽管也是
有序的，但是随机访问很慢，所以最好使用迭代器来遍历。如果原先提供两个接口就会容易一些了。

为了避免对链表完成随机访问操作，Java SE 1.4引入了一个标记接口RandomAccess。这个接口不包含任何方法，
不过可以用它来测试一个特定的集合是否支持高效的随机访问
```
if (c instanceof RandomAccess)
{
    use random access algorithm
}
else
{
    use sequential access algorithm
}
```
Set 接口等同于Collection接口，不过其方法的行为有更严谨定义。集（set）的add方法不允许增加重复
的元素。要适当地定义集的equals方法，只要两个集包含同样的元素就认为是相等的，而不要求这些元素有
同样的顺序。hashCode方法的定义要保证包含相同元素的两个集会得到相同的散列码。

既然方法签名是一样的，为什么还要建立一个单独的接口呢？从概念上讲，并不是所有集合都是集。建立一个
Set接口可以让程序员编写只接受集的方法。
SortedSet和SortedMap接口会提供用于排序的比较器对象，这两个接口定义了可以得到集合子集视图的方法。

最后，Java SE 6引入了接口NavigableSet和NavigableMap，其中包含一些用于搜索和遍历有序集和映射
的方法。（理想情况下，这些方法本应该直接包含在SortedSet和SortedMap接口中）TreeSet和TreeMap类
实现了这些接口。




















