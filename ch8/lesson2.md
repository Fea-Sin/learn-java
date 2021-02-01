
## 映射

集是一种集合，它可以快速地查找现有的元素。但是，要查看一个元素，需要有要查找元素的精确副本。
这不是一种非常通用的查找方式。通常，我们直到某些键的信息，并想要查找与之对应的元素。
映射（map）数据结构就是为此设计的。映射用来存放键/指对。如果提供了键，就能够查找到值。
例如，有一张关于员工信息的记录表，键为员工的ID，值为Employee对象。

### 基本映射操作

Java类库为映射提供了两个通用的实现，HashMap和TreeMap。这两个类都实现了Map接口。
散列映射对键进行散列，树映射用键的整体顺序对元素进行排序，并将其组织成搜索树。
散列或比较函数只能作用于键。与键关联的值不能进行散列或比较。

应该选择散列映射还是树映射呢？与集一样，散列稍微快一些，如果不需要按照排列顺序访问键，就最好
选择散列。
```
Map<String, Employee> staff = new HashMap<>();  // HashMap implements Map
Employee harry = new Employee("Harry Hacker");
staff.put("987-98-9996", harry);
```
每当往映射中添加对象时，必须同时提供一个键。在这里，键是一个字符串，对应的值是Employee对象。
要想检索一个对象，必须使用（因而，必须记住）一个键。
```
String id = "987-98-9996";
e = staff.get(id); // get harry
```
如果在映射中没有与给定键对应的信息，get将返回null。
null返回值可能并不方便。有时可以有一个好的默认值，用作为映射中不存在的键。然后，使用getOrDefault方法。
```
Map<String, Integer> scores = ...;
int score = scores.get(id, 0);  // gets 0 if the id is not present
```
键必须是唯一的。不能对同一个键存放两个值。如果对同一个键两次调用put方法，第二个值就会取代第一个值。
实际上，put将返回这个键参数存储的上一个值。

remove方法用于从映射中删除给定键对应的元素。size方法用于返回映射中的元素数。要迭代处理映射的键和值，
最容易的方法是使用forEach方法。可以提供一个接收键和值的lambda表达式。映射中的每一项会依序调用这个表达式。
```
scores.forEach((k, v) ->
    System.out.println("key=" + k + ", value=" + v));
```

### 更新映射项

处理映射时的一个难点就是更新映射项。正常情况下，可以得到与一个键关联的原值，完成更新，在放回更新后的值。
不过，必须考虑一个特殊情况，即键第一次出现。

下面看一个例子，使用一个映射统计一个单词在文件中出现的频度，看到一个单词（word）时，我们将计数器增1。

作为一个简单的补救，可以使用getOrDefault方法
```
counts.put(word, counts.getOrDefault(word, 0) + 1);
```
不过还可以做得更好。merge方法可以简化这个常见的操作。
```
counts.merge(word, 1, Integer::sum);
```
将把word与1关联，否则使用Integer::sum函数将原值与1求和。


### 映射视图

集合框架不认为映射本身是一个集合。（其他数据结构框架认为映射是一个键/值对集合，或者是由键索引的值集合）。
不过，可以得到映射的视图（view），这是实现了Collection接口或某个子接口的对象。

有3种视图，键集、值集合以及键/值对集。键和键/值对可以构成一个集，因为映射中的一个键只能有一个副本。
```
Set<K> keySet()
Collection<V> values()
Set<Map.Entry<K, V>> entrySet()
```
需要说明的是，keySet不是HashSet或TreeSet，而是实现了Set接口的另外某个类的对象。Set接口扩展了
Collection接口。因此，可以像使用集合一样使用keySet。

例如，可以枚举一个映射的所有键
```
Set<String> keys = map.keySet();
for (String key : keys)
{
    do something with key
}
```
如果想同时查看键和值，可以通过枚举条目来避免查找值
```
for (Map.Entry<String, Employee> entry : staff.entrySet())
{
    String k = entry.getKey();
    Employee v = entry.getValue();
    do something with k, v
}
```

上述是访问所有映射条目的最高效的方法。如今，只需要使用forEach方法
```
counts.forEach((k, v) -> {
    do something with k, v
})
```

### 弱散列映射

在集合类库中有几个专门的映射类。

设计WeakHashMap类是为了解决一个有趣的问题。如果有一个值，对应的键已经不再使用了，将会出现什么情况呢？
假定对某个键的最后一次引用已经消亡，不再有任何途径引用这个值的对象了。但是，由于在程序中的任何部分没有
再出现这个键，所以，这个键/值对无法从映射中删除。为什么垃圾回收器不能够删除它呢？难道删除无用的对象
不是垃圾回收器的工作码？

遗憾的是，事情没有这样简单。垃圾回收器跟踪活动的对象。只要映射对象是活动的其中的所有桶也是活动的，
它们不能被回收。因此，需要由程序负责从长期存活的映射表中删除那些无用的值。或者使用WeakHashMap完成
这件事情。当对键的唯一引用来自散列条目时，这一数据结构将与垃圾回收器协同工作一起删除键/值对。

下面是这种机制的内部运行情况。WeakHashMap使用弱引用（weak references）保存键。WeakReference
对象将引用保存到另外一个对象中，在这里，就是散列键。对于这种类型的对象，垃圾回收器用一种特有的
方式进行处理。通常，如果垃圾回收器发现某个特定的对象已经没有他人引用了，就将其回收。然而，如果某个
对象只能由WeakReference引用，垃圾回收器仍然回收它，但要将引用这个对象的弱引用放入队列中。
WeakHashMap将周期型地检查队列，以便找出新添加的若引用。一个弱引用进入队列意味着这个键不再被
他人使用，并且已经被收集起来。于是，WeakHashMap将删除对应的条目。

### 链接散列集与映射

LinkedHashSet和LinkedHashMap类用来记住插入元素项的顺序。这样就可以避免在散列表中的
项从表面上看是随机排列的。当条目插入到表中时，就会并入到双向链表中。
```
Map<String, Employee> staff = new LinkedHashMap<>();
staff.put("144-25-5464", new Employee("Amy Lee"));
staff.put("567-24-2546", new Employee("Harry Hacker"));
staff.put("157-62-7935", new Employee("Gary Cooper"));
staff.put("456-62-5527", new Employee("Francesca Cruz"));
```
然后，staff.keySet().iterator()以下面的次序枚举键
```
144-25-5464
567-24-2546
157-62-7935
456-62-5527
```
并且staff.values().iterator()以下列顺序枚举这些值
```
Amy Lee
Harry Hacker
Gary Cooper
Francesca Cruz
```
链接散列映射将用访问顺序，而不是插入顺序，对映射条目进行迭代。每次调用get或put，
受到影响的条目将从当前的位置删除，并放到条目链表的尾部（只有条目在链表中的位置受到影响，而散列表
中的桶不会受到影响。一个条目总位于与键散列码对应的桶中）。要想构造这样一个散列映射表，请调用
```
LinkedHashMap<K, V>(initialCapacity, loadFactor, true)
```
访问顺序对于实现高速缓存的"最近最少使用"原则十分重要。例如，可能希望将访问频率高的元素放在
内村中，而访问频率第的元素则从数据库中读取。当在表中找不到元素项且表又已经满时，可以将迭代器
加入到表中，并将枚举的前几个元素删除掉。这些是近期最少使用的几个元素。

甚至可以让这一过程自动化。即构造一个LinkedHashMap的子类，然后覆盖下面这些方法
```
protected boolean removeEldestEntry(Map.Entry(K, V) eldest)
```
每当方法返回true时，就添加一个新条目，从而导致删除eldest条目。

### 枚举集与映射

EnumSet是一个枚举类型元素集的高效实现。由于枚举类型只有有限个实例，所以EnumSet内部用位序列实现。
如果对应的值在集中，则相应的位被置为1。

EnumSet类没有公共的构造器。可以使用静态工厂方法构造这个集
```
enum Weekday { MONDAY, TUESDAY, WEDESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY };
EnumSet<Weekday> always = EnumSet.allOf(Weekday.class)
```
可以使用Set接口的常用方法来修改EnumSet。
EnumMap是一个键类型为枚举类型的映射。它可以直接且高效地用一个值数组实现。在使用时，需要在构造器中
指定键类型
```
EnumMap<Weekday, Employee> personIncharge = new EnumMap<>(Weekday.class);
```

所有的枚举类型都扩展于泛型Enum类，例如，Weekday 扩展 Enum<Weekday>

### 标识散列映射

类IdentityHashMap有特殊的作用。在这个类中，键的散列值不是用hashCode函数计算的，而是用
System.identityHashCode方法计算的。这是Object.hashCode方法根据对象的内存地址来计算散列码
时所使用的方式。而且，在对两个对象进行比较时，IdentityHashMap类使用==，而不使用equals。

也就是说，不同的键对象，即使内容相同，也被视为是不同的对象。在实现对象遍历算法时，这个类非常有用，
可以用来跟踪每个对象的遍历状况。






















