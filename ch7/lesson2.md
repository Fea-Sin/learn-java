

## 约束与局限性

下面几节中，将阐述使用Java泛型时需要考虑的一些限制。大多数限制都是由类型擦除引起的。

### 不能用基本类型实例化类型参数

不能用类型参数代替基本类型。因此，没有Pair<double>，只有Pair<Double>。当然，
其原因是类型擦除。擦除之后，Pair类含有Object类型的域，而Object不能存储double值。

这的确令人烦恼。但是，这样做与Java语言中基本类型的独立状态相一致。这并不是一个致命的缺陷，
只有8中基本类型，当包装器类型（wrapper type）不能接受替换时，可以使用独立的类和方法
处理它们。

### 运行时类型查询只适用于原始类型

虚拟机中对象总有一个特定的非泛型类型。因此，所有的类型查询只产生原始类型。

试图查询一个对象是否属于某个泛型类型时，倘若使用instanceof会得到一个编译错误，如果
使用强制类型转换会得到一个警告。
```
if (a instanceof Pair<String>) // Error

if (a instanceof Pair<T>)  // Error
```

### 不能创建参数化类型的数组

不能实例化参数类型的数组
```
Pair<String>[] table = new Pair<String>[10];  // Error
```
这有什么问题呢？擦除之后，table的类型是Pair[]。可以把它转换为Object[]
```
Object[] objarray = table;
```
数组会记住它的元素类型，如果试图存储其他类型的元素，就会抛出一个Array-StoreException异常。
不过对于泛型类型，擦除会使这种机制无效。以下赋值
```
objarray[0] = new Pair<Employee>();
```
能够通过数组存储检查，不过仍会导致一个类型错误。出于这个原因，不允许创建参数化类型的数组。

需要说明的是，只是不允许创建这些数组，而声明类型为Pair<String>的变量仍是合法的。不过
不能用`new Pair<String>[10]`初始化这个变量。

### Varargs 警告

上一节已经了解到，Java不支持泛型类型的数组。这一节中我们在来讨论一个相关的问题，
向参数个数可变的方法传递一个泛型类型的实例
```
public static <T> void addAll(Collection<T> coll, T... ts)
{
    for(t : ts) coll.add(t)
}
```
应该记得，实际上参数ts是一个数组，包含提供的所有实参。
```
Collection<Pair<String>> table = ...;
Pair<String> pair1 = ...;
Pair<String> pair2 = ...;
addAll(table, pair1, pair2)
```
为了调用这个方法，Java虚拟机必须建立一个Pair<String>数组，这就违反了前面的规则。
不过，对于这种情况，规则有所放松，你只会得到一个警告，而不是错误。
可以采用两种方法来抑制这个警告。一种方法是为包含addAll调用的方法增加注解@SupperssWarnings("unchecked")。
还可以用@SafeVarargs直接标注addAll方法
```
@SafeVarargs
public static <T> void addAll(Collection<T> coll, T... ts)
```

### 不能实例化类型变量













