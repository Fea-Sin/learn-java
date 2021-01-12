

## 反射

能够分析类能力的程序称为反射（reflective）。反射机制的功能极其强大，在下面可以看到，
反射机制可以用来：

- 在运行时分析类的能力
- 在运行时查看对象，例如，编写一个toString方法供所有的类使用
- 实现通用的数组操作代码
- 利用Method对象，这个对象很像C++中的函数指针

反射是一种强大且复杂的机制。使用它的主要人员是工具构造者，而不是应用程序员。

### Class 类

在程序运行期间，Java运行时系统始终为所有的对象维护一个被称为运行时的类型标识。
这个信息跟踪着每个对象所属的类。虚拟机利用运行时类型信息选择相应的方法执行。然而，
可以通过专门的Java类访问这些信息。保存这些信息的类被称为Class，这个名字很容易让人混淆。
Object类中的getClass()方法将返回一个Class类型的实例。
```
Employee e;
Class cl = e.getClass();
```

如果类在一个包里，包的名字也作为类名的一部分：
```
Random generator = new Random();
Class cl = generator.getClass();
String name = cl.getName();  // name is "java.util.Random"
```

在启动时，包含main方法的类被加载。它会加载所有需要的类。这些被加载的类又要加载它们需要的类，
一次类推，对于一个大型的应用程序来说，这将会消耗很多的时间。

获取Class类对象的另一种方法，如果T是任意的Java类型（或void关键字），T.class将代表
匹配的类对象
```
Class cl = Random.class;
```

Class类实际上是一个泛型类，例如，Employee.class的类型是Class<Employee>。

### 捕获异常

当程序运行过程中发生错误时，就会抛出异常，抛出异常比终止程序要灵活得多，这是因为可以提供
一个捕获异常的处理去（handler）对异常情况进行处理。
如果没有提供处理器，程序就会终止，并在控制台上打印一条信息，其中给出了异常的类型。
例如，偶然使用了null引用或者数组越界等。

异常有两种类型，未检查异常和已检查异常。对于以检查异常，编译器将会检查是否提供了处理器。
然而，很多常见的异常，例如，访问null引用，都属于未检查异常。编译器不会查看是否为这些错误
提供了处理器。

并不是所有的错误都是可以避免的。将可能抛出已检查异常的一个或多个方法调用代码放在try块中，
然后catch子句中提供处理代码
```
try
{
    String name = ...;  // get class name
    Class cl = Class.forName(name); // might throw exception
    do something with cl
}
catch (Exception e)
{
    e.printStackTrace();
}
```
如果类名不存在，则将跳过try块中的剩余代码，程序直接进入catch子句，这里利用Throwable类的
printStackTrace方法打印出栈的轨迹。Throwable是Exception类的超类。
如果try块中没有抛出任何异常，那么会跳过catch子句的处理代码。

如果调用了一个抛出已检查异常的方法，而又没有提供处理器，编译器就会给出错误报告。

### 利用反射分析类的能力

下面简要地介绍一下反射机制最重要的内容--检查类的结构

在java.lang.reflect包中又三个类Field、Method和Contructor分别用于描述类的域、方法和
构造器。这三个类都有一个getName的方法，用来返回项目的名称。

Class类中的getFields、getMethods和getConstructors方法将分别返回类提供的public域、方法
和构造器数组，其中包括超类的公有成员。Class类的getDeclareFields、getDeclareMethods和
getDeclaredConstructors方法将分别返回类中声明的全部域、方法和构造器，其中包括私有和受保护
成员，但不包括超类的成员。


### 在运行时使用反射分析对象

从前面一节中，已经知道如何查看任意对象的数据域名称和类型。

- 获取对应的Class对象
- 通过Class对象调用getDeclaredFields

本节将进一步查看数据域的实际内容。查看对象域的关键方法是Field类中的get方法。如果f是一个Field
类型的对象（例如通过getDeclaredFields得到的对象），obj是某个包含f域的类的对象，f.get(obj)
将返回一个对象，其值为obj域的当前值。
```
Employee harry = new Employee("Harry Hacker", 35000, 10, 1, 1989);
Class cl = harry.getClass();
Field f = cl.getDeclaredField("name");
Object v = f.get(harry); // the value of the name field of the harry object, the String objcect "Harry Hacker"
```

### 使用反射编写泛型数组代码

java.lang.reflect包中的Array类允许动态地创建数组。例如，将这个特性应用到Array类中的copyOf
方法实现中
```
Employee[] a = new Employee[100];
a = Arrays.copyOf(a, 2*a.length);
```

如何编写一个通用方法呢
```
public static Object goodCopyOf(Object a, int newLength)
{
    Class cl = a.getClass();
    if (!cl.isArray()) return null;
    Class componentType = cl.getComponentType();
    int length = Array.getLength(a);
    Object newArray = Array.newInstance(componentType, newLength);
    System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
    return newArray;
}
```

这个CopyOf方法可以用来扩展任意类型的数组，而不仅是对象数组
```
int[] a = {1, 2, 3, 4, 5};
a = (int[]) goodCopyOf(a, 10);
```

### 调用任意方法

在C和C++中，可以从函数指针执行任意函数。从表面上看，Java没有提供方法指针，
即将一个方法存储地址传给另外一个方法，以便第二个方法能够随后调用它。事实上，
Java的设计者曾说过，方法指针是很危险的，并且常常会带来隐患。他们认为Java提供的
接口（interface）是一种更好的解决方案。然而，反射机制允许你调用任意方法。

在Method类中有一个invoke方法，它允许调用包装在当前Method对象中的方法。
invoke方法的签名是：
```
Object invoke(Object obj, Object... args)
```
第一个参数是隐式参数，其余的对象提供了显式参数，如果没有显式参数就传递一个null.

例如，下面说明了如何获得Employee类的getName方法和raiseSalary方法的方法指针。
```
Method m1 = Employee.class.getMethod("getName");
Method m2 = Employee.class.getMethod("raiseSalary", double.class);

String n = (String) m1.invoke(harry);
```

可以使用method对象实现C（或C#中的委派）语言中函数指针的所有操作。同C一样，这种程序设计
风格并不太简便，出错的可能性也比较大。
不仅如此，使用反射获得方法指针的代码要比直接调用方法明显慢一些。有鉴于此，建议仅
在必要的时候才使用Method对象，而最好使用接口及Java SE 8中的lambda表达式。






























