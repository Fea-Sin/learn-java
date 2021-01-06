
## 对象构造

前面已经学习了编写简单的构造器，可以定义对象的初始状态。由于对象构造非常重要，所以Java提供了
多种编写构造器的机制。

#### 重再

如果多个方法有相同的名字、不同的参数，变产生了重载。编译器必须挑选出具体执行哪个方法，它通过
用各个方法给出参数类型与特定方法调用所有的值类型进行匹配来挑选出相应的方法。如果编译器找不到
匹配的参数，就会产生编译时错误。

Java允许重载任何方法，而不只是构造器方法。因此，要完整的描述一个方法，需要指出方法名以及参数
类型，这叫做方法签名（signature）。例如，String类有4个称为indexOf的公有方法，它们的签名是
```
indexOf(int)
indexOf(int, int)
indexOf(String)
indexOf(String, int)
```
返回类型不是方法签名的一部分，也就是说，不能有两个名字相同、参数类型也相同却返回不同类型值方法。

#### 默认域初始化

如果在构造器中没有显式地给域赋予初值，那么就会被自动地赋为默认值，数值为0、布尔值为false、
对象引用为null。如果不明确地对域进行初始化，就会影响程序代码，这并不是一种良好的编程习惯。

主是域与局部变量的主要不同点，必须明确地初始化方法中的局部变量。但是，如果没有初始化类中的域，
将会被自动初始化为默认值（0，false或null）。

#### 无参数的构造器

很多类都包含一个无参数的构造函数，对象由无参数构造函数创建时，其状态会设置为适当的默认值，例如
```
public Employee()
{
    name = "";
    salary = 0;
    hireDay = LocalDate.now();
}
```
如果在编写一个类时没有编写构造器，那么系统就会提供一个无参数构造器，这个构造器将所有的实例域设置为
默认值，实例域中的数值型数据设置为0、布尔型数据设置为false、所有对象变量将设置为null。

如果类中提供了至少一个构造器，但是没提供无参数的构造器，则在构造对象时如果没有提供参数就会视为不合法。
仅当类没有提供任何构造器的时候，系统才会提供一个默认的构造器。

如果在编写类的时候，给出了一个构造器，要想让这个类的用户能够采用下列方式构造实例
```
new ClassName();
```
就必须提供一个不带参数的构造器，如果希望所有域被赋予默认值，可以采用下列格式
```
public ClassName()
{}
```

#### 显式域初始化

通过重载类的构造器方法，可以采用多种形式设置类的实例域的初始状态，确保不管怎样调用构造器，每个
实例域都可以被设置为一个有意义的初值，这是一种很好的设计习惯。

可以在类定义中，直接将一个值赋给域，例如
```
class Employee
{
    private String name = "";
    ...
}
```
在执行构造器之前，先执行赋值操作。当一个类的所有构造器都希望把相同的值赋予某个特定的实例域时，
这种方法特别有用。

初始值不一定是常量值，下面的例子中，可以调用方法对域进行初始化
```
class Employee
{
    private static int nextId;
    private int id = assignId();
    
    private static int assignId()
    {
        int r = nextId;
        nextId++;
        return r;
    }
    ...
}
```

在C++中，不能直接初始化类的实例域，所有的域必须在构造器中设置。但是，有一个特殊的初始化器列表语法。
```
// C++
Employee::Employee(String n, double s, int y, int m, int d)
: name(n),
  salary(s),
  hireDay(y, m, d)
{}
```

#### 调用另一个构造器

关键字this引用方法的隐式参数。然而，这个关键字还有另外一个含义。
如果构造器的第一个语句型如this()，这个构造器将调用同一个类的另一个构造器。例如
```
public Employee(double s)
{
    this("Employee #" + nextId, s);
    nextId++;
}
```
当调用new Employee(10)，Employee(double)构造器将调用Employee(String, double)构造器。

采用这种方式使用this关键字非常有用，这样对公共的构造器代码部分只编写一次即可。

C++中，一个构造器不能调用另一个构造器，必须将抽取出的公共初始代码编写成一个独立的方法。

#### 初始化快

前面已经讲过两种初始化数据的方法

- 在构造器中设置值
- 在声明中赋值

实际上，Java还有第三种机制，称为初始化块（initialization block）,在一个声明中可以包含多个代码块。
只要构造类的对象，这些块就会被执行。

对象初始化块
```
class Employee
{
    private static int nextId;
    private int id;
    private String name;
    private double salary;
    
    // object initialization block
    {
        id = nextId;
        nextId++;
    }
}
```

如果对类的静态域进行初始化的代码比较复杂，那么可以使用静态的初始化块。将代码放在一个块中，并标记
关键字static。
```
// static initoalization block
static
{
    Random generator = new Random();
    nextId = generator.nextInt(10000);
}
```
在类第一次加载的时候，将会进行静态域的初始化，与实例域一样。

由于初始化数据域有多种途径，所以列出构造过程的所有路径可能相当混乱。下面是调用构造器的具体处理步骤

1）所有数据被初始化为默认值
2）按照在类声明中出现的次序，以此执行所有的域初始化语句和初始化块
3）如果构造器第一行调用了第二个构造器，则执行第二个构造器主体
4）执行这个构造器的主体

#### 对象析构与finalize方法

有些面向对象的程序设计语言，特别是C++，有显式的析构器方法，其中放置一些当对象不再使用时
需要执行的清理代码。在析构器中，最常见的操作是回收分配给对象的存储空间。由于Java有自动的垃圾
回收器，不需要人工回收内存，所以Java不支持析构器。

当然，某些对象使用了内存之外的其他资源，例如，文件或使用了系统资源的另一个对象的句柄。在这种情况下，
当资源不再需要时，将其回收和再利用将显得十分重要。

可以为任何一个类添加finalize方法，finalize方法将在垃圾回收器清除对象之前调用。在实际应用中，
不要依赖使用finalize方法回收任何短缺的资源，这是因为很难知道这个方法什么时候才能够调用。

如果某个资源需要在使用完毕后立刻被关闭，那么就需要由人工来管理，对象用完时，可以应用一个close方法
来完成相应的清理操作。

















