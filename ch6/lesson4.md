
## 代理

利用代理（proxy）可以在运行时创建一个实现了一组给定接口的新类。这种功能只有在编译时无法
确定需要实现哪个接口时才有必要使用。对于应用程序设计人员来说，遇到这种情况的机会很少。然而，
对于系统程序设计人员来说，代理带来的灵活性却十分重要。

### 何时使用代理

假设有一个表示接口的Class对象（有可能只包含一个接口），它的确切类型在编译时无法知道。这确实
有写难度。要想构造一个实现这些接口的类，就需要使用newInstance方法或反射找出这个类的构造器。
但是，不能实例化一个接口，需要在程序处于运行状态时定义一个新类。

为了解决这个问题，有些程序将会生成代码，将这些代码放置在一个文件中，调用编译器，然后再加载
结果类文件。很自然，这样做的速度会比较慢，并且需要将编译器与程序放在一起。而代理机制则是
一种更好的解决方案。代理类可以在运行时创建全新的类。这样的代理类能够实现指定的接口。尤其是，
它具有下列方法：

- 指定接口所需要的全部方法
- Object类中的全部方法，例如，toString、equals等

然而，不能在运行时定义这些方法的新代码。而是要提供一个调用处理器（invocation handler）。
调用处理器是实现了InvocationHandler接口的类对象。在这个接口中只有一个方法
```
Object invoke(Object proxy, Method method, Object[] args)
```
无论何时调用代理对象的方法，调用处理器的invoke方法都会被调用，并向其传递Method对象和原始
的调用参数。调用处理器必须给出处理调用的方法。

### 创建代理对象

要想创建一个代理对象，需要使用Proxy类的newProxyInstance方法。这个方法有三个参数

- 一个类加载器（class loader）。作为Java安全模式的一部分，对于系统类和因特网上下载的来的类，
可以使用不同的类加载器。用null表示使用默认的类加载器。
- 一个Class对象数组，每个元素都是需要实现的接口
- 一个调用处理器

还有两个需要解决的问题。如何定义一个处理器？能够用结果代理对象做些什么？当然，这两个问题的答案
取决于打算使用代理机制解决什么问题。使用代理可能处于很多原因，例如

- 路由对远程服务器的方法调用
- 在程序运行期间，将用户接口事件与动作关联起来
- 为调试，跟踪方法调用

### 代理类的特性

现在，我们已经看到了代理类的应用，接下来了解它们的一些特性。需要记住，代理类是在程序运行过程中
创建的。然而，一旦被创建，就变成了常规类，与虚拟机中的任何其他类没什么区别。

所有的代理都扩展与Proxy类。一个代理类只有一个实例域--调用处理器，它定义在Proxy的超类中。为了
履行代理对象的职责，所需要的任何附加数据都必须存储在调用处理器中。

所有的代理类都覆盖了Object类中的方法toString、equals和hashCode。如同所有的代理方法一样，
这些方法仅仅调用了调用处理器的invoke。Object类中的其他方法（如clone和getClass）没有被重新定义。

没有定义代理类的名字，Sun虚拟机中的Proxy类将生成一个以字符串$Proxy开头的类名。
对于特定的类加载器和预设的一组接口来说，只能有一个代理类。也就是说，如果使用同一个类加载器和
接口数组调用两次newProxyInstance方法的话，那么只能够得到同一个类对象，也可以利用getProxyClass
方法获得这个类
```
Class proxyClass = Proxy.getProxyClass(null, interfaces);
```

代理类一定是public和final。如果代理类实现的所有接口都是public，代理类就不属于某个特定的包，
否则，所有非公有的接口必须都属于同一个包，同时，代理类也属于这个包。

可以通过调用Proxy类中的isProxyClass方法检测一个特定的Class对象是否代表一个代理类。

到此为止，Java程序设计语言的基础概念介绍完毕了。接口、lambda表达式和内部类是经常使用的
几个概念。然而，克隆和代理是库设计者和工具构造者感兴趣的高级技术。


















