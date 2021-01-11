

## 泛型数组列表

在许多程序设计语言中，特别是在C++语言中，必须在编译时就确定整个数组的大小。程序员对此十分反感，因为
这样做将迫使程序员做出一些不情愿的折中。

在Java中，情况就好多了，它允许在运行时确定数组的大小。
```
int actualSzie = ...;
Employee[] staff = new Employee[actualSize];
```
当然，这并没有完全解决运行时动态更改数组的问题。一旦确定了数组的大小，改变它就不太容易了。
在Java中，解决这个问题最简单的方法是使用Java中另外一个被称为ArrayList的类。它使用起来优点像数组，
但在添加或删除元素时，具有自动调节数组容量的功能，而不需为此编写任何代码。

ArrayList是一个采用类型参数的泛型类。
```
ArrayList<Employee> staff = new ArrayList<>();
```

分配数组列表与为新数组分配空间有所不同
```
new ArrayList<>(100); // capacity is 100

new Employee[100]; // size is 100
```
数组列表的容量与数组的大小有一个非常重要的区别。如果为数组分配100个元素的存储空间，数组就是100个空位置
可以使用。而容量为100个元素的数组列表是用有保存100个元素的潜力，但是最初甚至完成初始化构造之后，
数组列表根本不含任何元素。

size 方法将返回数组列表中包含的实际元素数目`staff.size()`，它等价于数组a的a.length

对数组实施插入和删除元素的操作其效率比较低。对于小型数组来说，这一点不必担心，但是如果数组存储的
元素比较多，又经常需要在中间位置插入、删除元素，就应该使用链表了。

使用for each循环遍历数组列表
```
for(Employee e : staff)
    do something with e
```

这个循环和下列代码具有相同的效果
```
for (int i = 0; i < staff.size(); i++)
{
    Employee e = staff.get(i);
    do something with e
}
```

## 对象包装器与自动装箱

有时，需要将int这样的基本类型转换为对象。所有的基本类型都有一个与之对应的类。例如，Integer类对应
基本类型int。通常，这些类称为包装器。这些对象包转器类拥有很明显的名字：Integer、Long、Float、
Double、Short、Byte、Character、Void和Boolean（前6个类派生自公共的超类Number）。对象
包装器类是不可变的，即一旦构造了包装器，就不允许更改包装在其中的值。同时，对象包转器类还是final，
因此不能定义它们的子类。

假设想定义一个整型数组列表。而尖括号中的类型参数不允许是基本类型，也就是说，不允许写成ArrayList<int>。
这里就用到了Integer对象包转器类，
```
ArrayList<Integer> list = new ArrayList<>();
```

警告，由于每个值分别包装在对象中，所以ArrayList<Integer>的效率远远低于int[]数组。因此，应该
用它构造小型集合，其原因是此时操作的方便性要比执行效率更加重要。

幸运的是，有一个很有用的特性，从而更加方便于添加int类型的元素到ArrayList<Integer>中，下面这个调用
```
list.add(3)
```
将自动的变成
```
list.add(Integer.valueOf(3));
```
这种变换被称为自动装箱（autoboxing）。装箱（boxing）这个词源自于C#。
相反地，当将一个Integer对象赋给一个int值时，就会自动地拆箱
```
int n = list.get(i);

// 翻译成

int n = list.get(i).intValue();
```

大多数情况下，容易有一种假象，即基本类型于它们的对象包装器是一样的，只是它们的相等性不同。
大家知道，==运算也可以应用于对象包装器对象，只不过检测的是对象是否指向同一个存储区域，
因此，下面的比较通常不会成立
```
Integer a = 1000;
Integer b = 1000;
if (a == b) ...;
```
解决这个问题的办法是在两个包装器对象比较时调用equals方法。

装箱和拆箱是编译器认可的，而不是虚拟机。编译器在生成类的字节码时，插入必要的方法调用。
虚拟机只是执行这些字节码。

### 参数数量可变的方法

在Java SE 5.0以前的版本中，每个Java方法都有固定数量的参数。然而，现在版本提供了可以用可变的
参数数量调用的方法（有时称为"变参"方法）。

printf方法就是这样的方法，printf方法是这样定义的
```
public class PrintStream
{
    public Printstream printf(String fmt, Object... args)
    {
        return format(fmt, args);
    }
}
```
这里的省略号...是Java代码的一部分，它表明这个方法可以接收任意数量的对象。
Object...参数类型与Object[]完全一样。编译器需要对printf的每次调用进行转换，以便将参数绑定到
数组上，并在必要的时候进行自动装箱：
```
System.out.printf("%d %s", new Object[] {new Integer(n), "widgets"});
```

用户自己也可以定义可变参数的方法，并将参数指定为任意类型，甚至是基本类型。
下面是一个简单示例，其功能为计算若干个数值的最大值
```
public static double max(double... values)
{
    double largest = Double.NEGATIVE_INFINITY;
    for (double v : values) if (v > largest) largest = v;
    return largest;
}
```

```
double m = max(3.1, 40.4, -5);
```

#### 枚举类

在第3章已经看到如何定义枚举类型，下面是一个典型的例子
```
public enum Size { SMALL, MEDIUM, LARGE, EXTRA_LARGE }
```
在比较两个枚举类型的值时，可以直接使用`==`。

所有的枚举类型都是Enum类的子类，它们继承了这个类的许多方法。其中之一是toString，这个方法
能够返回枚举常量名。例如，Size.SMALL.toString()将返回字符串"SMALL"。
























