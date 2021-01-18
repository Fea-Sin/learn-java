
## 接口示例

我们将给出一些接口示例，可以从中了解接口的实际使用

### 接口与回调

回调（callback）是一种常见的程序设计模式。在这种模式中，可以指出某个特定事件发生时应该采取的
动作。例如，可以指出在按下鼠标或选择某个菜单时应该采取什么行动。

在java.swing包中有一个Timer类，可以使用它在到达给定的时间间隔时发出通告。例如，假如程序中有
一个时钟，就可以请求每秒获得一个通告，以便更新时钟的表盘。在构造定时器时，需要设置一个时间间隔，
并告之定时器，当到达时间间隔时需要做什么操作。

如果告之定时器做什么呢？在很多程序设计语言中，可以提供一个函数名，定时器周期性地调用它。但是，
在java标准类库中的类采用的是面向对象方法。它将某个类的对象传递给定时器，然后，定时器调用这个
对象的方法。由于对象可以携带一些附加的信息，所以传递一个对象比传递一个函数要灵活得多。

当然，定时器需要知道调用哪一个接口，并要求传递的对象所属的类实现了java.awt.event包的
ActionListener接口。
```
public interface ActionListener
{
    void actionPerformed(ActionEvent event);
}
```
当到达指定的时间间隔时，定时器就调用actionPerformed方法。

假设希望每隔10秒种打印一条信息"At the tone,the time is ..."，然后响一声，就应该定义
一个实现ActionListener接口的类，然后将需要执行的语句放在actionPerformed方法中。
```
class TimePrinter implements ActionListener
{
    public void actionPerformed(ActionEvent event)
    {
        System.out.println("At the tone, the time is " + new Date());
        Toolkit.getDefaultToolkit().beep();
    }
}
```
需要注意actionPerformed方法的ActionEvent参数，这个参数提供了事件的相关信息。
接下来，构造这个类的一个对象，并将它传递给Timer构造器
```
ActionListener listener = new TimePrinter();
Timer t = new Timer(10000, listener);
```
Timer构造器的第一个参数是发出通告的时间间隔，它的单位是毫秒。这里希望每个10秒钟通告一次。第二个
参数是监听对象。
最后，启动定时器`t.start()`。


### Comparator 接口

我们已经了解了如何对一个对象数组排序，前提是这些对象是实现了Comparable接口的类的实例。例如，可以
对一个字符串数组排序，因为String类实现了Comparable<String>，而且String.compareTo方法
可以按字典顺序比较字符串。
现在假设我们希望按长度递增的顺序顺序对字符串进行排序，而不是按字典顺序进行排序。肯定不能让String
类用两种不同的方式是想compareTo方法，更何况，String类也不应由我们来修改。

要处理这种情况，Arrays.sort方法有第二个版本，有一个数组和一个比较器（comparator）作为参数，
比较器是实现了Comparator接口的类的实例。

```
public interface Comparator<T>
{
    int compare(T first, T second);
}
```
要按长度比较字符串，可以如下定义一个实现Comparator<String>接口的类：
```
class lengthComparator implements Comparator<String>
{
    public int compare(String first, String second)
    {
        return first.length() - second.length();
    }
}
```
具体完成比较时，需要建立一个实例
```
Comparator<String> comp = new LengthComparator();
if (comp.compare(words[i], words[j]) > 0) ...
```
将这个调用与words[i].compareTo(words[j])做比较。这个compare方法要在比较器对象上调用，
而不是在字符串本身上调用。

尽管LengthComparator对象没有状态，不过还是需要建立这个对象的一个实例。我们需要这个实例
来调用compare方法，它不是一个静态方法。

要对一个数组排序，需要为Arrays.sort方法传入一个LengthComparator对象
```
String[] friends = {"Peter", "Paul", "Mary"};
Arrays.sort(friends, new LengthComparator());
```

### 对象克隆

本节我们会讨论Cloneable接口，这个接口指示一个类提供了一个安全的的clone方法。由于克隆并不太
常见，而且有关的细节技术性很强。

要了解克隆的具体含义，先来回忆一个包含对象引用的变量建立副本时会发生什么。原变量和副本都是同
一个对象的引用。这说明，任何一个变量改变都会影响另一个变量。
如果希望copy是一个新对象，它的初始状态与original相同，但是之后它们各自会有自己不同的状态，
这种情况下就可以使用clone方法。
```
Employee copy = original.clone();
copy.raiseSalary(10); // OK original unchanged
```
不过并没有这么简单。clone方法是Object的一个protected方法，这说明你的代码不能直接调用
这个方法。只有Employee类可以克隆Employee对象。这个限制是有原因的。想想看Object类如何实现clone。
它对于这个对象一无所知，所以只能逐个域地进行拷贝。如果对象中的所有数据域都是数值或其他基本类型，
拷贝这些域没有任何问题。但是如果对象包含子对象的引用，拷贝域就会得到相同子对象的另一个引用，
这样一来，原对象和克隆的对象仍然会共享一些信息。

浅拷贝会有什么影响吗？这要看具体情况。如果原对象和浅克隆对象共享的子对象是不可变的，那么这种共享
是安全的。如果子对象属于一个不可变的类，如String，就是这种情况。或者在对象的生命期中，子对象
一直包含不变的常量，没有更改器方法会改变它，也没有方法会生成它的引用，这种情况下同样是安全的。

不过，通常子对象是可变的，必须重新定义clone方法来建立一个深拷贝，同时克隆所有子对象。
对于每一个类，需要确定：

1）默认的clone方法是否满足要求

2）是否可以在可变的子对象上调用clone来修补默认的clone方法

3）是否不该使用clone

类必须

1）实现Cloneable接口

2）重新定义clone方法，并指定public访问修饰符

Cloneable接口的出现与接口的正常使用并没有关系。具体来说，它没有指定clone方法，这个方法是从
Object类继承的。这个接口只是作为一个标记，指示类设计者了解克隆过程。
Cloneable接口是Java提供的一组标记接口（tagging interface）之一，标记接口不包含
任何方法，它唯一的作用就是允许在类型查询中使用`instanceof`
```
if (obj instanceof Cloneable) ...
```

即使clone的默认（浅拷贝）实现能够满足要求，还是需要实现Cloneable接口，将clone重新
定义为public，在调用super.clone()。
```
class Employee implements Cloneable
{
   ...
    public Employee clone() throws CloneNotSupportedException
    {
        // call Object.clone()
        Employee cloned = (Employee) super.clone();
        
        // clone mutable fields
        cloned.hireDay = (Date) hireDay.clone();
        
        return cloned;
    }
    ...
}
```

最好还是保留throws说明符。这样就允许子类在不支持克隆时选择抛出一个CloneNotSupportedException。

必须当心子类的克隆。例如，一旦为Employee类定义了clone方法，任何人都可以用它来克隆Manager对象。
Employee克隆方法能完成工作吗？这取决于Manager类的域。在这里是没有问题的，因为bonus域是基本类型。
但是Manager可能会有需要深拷贝或不可克隆的域。

要不要在自己的类中实现clone呢？如果你的客户需要建立深拷贝，可能就需要实现这个方法。
克隆没有你想象中那么常用。标准库中只有不到5%的类实现了clone。

所有数组类型都有一个public的clone方法，而不是protected。可以用这个方法建立一个新数组，
包含原数组所有元素的副本。
```
int[] luckyNumbers = {2, 3, 5, 7, 11, 13};
int[] cloned = luckyNumbers.clone();
cloned[5] = 12; // doesn't change luckyNumbers[5]
```


