
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
























