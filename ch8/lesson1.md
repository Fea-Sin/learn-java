
## 具体的集合

Java类库集合，并简要描述了每个集合类的用途。除了以Map结尾的类之外，其他类都实现了Collection接口，
而以Map结尾的类实现了Map接口。

| 集合类型 | 描述 |
|----|----|
| ArrayList | 一种可以动态增长和缩减的索引序列 |
| LinkedList | 一种可以在任何位置进行高效的插入和删除操作的有序序列 |
| ArrayDeque | 一种用循环数组实现的双端队列 |
| HashSet | 一种没有重复元素的无序集合 |
| TreeSet | 一种有序集 |
| EnumSet | 一种包含枚举类型值的集 |
| LinkedHashSet | 一种以记住元素插入次序的集 |
| PriorityQueue | 一种允许高效删除最小元素的集合 |
| HashMap | 一种存储键\/值关联的数据结构 |
| TreeMap | 一种键值有序排列的映射表 |
| EnumMap | 一种键值属于枚举类型的映射表 |
| LinkedHashMap | 一种可以记住键\/值项添加次序的映射表 |
| WeakHashMap | 一种其值无用武之地后可以被垃圾回收器回收的映射表 |
| IdentityHashMap | 一种用==而不是用equals比较键值的映射表 |


### 链表

在本书中，有很多示例已经使用了数组以及动态的ArrayList类。然而，数组和数组列表都有一个重大的缺陷。
这就是从数组的中间位置删除一个元素要付出很大的代价，其原因是数组中处于被删除元素之后的所有元素
都要想数组的前端移动。这数组中间的位置上插入一个元素也是如此。

另外一个大家非常熟悉的数据结构--链表（linked list）解决了这个问题。尽管数组在连续的存储位置
上存放对象引用，但链表却将每个对象存放在独立的结点中。每个结点还存放着序列中下一个结点的引用。
在Java程序设计语言中，所有链表实际上都是双向链接的（doubly linked），即每个结点还存放着
指向前驱结点的引用。

从链表中间删除一个元素是一个很轻松的操作，即需要更新被删除元素附近的链接。

你也许曾经在数据结构课程中学习过如何实现链表的操作。在链表中添加或删除元素时，绕来绕去的指针可能已经
给人们留下了极坏的印象。
下面的代码示例中，先添加3个元素，然后再将第2个元素删除
```
List<String> staff = new LinkedList<>();  // LinkedList implements List
staff.add("Amy");
staff.add("Bob");
staff.add("Carl");

Iterator iter = staff.iterator();
String first = iter.next(); // visit first element
String second = iter.next(); // visit second element
iter.remove(); // remove last visited element
```
但是，链表与泛型集合之间有一个重要的区别。链表是一个有序集合（ordered collection），每个对象的
位置十分重要。LinkedList.add方法将对象添加到链表的尾部。但是，常常需要将元素添加到链表的中间。
由于迭代器是描述集合中位置的，所以这种依赖位置的add方法将由迭代器负责。只有对自然有序的集合使用
迭代器添加元素才有实际意义。例如，下一节将要讨论的集（Set）类型，其中的元素完全无序。因此，在Iterator
接口中就没有add方法。相反地，集合类库提供了子接口ListIterator，其中包含add方法。
```
interface ListIterator<E> extends Iterator<E>
{
    void add(E element);
    ...
}
```
与Collection.add不同，这个方法不返回boolean类型的值，它假定添加操作总会改变链表。
另外，ListIterator接口有两个方法，可以用来反向遍历链表
```
E previous()
boolean hashPrevious()
```
与next方法一样，previous方法返回越过的对象。
LinkedList类的listIterator方法返回一个实现了ListIterator接口的迭代器对象。
```
ListIterator<String> iter = staff.listIterator();
```
Add方法在迭代器位置之前添加一个新对象。例如，下面的代码将越过链表中的第一个元素，并在第二个
元素之前添加"Juliet"
```
List<String> staff = new LinkedList<>();
staff.add("Amy");
staff.add("Bob");
staff.add("Carl");
ListIterator<String> iter = staff.listIterator();
iter.next(); // skip past first element
iter.add("Juliet");
```

如果多次调用add方法，将按照提供的次序把元素添加到链表中。它们被依次添加到迭代器当前位置之前。

当用一个刚刚由Iterator方法返回，并且指向链表表头的迭代器调用add操作时，新添加的元素将变成列表的新
表头。当迭代器越过链表的最后一个元素时（即hasNext返回false），添加的元素将变成列表的新表尾。
如果链表有n个元素，有n+1个位置可以添加新元素。这些位置与迭代器的n+1个可能的位置相对应。

add方法只依赖于迭代器的位置，而remove方依赖于迭代器的状态。

最后需要说明，set方法用一个新元素取代调用next或previous方法返回的上一个元素。例如，下面的代码将用
一个新值取代链表的第一个元素
```
ListIterator<String> iter = list.listIterator();
String  oldValue = iter.next();  // returns first element
iter.set(newValue); // sets first element to newValue
```
可以想象，如果在某个迭代器修改集合时，另一个迭代器对其进行遍历，一定会出现混乱的状况。

为了避免发生并发修改的异常，请遵循下述简单规则，可以根据需要给容器附加许多的迭代器，但是这些迭代器只能
读取列表。另外，再单独附加一个既能读又能写的迭代器。

对于并发修改列表的检测有一个奇怪的例外。链表只负责跟踪对列表的结构性修改，例如，添加元素、删除元素。
set方法不被视为结构性修改。可以将多个迭代器附加给一个链表，所有的迭代器都调用set方法对现有结点的
内容进行修改。

现在已经介绍了LinkedList类的各种基本方法。可以使用ListIterator类从前后两个方向遍历链表中的元素，
并可以添加、删除元素。

Collection接口声明了许多用于对链表操作的有用方法，其中大部分方法都是在LinkedList类的超类AbstractCollection
中实现的。例如，toString方法调用了所有元素的toString，并产生一个很长的格式为`[A, B, C]`的字符串。
这为调试工作提供了便利。可以使用contains方法检测某个元素是否出现在链表中。例如，如果链表中包含一个
等于"Harry"的字符串，调用staff.contains("Harry")后会返回true。

在Java类库中，还提供了许多在理论上存在一定争议的方法。链表不支持快速的随机访问。如果要查看链表中第n
个元素，就必须从头开始，越过n-1个元素。没有捷径可走。鉴于这个原因，在程序需要采用整数索引访问元素时，
程序员通常不选用链表。

尽管如此，LinkedList类还是提供了一个用来访问某个特定元素的get方法
```
LinkedList<String> list = ...;
String obj = list.get(n)
```












