

## 算法

泛型集合接口有一个很大的优点，即算法只需要实现一次。例如，考虑以下计算集合中最大元素这样一个简单
的算法。使用传统方式，程序设计人员可能会用循环实现这个算法。下面是找出数组中最大元素的代码
```
if (a.length == 0) throw new NoSuchElementException();
T largest = a[0];
for (int i = 1; i < a.length; i++)
    if (largest.compareTo(a[i]) < 0)
        largest = a[i];
```

当然，为找出数组列表中的最大元素所编写的代码会与此稍有差别
```
if (v.size() == 0) throw new NoSuchElementException();
T largest = v.get(0);
for (int i = 1; i < v.size(); i++)
    if (largest.compareTo(v.get(i)) < 0)
        largest = v.get(i)
```

链表应该怎么做呢？对于链表来说，无法实施高效的随机访问，但却可以使用迭代器
```
if (l.isEmpty()) throw new NoSuchElementException();
Iterator<T> iter = l.iterator();
T largest = iter.next();
while(iter.hashNext())
{
    T next = iter.next();
    if (largest.compareTo(next) < 0)
        largest = next;
}
```
编写这些循环代码有些乏味，并且也很容易出错。是否存在严重错误？对空容器循环正常工作吗？
对于只包有一个元素的容器又会发生什么情况呢？我们不希望每次都测试和调试这些代码，
也不想实现下面这一系列的方法
```
static <T extends Comparable> T max(T[] a)
static <T extends Comparable> T max(ArrayList<t> v)
static <T extends Comparable> T max(LinkedList<T> l)
```
这正是集合接口的用武之地。为了高效地使用这个算法所需要的最小集合接口。可以将max方法实现为
能够接收任何实现了Collection接口的对象。
```
public static <T extends Comparable> T max(Collection<T> c)
{
    if (c.isEmpty()) throw new NoSuchElementException();
    Iterator<T> iter = c.iterator();
    T largest = iter.next();
    while(iter.hasNext())
    {
        T next = iter.next();
        if (largest.compareTo(next) < 0)
            largest = next;
    }
    return largest;
}
```
现在就可以使用一个方法计算链表、数组列表和数组中组大元素了。

这是一个非常重要的概念。事实上，标准的C++类库已经又几十种非常有用的算法，每个算法都是
在泛型集合上操作的。Java类库中的算法没有如此丰富，但是，也包含了基本的排序、二分查找等实用算法。

### 排序与混排

计算机行业的前辈们有时会回忆起他们当年不得不使用穿孔卡片以及手工地编写排序算法的情形。当然，
如今排序算法已经成为大多数编程语言标准库中的一个组成部分，Java程序设计语言也不例外。

Collections类中的sort方法可以对实现了List接口的集合进行排序.
```
List<String> staff = new LinkedList<>();
fill collection
Collections.sort(staff)
```
这个方法假定列表元素实现了Comparable接口。如果想采取其他方式对列表进行排序，
可以使用List接口的sort方法并传入一个Comparator对象。可以如下按工资对一个员工列表排序
```
staff.sort(Comparator.comparingDouble(Employee::getSalary));
```
如果想按照降序对列表进行排序，可以使用一种非常方便的静态方法Collections.reverseOrder()。
这个方法将返回一个比较器，比较器返回b.compareTo(a)，例如
```
staff.sort(Comparator.reverseOrder())
```
这个方法将根据元素类型的compareTo方法给定排序顺序，按照逆序对列表staff进行排序。
```
staff.sort(Comparator.comparingDouble(Emloyee::getSalary).reversed())
```
将按工资逆序排序。

集合类库中使用的排序算法比快速排序要慢一些，快速排序是通用排序算法的传统选择。但是，
归并排序有一个主要优点：稳定，即不需要交换相同的元素。为什么要关注相同的元素的顺序呢？
下面是一种常见的情况，假设有一个已经按照姓名排序的员工列表，现在，要按照工资再进行排序。
如果两个雇员的工资相等发生什么情况呢？如果采用稳定的排序算法，将会保留按名字排列的顺序。
换句话说，排序的结果将会产生这样一个列表，首先按照工资排序，工资相同者再按照姓名排序。

因为集合不需要实现所有的"可选"方法，因此，所有接受集合参数的方法必须描述什么时候可以
安全地将集合传递给算法。例如，显然不能将unmodifiableList列表传递给排序算法。
可以传递什么类型的列表呢？根据文档，列表必须是可修改的，但不必是可以改变大小的。

下列是有关的术语定义

- 如果列表支持set方法，则是可修改的
- 如果列表支持add和remove方法，则是可改变大小的。

Collections类有一个算法shuffle，其功能与排序刚好相反，即随机的混排列表中元素的顺序。
```
ArrayList<Card> cards = ...;
Collections.shuffle(cards);
```
如果提供的列表没有实现RandomAccess接口，shuffle方法将元素复制到数组中，然后打乱数组元素的顺序，
最后再将打乱顺序后的元素复制回列表。


### 二分查找

要想在数组中查找一个对象，通常要以此访问数组中的每个元素，直到找到匹配的元素为止。
然而，如果数组是有序的，就可以直接查看位于数组中间的元素，看一看是否大于要查找的元素。
如果是，用同样的方法在数组的前半部分继续查找；否则，用同样的方法在数组的后半部分继续查找。
这样就可以将查找范围缩减一半。一直用这种方式查找下去。例如，如果数组中有1024个元素，
可以在10次比较后定位所匹配的元素（或者可以确认在数组中不存在这样的元素），而使用线性查找，
如果元素存在，平均需要512次比较；如果元素不存在，需要1024次比较才可以确认。

Collections类的binarySearch方法实现了这个算法。注意，集合必须是排好序的，否则算法
将返回错误的答案。要想查找某个元素，必须提供集合（这个集合要实现List接口，下面还要更加详细地
介绍这个问题）以及要查找的元素。如果集合没有采用Comparable接口的compareTo方法进行排序，
就还要提供一个比较器对象。
```
i = Collections.binarySearch(c, element);
i = Collections.binarySearch(c, element, comparator);
```

如果binarySearch方法返回的数值大于等于0，则表示匹配对象的索引。也就是说，
c.get(i)等于在这个比较顺序下的element。如果返回负值，则表示没有匹配的元素。但是，
可以利用返回值计算应该将element插入到集合的哪个位置，以保持集合的有序性。插入的位置是
```
insertionPoint = -i - 1;
```
这并不是简单的-i，因为0值是不确定的。也就是说，下面这个操作
```
if (i < 0)
    c.add(-i - 1, element);
```
将把元素插入到正确的位置上。

只有采用随机访问，二分查找才有意义。如果必须利用迭代方式一次次地遍历链表的一半元素来找
到中间位置的元素，二分查找就完全失去了优势。因此，如果为binarySearch算法提供一个链表，
它将自动地变为线性查找。

### 简单算法

在Collections类中包含了几个简单且很有用的算法。前面介绍的查找集合中最大元素的示例就
在其中。另外还包括，将一个列表中的元素复制到另外一个列表中，用一个常量值填充容器，逆
置一个列表的元素顺序。

为什么在标准库汇中提供这些简单算法呢？大多数程序员肯定可以很容易地采用循环实现这些算法。
我们之所以喜欢这些算法是因为，它们可以让程序员阅读算法变成一件轻松的事情。当阅读由别人
实现的循环时，必须要揣摩编程者的意图。而在看到诸如Collections.max这样的方法调用时，一定会立刻明白其
用途。例如，看下面这个循环
```
for (int i = 0; i < words.size(); i++)
    if (words.get(i).equals("C++")) words.set(i, "Java")；
```
现在将这个循环与以下调用比较
```
Collections.replaceAll("C++", "Java");
```
看到这个方法调用，你马上就能知道这个代码要做什么。

Java SE 8增加了默认方法Collection.removeIf和List.replaceAll，这两个方法稍有些复杂。
要提供一个lambda表达式来测试或转换元素。例如，下面的代码将删除所有短词，并把其余单词改为小写
```
words.removeIf(w -> w.length() <= 3);
words.replaceAll(String::toLowerCase);
```

### 批操作

很多操作会"成批"复制或删除元素。以下调用
```
coll1.removeAll(Coll2);
```
从coll1中删除coll2中出现的所有元素。与之相反
```
coll1.retainAll(coll2)
```
会从coll1中删除所有未在coll2中出现的元素。下面是一个典型的应用
假设希望找出两个集的交集（intersection），也将是两个集中公有的元素。首先，建立
一个新集来存放结果
```
Set<String> result = new HashSet<>(a);
```
在这里，我们利用了一个事实，每一个集合都有这样一个构造器，其参数是包含初始值的另一个集合。
现在来使用retainAll方法
```
result.retainAll(b);
```
这会保留恰好也在b中出现的所有元素。这样就构成了交集，而无需编写循环。

可以把这个思路更进一步，对视图应用一个批操作。例如，假设有一个映射，将员工ID映射到员工对象，
而且建立一个将不再聘用的所有员工的ID。
```
Map<String, Employee> staffMap = ...;
Set<String> terminatedIDs = ...;
```
直接建立一个键集，并删除终止聘用关系的所有员工的ID。
```
staffMap.keySet().removeAll(terminatedIDs);
```
由于键集是映射的一个视图，所以键和相关联的员工名会自动从映射中删除。
通过使用一个子范围视图，可以把批操作限制的子列表和子集上。例如，假设希望把一个列表的前10
个元素增加到另一个容器，可以建立一个子列表选出前10个元素
```
relocated.addAll(staff.subList(0, 10));
```
这个子范围还可以完成更改操作
```
staff.subList(0, 10).clear();
```

### 集合与数组的转换

由于Java平台API的大部分内容都是在集合框架创建之前设计的，所以，有时候需要在传统的数组和比较
现代的集合之间进行转换。

如果需要把一个数组转换为集合，Arrays.asList包装器可以达到这个目的。例如
```
String[] values = ...;
HashSet<String> staff = new HashSet<>(Arrays.asList(values));
```

从集合得到数组会更困难一些。当然，可以使用toArray方法
```
Object[] values = staff.toArray();
```
不过，这样做的结果是一个对象数组。尽管你知道集合中包含一个特定类型的对象，但不能使用强制类型转换
```
String[] values = (String[]) staff.toArray();  // Error!
```

toArray方法返回的数组是一个Object[]数组，不能改变它的类型。实际上，必须使用toArray方法的一个
变体形式，提供一个所需类型且长度为0的数组。这样一来，返回的数组就会创建为相同的数组类型

```
String[] values = staff.toArray(new String[0]);
```

如果愿意，可以构造一个指定大小的数组
```
staff.toArray(new String[staff.size()]);
```
这种情况下，不会创建新数组。

### 编写自己的算法

如果编写自己的算法（实际上，是以集合作为参数的任何方法），应该尽可能地使用接口，
而不是使用具体的实现。例如，假设想用一组菜单项填充JMenu。传统上，这种方法可能会
按照下列方式实现
```
void fillMenu(JMenu menu, ArrayList<JMenuItem> items)
{
    for (JMenuItem item : items)
        menu.add(item);
}
```
但是，这样会限制方法的调用程序，即调用程序必须在ArrayList中提供选项。如果这些选项
需要在另一个容器中，首先必须对它们重新包装，因此，最好接受一个更加通用的集合。

什么是完成这些工作的最通用的集合接口？在这里，只需要访问所有的元素，这是Collection接口
的基本功能。下面代码说明了如何重新编写fillMenu方法使之接受任意类型的集合。
```
void fillMenu(JMenu menu, Collection<JMenuItem> items)
{
    for (JMenu item : items)
        menu.add(item);
}
```

现在，任何人都可以用ArrayList或LinkedList，甚至用Arrays.asList包装器包装的数组
调用这个方法。

既然将集合接口作为方法参数是个很好的想法，为什么Java类库不更多地这样做呢？例如，
JComboxBox有两个构造器
```
JComboxBox(Object[] items)
JComboxBox(Vector<?> items)
```
之所以没有这样做，原因很简单，时间问题，Swing类库是在集合类库之前创建的。

如果编写了一个返回集合的方法，可能还想要一个返回接口，而忽视返回类的方法，因为
这样做可以在日后改变想法，并用另一个集合重新实现这个方法。
例如，编写一个返回所有菜单项的方法getAllItems
```
List<JMenuItme> getAllItems(JMenu menu)
{
    List<JMenuItem> items = new ArrayList<>();
    for (int i = 0; i < menu.getItemCount(); i++)
        items.add(menu.getItem(i));
    
    return items;
}
```

日后，可以做出这样的决定，不复制所有的菜单项，而仅仅提供这些菜单项的视图。
要做到这一点，只需要返回AbstractList的匿名类
```
List<JMenuItem> getAllItems(final JMenu menu)
{
    return new AbstractList<>()
        {   
            public JMenuItem get(int i)
            {
                return menu.getItem(i);
            }
            public int size()
            {
                return menu.getItemCount();
            }
        }
}
```
当然，这是一项高级技术。如果使用它，就应该将它支持的那些"可选"操作准确地记录在文档中。
在这种情况下，必须提醒调用者返回的对象是一个不可修改的列表。



















