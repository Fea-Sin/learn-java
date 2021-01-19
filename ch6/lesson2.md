
## lambda 表达式

现在可以来学习lambda表达式，这是这些年来Java语言最让人激动的一个变化。你会了解如何使用lambda
表达式采用一种简洁的语法定义代码块，以及如何编写处理lambda表达式的代码。

### 为什么引入lambda表达式

lambda表达式是一个可以传递的代码块，可以在以后执行一次或多次。

考虑如何用一个定制比较器完成排序，如果想按长度而不是默认的字典顺序对字符串排序，可以向sort方法
传入一个Comoarator对象
```
class LengthComparator implements Comparator<String>
{
    public int compare(String first, String second)
    {
        return first.length() - second.length();
    }
}

Arrays.sort(strings, new LengthComparator());
```
compare方法不是立即调用。实际上，在数组完成排序之前，sort方法会一直调用compare方法，只要
元素的顺序不正确就会重新排序元素。将比较元素所需的代码段放在sort方法中，这个代码将与其余的排序
逻辑继承。

上述例子，将一个代码块传递到某个对象（一个定时器，或者一个sort方法）。这个代码块会在将来某个
时间调用。

到目前为止，在Java中传递一个代码段并不容易，不能直接传递代码段。Java是一种面向对象语言，
所以必须构造一个对象，这个对象的类需要有一个方法能包含所需的代码。
在其他语言中，可以直接处理代码块。Java设计者很长时间以来一直拒绝增加这个特性。毕竟，Java的
强大之处就在于其简单性和一致性。如果只要一个特性能够让代码稍简洁一些，就把这个特性增加到语言
中，这个语言很快就会变得一团糟，无法管理。不过，在另外那些语言中，并不只是创建线程或注册按钮
点击事件更容易，它们的大部分API都更简单、更一致而且更强大。在Java中，也可以编写类似的API
利用类对象实现特定的功能，不过这种API使用很不方便。

就现在来说，问题已经不是是否增加Java来支持函数式编程，而是要如何做到这一点。设计者们做了
多年尝试，终于找到一种适合Java的设计，lambda表达式。

### lambda表达式的语法

再来考虑上一节讨论的拍讯例子。我们传入代码来检车一个字符串是否比另一个字符串短。
```
(String first, String second)
    -> first.length() - second.length()
```
这就是你看到的第一个lambda表达式。lambda表达式就是一个代码块，以及必须传入代码的变量规范。

你已经见过Java中的一种lambda表达式形式，参数、箭头（->）以及一个表达式。如果代码要完成的计算无法
放在一个表达式中，就可以像写方法一样，把这些代码放在{}中，并包含显式的return语句。
```
(String first, String second) ->
    {
        if (first.length() < second.length()) return -1;
        else if (first.length() > second.length()) return 1;
        else return 0;
    }
```
即使lambda表达式没有参数，仍然要提供空括号，就像无参数方法一样
```
() -> { for (int i = 100; i >= 0; i--) System.out.println(i); }
```
如果可以推导出一个lambda表达式的参数类型，则可以忽略其类型，例如
```
Comparator<String> comp = (first, second) -> first.length() - second.length();
```
因为这个lambda表达式将赋给一个字符串比较器，编译器可以推导出first和second必然是字符串

如果方法只有一个参数，而且这个参数的类型可以推导得出，那么甚至还可以省略小括号
```
ActionListener listener = event ->
    System.out.println("this is " + new Date());
```

无需指定lambda表达式的返回类型。lambda表达式的返回类型总是会由上下文推导得出。

如果一个lambda表达式只在某些分之返回一个值，而在另一些分支不返回值，这是不合法的。例如
```
(int x) -> { if (x >= 0) return 1; }  // 不合法
```

### 函数式接口

前面已经讨论过，Java中已经有很多封装代码块的接口，如ActionListener或Comparator。
lambda表达式与这些接口是兼容的。

对于只有一个抽象方法的接口，需要这种接口的对象时，就可以提供一个lambda表达式。这种接口
称为函数式接口（functional interface）。

为了展示如何转换为函数式接口，下面考虑Arrays.sort方法。它的第二个参数需要一个Comparator实例，
Comparator就是只有**一个方法**的接口，所以提供一个lambda表达式
```
Arrays.sort(words,
    (first, second) -> first.length() - second.length());
```
在底层，Arrays.sort方法会接收实现了Comparator<String>的某个类的对象。在这个对象
上调用compare方法会执行这个lambda表达式。

实际上，在Java中，对lambda表达式所能做的也只是能转换为函数式接口。在其他支持函数式字面量
的程序设计语言中，可以声明函数类型（如（String, String）-> int）、声明这些类型的变量，
还可以使用变量保存函数表达式。不过，Java设计者还是决定保持我们熟悉的接口概念，没有为
Java语言增加函数类型。

甚至不能把lambda表达式赋给类型为Object的变量，Object不是一个函数式接口。

java.util.function包中有一个尤其有用的接口Predicate
```
public interface Predicate<T>
{
    boolean test(T t);
}
```

ArrayList类有一个removeIf方法，它的参数就是一个Predicate。这个接口专门用来传递
lambda表达式。例如，下面的语句将从一个数组列表删除所有的null值
```
list.removeIf(e -> e == null);
```

### 方法引用

有时，可能已经有现成的方法可以完成你想要传递到其他代码的某个动作。例如，假设你希望只要
出现一个定时器事件句打印这个事件对象。当然，为此你可以调用
```
Timer t = new Timer(1000, event -> System.out.println(event));
```
但是，如果直接把println方法传递给Timer构造器就更好了
```
Timer t = new Timer(1000, System.out::println);
```
表达式System.out::println是一个方法引用（method reference），它等价于lambda表达式
x -> System.out.println(x)。

要用`::`操作符分隔方法名与对象或类名，主要有3种情况

- object::instanceMethod
- Class::staticMethod
- Class::instanceMethod

从前2种情况看，方法引用等价于提供方法参数的lambda表达式。前面已经提到，System.out::println
等价于x -> System.out.println(x)。类似地，Math::pow等价于（x, y）-> Math.pow(x, y)。
对于第3种情况，第1个参数会称为方法的目标。例如，String::compareToIgnoreCase等同于
(x, y) -> x.compareToIgnoreCase(y)。

如果有多个同名的重载方法，编译器就会尝试从上下文中找出你指的那一个方法。

方法引用不能独立存在，总是会转换为函数式接口的实例。

可以在方法引用中使用this参数。例如，this::equals等同于x -> this.equals(x)，使用this
作为目标，会调用给定方法的超类版本。super也是合法的
```
super::instanceMethod
```

### 构造器引用

构造器引用与方法引用很类似，只不过方法名为new。例如，Person::new是Person构造器的引用。
哪一个构造器呢？这取决于上下文。假设你有一个字符串列表。可以把它转换为一个Person对象数组，
为此要在各个字符串上调用构造器
```
ArrayList<String> names = ...;
Stream<Person> stream = names.stream().map(Person::new);
List<Person> people = stream.collect(Collectors.toList());
```

map方法会为各个列表元素调用Person(String)构造器。如果有多个构造器，编译器会选择有
一个String参数的构造器，因为它从上下文推导出这是在对一个字符串调用构造器。

可以用数组类型建立构造器引用。例如，int[]::new是一个构造器引用，它有一个参数：即数组
的长度。这等价于lambda表达式x -> new int[x]。

### 变量作用域

通常，你可能希望能够在lambda表达式中访问外围方法或类中的变量。考虑下面这个例子
```
public static void repeatMessage(String text, int delay)
{
    ActionListener listener = event ->
        {
            System.out.println(text);
            Toolkit.getDefaultToolkit().beep();
        };
    new Timer(delay, listener).start();
}
```
来看这样一个调用
```
repeatMessage("Hello", 1000);
```
现在来看lambda表达式中的变量text。注意这个变量并不是在这个lambda表达式中定义的。实际上，
这是repeatMessage方法的一个参数变量。
如果再想想看，这里好像会有问题，尽管不那么明显。lambda表达式的代码可能会在repeatMessage
调用返回很久后才运行，而那时这个参数变量已经不存在了。如何保留text变量呢？

要了解到底会发生什么，下面来巩固我们对lambda表达式的理解。lambda表达式有3个部分

1）一个代码块

2）参数

3）自由变量的值，这是指非参数而且不在代码中定义的变量。

在我们的例子中，这个lambda表达式有1个自由变量text。表示lambda表达式的数据结构必须存储自由
变量的值，在这里就是字符串"Hello"。我们说它被lambda表达式捕获（captured）。（下面来看
具体的实现细节。例如，可以把一个lambda表达式转换为包含一个方法的对象，这样自由变量的值就会复制到
这个对象的实例变量中）

关于代码块以及自由变量值有一个术语：闭包（closure）。如果有人吹嘘他们的语言有闭包，现在你也可以
自信地说Java也有闭包。在Java中，lambda表达式就是闭包。

可以看到，lambda表达式可以捕获外围作用域中变量的值。在Java中，要确保所捕获的值是明确定义的，
这里有一个重要限制。

1）在lambda表达式中，只能引用值不会改变的变量，例如，下面的做法是不合法的
```
public static void countDown(int start, int delay)
{
    ActionListener listener = event ->
        {
            start--;  // Error: Can't mutate captured variable
            System.out.println(start)
        };
    new Timer(delay, listener).start()
}
```
之所以有这个限制是有原因的。如果在lambda表达式中改变变量，并发执行多个动作时就会不安全。

2）如果在lambda表达式中引用变量，而这个变量肯能在外部改变，这也是不合法的。
```
public static void repeat(String text, int count)
{
    for (int i = 1; i <= count; i++)
    {
        ActionListener listener = event ->
            {
                System.out.println(i + ":" + text); // Error: Cannot refer to changing i
            };
        new Timer(1000, listener).start();
    }
}
```

这里有一条规则，lambda表达式中捕获的变量必须实际上是最终变量（effectively final），实际上
的最终变量是指，这个变量初始化之后句不会再为它赋新值。

lambda表达式与嵌套块有相同的作用域。这里同样适用命名冲突和遮蔽的有关规则。在lambda表达式中
声明与一个局部变量同名的餐素或局部变量是不合法的
```
Path first = Paths.get("/usr/bin");
Comparator<String> comp =
    (first, second) -> first.length() - second.length();
    // Error: Variable first already defined
```
在方法中不能有两个同名的局部变量，因此，lambda表达式中同样也不能有同名的局部变量。


### 处理lambda表达式

到目前为止，你已经了解了如何生成lambda表达式，以及如何把lambda表达式传递到需要一个函数式接口
的方法。下面来看如何编写方法处理lambda表达式

使用lambda表达式的重点是延迟执行（deferred execution）。毕竟，如果想要立即执行代码，完全
可以直接执行，而无需把它包装在一个lambda表达式中。之所以希望以后再执行代码，这里有很多原因

- 在一个单独的线程中运行代码
- 多次运行代码
- 在算法的适当位置运行代码（例如，排序中的比较操作）
- 发生某种情况时执行代码（如：点击了一个按钮，数据到达等）
- 只在必要时才运行代码

下面来看一个简单的例子。假设你想要重复一个动作n次。将这个动作和重复次数传递到一个repeat方法
```
repeat(10, () -> System.out.println("Hello World"));
```
要接受这个lambda表达式，需要选择一个函数式接口。Java API中提供了这个函数式接口 Runnable
```
public static void repeat(int n, Runnable action)
{
    for (int i = 0; i < n; i++) action.run();
}
```

现在让这个例子更复杂一些。我们希望告诉这个动作出现在那一次迭代中。为此，需要选择一个合适的
函数式接口，其中要包含一个方法，这个方法有一个int参数而且返回类型是void。
```
public interface IntConsumer
{
    void accept(int value);
}
```

下面给出repeat方法的改进版本
```
public static void repeat(int n, IntConsumer action)
{
    for (int i = 0; i < n; i++) action.accept(i);
}
```
可以如下调用它
```
repeat(10, i -> System.out.println("Countdown" + (9 - i)))
```

### 再谈 Comparator

Comparator接口包含很多方便的静态方法来创建比较器。这些方法可以用于lambda表达式或方法引用。

静态comparing方法取一个"键提取器"函数，它将类型T映射为一个可比较的类型（如String）。
对要比较的对象应用这个函数，然后对返回的键完成比较。例如，假设有一个Person对象数组，
```
Arrays.sort(people, Comparator.comparing(Person::getName));
```
与手动实现一个Comparator相比，这当然要容易得多。另外，代码也更为清晰。

可以把比较器与thenComparing方法串起来
```
Arrays.sort(people, Comparator.comparing(Person::getLastName)
    .thenComparing(Person::getFirstName));
```
如果两个人的姓相同，就会使用第二个比较器。

可以为comparing和thenComparing方法提取的键指定一个比较器。例如，可以根据人名
长度完成排序
```
Arrays.sort(people, Comparator.comparing(Person::getName,
    (s, t) -> Integer.compare(s.length(), t.length()) ));
```













