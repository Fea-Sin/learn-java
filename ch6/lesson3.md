
## 内部类

内部类（inner class）是定义在另一个类中的类。为什么需要使用内部类呢？其主要原因有一下三点

- 内部类方法可以访问该类定义所有的作用域中的数据，包括私有的数据
- 内部类可以对同一个包中的其他类隐藏起来
- 当想要定义一个回调函数且不想编写大量代码时，使用匿名（anonymous）内部类比较便捷

我们将这个比较复杂的内容分几部分介绍

- 给出一个简单的内部类，它将访问外围类的实例域
- 给出内部类的特殊语法规则
- 领略一下内部类的内部，探讨一下如何将其转换成常规类
- 讨论局部内部类，它可以访问外围作用域中的局部变量
- 介绍匿名内部类，说明在Java有lambda表达式之前用于实现回调的基本方法
- 介绍如何将静态内部类嵌套在辅助类中

C++有嵌套类，一个被嵌套的类包含在外围类的作用域内。嵌套是一种类之前的关系，而不是对象之间的关系。


### 使用内部类访问对象状态

内部类的语法比较复杂。鉴于此情况，我们选择一个简单但不太实用的例子说明内部类的使用方法。下面将
进一步分析TimerTest示例，并抽象出一个TalkingClock类。构造一个语音时间需要提供两个参数，
发布通告的间隔和开关铃声的标志。
```
public class TalkingClock
{
    private int interval;
    private boolean beep;
    
    public TalkingClock(int interval, boolean beep) 
    {
        this.interval = interval;
        this.beep = beep;
    }
    public void start()
    {
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(interval, listener);
        t.start()
    }
    
    public class TimePrinter implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            System.out.println("At the tone, the time is " + new Date());
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
}
```

令人惊讶的事情发生了。TimePrinter类没有实例域或这名为beep的变量，取而代之的是beep引用了创建
TimePrinter的TalkingClock对象的域。这是一种创新的想法。从传统意义上讲，一个方法可以引用调用
这个方法的对象数据域。内部类既可以访问自身的数据域，也可以访问创建它的外围类对象的数据域。

为了能够运行这个程序，内部类的对象总有一个隐式引用，它指向了创建它的外部类对象。
这个引用在内部类的定义中是不可见的。然而，为了说明这个概念，我们将外围类对象的引用称为outer。

外围类的引用在构造器中设置。编译器修改了所有的内部类的构造器，添加一个外围类引用的参数。
因为TimerPrinter类没有定义构造器，所以编译器为这个类生成了一个默认的构造器
```
public TimePrinter(TalkingClock clock)
{
    outer = clock
}
```
请注意一下，outer不是Java的关键字。我们只是用它说明内部类的机制。

当在start方法中创建了TimePrinter对象后，编译器就会将this引用传递给当前的语音时钟的构造器
```
ActionListener listener = new TimePrinter(this)
```

如果有一个TimePrinter类是一个常规类，它就需要通过TalkingClock类的公有方法访问beep标志，
而使用内部类可以给予改进，即不必提供用于访问其他类的访问器。

TimePrinter类声明为私有的。这样以来，只有TalkingClock的方法才能够构造TimePrinter对象。
只有内部类可以是私有类，而常规类只有可以具有包可见性，或公有可见性。

### 内部类的特殊语法规则

在上面，已经讲述了内部类有一个外围类的引用outer。事实上，使用外围类引用的正规语法还要复杂一些
```
OuterClass.this
```
表示外围类引用。例如，可以像下面这样编写TimePrinter内部类的actionPerformed方法
```
public void actionPerformed(ActionEvent event)
{
    ...
    if(TalkingClock.this.beep) Toolkit.getDefaultToolkit().beep();
}
```
反过来，可以采用下列语法更加明确地编写内部对象的构造器
```
outerObject.new InnerClass(construction parameters)
```
例如
```
ActionListener listener = this.new TimePrinter();
```

可以通过显式地命名将外围类引用设置为其他的对象。例如，如果TimePrinter是一个公有内部类，对于
任意的语音时钟都可以构造一个TimePrinter
```
TalkingClock jabberer = new TalkingClock(1000, true);
TalkingClock.TimePrinter listener = jaberer.new TimePrinter();
```

需要注意，在外围类的作用域之外，可以这样引用内部类
```
OuterClass.InnerClass
```

### 内部类是否有用、必要和安全

当在Java 1.1的Java语言中增加内部类时，很多程序员认为这是一项很主要的新特性，但这却违背了
Java要比C++更加简单的设计理念。内部类的语法很复杂。它与访问控制和安全性等其他的语言特性
没有明显的关联。

由于增加了一些看似优美有趣，实属没必要的特性，似乎Java也开始走上了许多语言饱受折磨的毁灭
性道路上。

内部类是一种编译器现象，与虚拟机无关。编译器将会把内部类翻译成用$（美元符号）分隔外部类名
与内部类名的常规类文件，而虚拟机则对此一无所知。

### 局部内部类

如果仔细地阅读一下TalkingClock示例的代码就会发现，TimePrinter这个类名字只在start
方法中创建这个类型的对象使用了一次。
当遇到这类情况时，可以在一个方法中定义局部类
```
public void start()
{
    class TimePrinter implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            System.out.println("At the tone, the time is " + new Date());
            if (beep) Toolkit.getDefaultkit().beep();
        }
    }
    
    ActionListener listener = new TimePrinter();
    Timer t = new Timer(interval, listener);
    t.start();
}
```
局部类不能用public或private访问说明符进行声明。它的作用域被限定在声明这个局部类的块中。

局部类有一个优势，即对外部世界可以完全地隐藏起来。即使TalkingClock类中的其他代码也不能
访问它。除start方法之外，没有任何方法直到TimePrinter类的存在。


### 从外部方法访问变量

与其他内部类相比较，局部类还有一个优点。它们不仅能够访问包含它们的外部类，还可以访问局部变量。
不过，那些局部变量必须事实上为final。这说明，它们一旦赋值就绝不会改变。
下面是一个典型的示例
```
public vid start(int interval, boolean beep)
{
    class TimePrinter implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            System.out.println("At the tone, the time is " + new Date());
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
    
    ActionListener listener = new TimePrinter();
    Timer t = new Timer(interval, listener)
    t.start();
}
```
请注意，TalkingClock类不再需要存储实例变量beep了，它只是引用stat方法中的beep参数变量。

为了能够清楚地看到内部的问题，让我们仔细地考察一下控制流程

1）调用start方法
2）调用内部类TimePrinter的构造器，以便初始化对象变量listener
3）将listener引用传递给Timer构造器，定时器开始计时，start方法结束。此时，start方法
的beep参数变量不复存在。
4）然后，actionPerformed方法执行if(beep)...

为了能够让actionPerformed方法工作，TimePrinter类在beep域释放之前将beep域用start
方法的局部变量进行备份。

编译器必须检测对局部变量的访问，为每一个变量建立相应的数据域，并将局部变量拷贝到构造器中，
以便将这些数据域初始化为局部变量的副本。

从程序员的角度看，局部变量的访问非常容易。它减少了需要显式编写的实例域，从而使得内部类更加
简单。

### 匿名内部类

将局部内部类的使用再深入一步。假设只要创建这个类的一个对象，就不必命名了。这种类被称为
匿名内部类（anonymous inner class）。
```
public void start(int interval, boolean beep)
{
    ActionListener listener = new ActionListener()
    {
        public void actionPerformed(ActionEvent event)
        {
            System.out.println("At the tone, the time is " + new Date());
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
    Timer t = new Timer(interval, listener);
    t.start();
}
```
这种语法确实有些难以理解。它的含义是，创建一个实现ActionListener接口的类的新对象，
需要实现的方法actionPerformed定义在括号{}内。

通常的语法格式为
```
new SuperType(construction parameters)
    {
        inner class methods and data
    }
```
其中，SuperType可以是ActionListener这样的接口，于是内部类就要是实现这个接口。SuperType
也可以是一个类，于是内部类就要扩展它。

由于构造器的名字必须域类名相同，而匿名类没有类名，所以，匿名类不能有构造器。取而代之的是，
将构造器参数传递给超类（superclass）构造器。尤其是在内部类实现接口的时候，不能有任何构造参数。

看看构造一个类的新对象与构造一个扩展了那个类的匿名内部类的对象之间有什么差别。
```
Person queen = new Person(""Mary);
Person count = new Person("Dracula") {...}
```

### 静态内部类

有时候，使用内部类只是为了把一个类隐藏在另一个类的内部，并不需要内部类引用外围类对象。
为此，可以将内部类声明为static，以便取消产生的引用。

下面是一个使用静态内部类的典型例子。考虑一些计算数组中最小值和最大值的问题。
当然，可以编写两个方法，一个方法用于计算最小值，另一个方法用于计算最大值。在调用这两个方法的时候，
数组被遍历两次。如果只遍历数组一次，并能够同时计算除最小值和最大值，那就可以大大地提高效率了。

在大型项目中，定义一个类Pair，Pair是一个十分大众化的名字，类的名字其他程序员也很可能使用这个名字。这样就会产生冲突，解决这个问题
的办法是将Pair定义为使用此类的ArrayAlg的内部公有类。通过`ArrayAlg.Pair`访问它。

与前面例子中使用的内部类不同，在Pair对象中不需要引用任何其他的对象，为此，可以将这个内部类
声明为static。当然，只有内部类可以声明为static。静态内部类的对象除了没有对生成它的外围类对象
的引用特权外，与其他所有内部类完全一样。
```
class ArrayAlg
{
    public static class Pair
    {
        private double first;
        private double second;
        
        public Pair(double f, double s)
        {
            first = f;
            second = s;
        }
        
        public double getFirst()
        {
            return first;
        }
        public double getSecond()
        {
            return second;
        }
    }
    
    public static Pair minmax(double[] values)
    {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (double v : values)
        {
            if (min > v) min = v;
            if (max < v) max = v;
        }
        return new Pair(min, max)
    }
}
```

使用
```
public class StaticInnerClassTest
{
    double[] d = new double[20];
    for (int i = 0; i < d.length; i++)
        d[i] = 100 * Math.random();
    
    ArrayAlg.Pair p = ArrayAlg.minmax(d);
    System.out.println("min= " + p.getFirst());
    System.out.println("max= " + p.getSecond());
}
```



























