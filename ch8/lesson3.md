
## 视图与包装器

通过使用视图（views）可以获得其他的实现了Collection接口和Map接口的对象。映射类的keySet方法就是一个这样的示例。
初看起来，好像这个方法创建了一个新集，并将映射中的所有键都填进去，然后返回这个集。但是，
情况并非如此。取而代之的是，keySet方法返回一个实现Set接口的类对象，这个类的方法对原映射进行操作。
这种集合称为视图。
视图技术在集合框架中有许多非常有用的应用。

### 轻量级集合包装器

Arrays类的静态方法asList将返回一个包装了普通Java数组的List包装器。这个方法可以将数组
传递给一个期望得到列表或集合参数的方法。
```
Card[] cardDeck = new Card[52];

List<Card> cardList = Arrays.asList(cardDeck);
```
返回的对象不是ArrayList，它是一个视图对象，带有访问底层数组的get和set方法。
改变数组大小的所有方法（与迭代器相关的add和remove方法）都会抛出一个Unsupported OperationException异常。

asList方法可以接收可变参数目的参数
```
List<String> names = Arrays.asList("Amy", "Bob", "Carl");
```
这个方法调用
```
Collections.nCopies(n, anObject);
```
将返回一个实现了List接口的不可修改的对象，并给人一种包含n个元素，每个元素都像是一个anObject的错觉。
例如，下面的调用将创建一个包含100个字符串的List，每个串都被设置为"DEFAULT"。
```
list<String> setting = Collection.nCopies(100, "DEFAULT");
```
存储代价很小。这是视图技术的一种巧妙应用。

Collection类包含很多实用方法，这些方法的参数和返回值都是集合。不要将它与Collection接口混淆起来。

调用下列方法
```
Collections.singleton(anObject);
```
将返回一个视图对象，这个对象实现了Set接口。返回的对象实现了一个不可修改的单元素集，而不需要
付出建立数据结构的开销。singletonList方法与singletonMap方法类似。

类似地，对于集合框架中的每一个接口，还有一些方法可以生成空集、列表、映射等等。
特别是，集的类型可以推导得出
```
Set<String> deepThoughts = Collection.emptySet();
```

### 子范围

可以为很多集合建立子范围（subrange）视图。例如，假设有一个列表staff，想从中取出第10个~第19个元素，
可以使用subList方法来获得一个列表的子范围视图
```
List group2 = staff.subList(10, 20);
```
第一个索引包含在内，第二个索引则不包含在内。
可以将任何操作应用与子范围，并且能够自动地反映整个列表的情况。例如，可以删除整个子范围
```
group2.clear();  // staff reduction
```
现在，元素自动地从staff列表中清除了，并且group2为空。

对于有序集和映射，可以使用排序顺序而不是元素位置建立子范围。SortedSet接口声明了3个方法。
```
SortedSet<E> subSet(E from, E to)
SortedSet<E> headSet(E to)
SortedSet<E> tailSet(E from)
```
这些方法将返回大于等于from且小于to的所有元素子集。有序映射也有类似的方法
```
SortedMap<K, V> subMap(K from, K to)
SortedMap<K, V> headMap(K to)
SortedMap<K, V> tailMap(K from)
```
返回映射视图，该映射包含键落在指定范围内的所有元素。

Java SE 6引入的NavigableSet接口赋予子范围操作更多的控制能力。可以指定是否包括边界
```
NavigableSet<E> subSet(E from, boolean fromInclusive, E to, boolean toInclusive)
```

### 不可修改的视图

Collections还有几个方法，用于产生集合的不可修改视图（unmodifiable views）。这些视图对现有集合
增加了一个运行时的检查。如果发现视图对集合进行修改，就抛出一个异常。同时这个集合将保持未修改的状态。

每个方法都定义于一个接口。例如，Collections.unmodifiableList与ArrayList、LinkedList或者
任何实现了List接口的其他类一起协同工作。

例如，假设想要查看某部分代码，但又不触及某个集合的内容，就可以进行下列操作
```
List<String> staff = new LinkedList();

lookAt(Collections.unmodifiableList(staff));
```
Collections.unmodifiableList方法将返回一个实现List接口的类对象。其访问器方法从staff集合中
获取值。当然，lookAt方法可以调用List接口中的所有方法，而不只是访问器。但是所有的更改器方法（例如，add）
已经被重新定义为抛出一个UnsupportedOperationException异常，而不是将调用传递给底层集合。

不可修改视图并不是集合本身不可修改。仍然可以通过集合的原始引用对集合进行修改。并且
仍然可以让集合的元素调用更改器方法。

由于视图只是包装了接口而不是实际的集合对象，所以只能访问接口中定义的方法。例如，LinkedList类
有一些非常方便的方法，addFirst和addLast，它们都不是List接口的方法，不能通过不可修改
视图进行访问。

### 同步视图

如果由多个线程访问集合，就必须确保集不会被意外地破坏。例如，如果一个线试图将元素添加到散列列表中，
同时另一个线程正在对散列列表进行再散列，器结果将是灾难性的。

类库的设计者使用视图机制来确保常规集合的线程安全，而不是实现线程安全的集合类。例如，Collections类
的静态synchronizedMap方法可以将任何一个映射表转换成具有同步访问方法的Map。
```
Map<String, Employee> map = Collections.synchronizedMap(new HashMap<String, Employee>());
```
现在，就可以由多线程访问map对象了。想get和put这类方法都是同步操作的，即在另一个线程调用另一个方法之前，
刚才的方法调用必须彻底完成。

### 受查视图

"受查"视图用来对泛型类型发生问题时提供调试支持。实际上将错误类型的元素混入泛型集合中的问题极有可能发生。例如
```
ArrayList<String> strings = new ArrayList<>();
ArrayList rawList = strings; // warning only, not an error, for compatibility with legacy code
rawList.add(new Date()); // now strings contains a Date object!
```
这个错误的add命令在运行时检测不到。相反，只有在稍后的另一部分代码中调用get方法，并将结果转化为String时，
这个类才会抛出异常。

收查视图可以探测到这类问题。下面定义了一个安全的列表
```
List<String> safeString = Collections.checkedList(strings, String.class);
```
视图的add方法将检测插入的对象是否属于给定的类。如果不属于给定的类，就立即抛出一个ClassCastException。
这样做的好处是错误可以在正确的位置得以报告。


### 关于可选操作的说明

通常，视图有一些局限性，即可能只可以读、无法改变大小、只支持删除而不支持插入，这些与映射的键
视图情况相同。如果视图进行不恰当的操作，受限制的视图就会抛出一个UnsupportedOperationException。

在集合和迭代器接口的API文档中，许多方法描述为"可选操作"。这看起来与接口的概念有所抵触。
毕竟，接口的设计目的难道不是负责给出一个类必须实现的方法吗？确实，从理论的角度看，这里给出的
方法很难令人满意。一个更好的解决方案是为每个只读视图和不能改变集合大小的视图建立各自独立的两个
接口。不过，这将会使接口的数量成倍增长，这让类库设计者无法接受。

时候应该"可选"方法这以技术扩展到用户的设计中呢？我们认为不应该。尽管结合被频繁地使用，其实现代
代码的风格也未必使用与其他问题领域。集合类库的设计者必须解决一组特别严格且又相互冲突的需求。用户
希望类库应该易于学习、使用方便，彻底泛型化，面向通用性，同时又与手写算法一样高效。要同时
达到所有目标的要求，或这尽量兼顾所有目标完全是不可能的。但是，在自己的编程问题中，很少遇到这样极端
的局限性。




























