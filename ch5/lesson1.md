
## Object: 所有类的超类

Object类是Java中所有类的始祖，在Java中每个类都是由它扩展而来的。但是并不需要这样写：
```
public class Employee extends Object
```
如果没有明确地指出超类，Object就被认为是这个类的超类。由于在Java中，每个类都是由Object类扩展而来，
所以熟悉这个类提供的所有服务十分重要。

可以使用Object类型的变量引用任何类型的对象
```
Object obj = new Employee("Harry Hacker", 35000);
```
当然，Object类型的变量只能用于作为各种值的通用持有者，要想对其中的内容进行具体的操作，还需要
清楚对象的原始类型，并进行相应的类型转换
```
Employee e = (Employee) obj;
```

在Java中，只有基本类型（primitive types）不是对象。例如，数值、字符和布尔值类型的值不是对象。
所有的数组类型，不管是对象数组还是基本类型的数组都扩展了Object类。

> 在C++中没有所有类的根类，不过，每个指针都可以转换成 void* 指针。

#### equals 方法

Object类中的equals方法用于检测一个对象是否等于另外一个对象。在Object类中，这个方法将判断两个对象
是否具有相同的引用。如果两个对象具有相同的引用，它们一定是相等的。然而，对于大多数类来说，
这种判断并没有什么意义。然而，经常需要检测两个对象状态相等性，如果两个对象的状态相等，就认为
这两个对象是相等的。

如果两个雇员对象的姓名、薪水和雇佣日期都一样，就认为它们是相等的
```
public class Employee
{
    public boolean equals(Object otherObject)
    {
        // a quik test to see if the objects are identical
        if (this == otherObject) return true;
        
        // must return false if the explicit parameter is null
        if (otherObject == null) return false;
        
        // if the classes don't match, they can't be equal
        if (getClass() != otherObject.getClass()) return false;
        
        // now we know otherObject is a non-null Employee
        Employee other = (Employee) otherObject;
        
        // test whether the fields have identical values
        return name.equals(other.name)
            && salary = other.salary
            && hireDay.equals(other.hireDay);
    }
}
```
getClass方法将返回一个对象所属的类。

在子类中定义equals方法时，首先调用超类equals。如果超类中的域相等，就需要比较子类中的实例类。
```
public class Manager extends Employee
{
    ...
    public boolean equals(Object otherObject)
    {
        if (!super.equals(otherObject)) return false;
        
        Manager other = (Manager) otherObject;
        return bonus == other.bonus;
    }
}
```

#### 相等测试与继承

在Java标准库中包含了150多个equals方法的实现，包括使用instanceof检测、调用getClass
检测、捕获ClassCastException等。

编写一个完美的equals方法的建议：

1）显式参数命名为otherObject，稍后需要将它转换成另一个叫做other的变量。

2）检测this与otherObject是否引用同一个对象，`if (this == otherObject) return true;`

3）检测otherObject是否为null，如果为null，返回false。这项检测是很必要的
`if (otherObject == null) return false`

4）比较this与otherObject是否属于同一个类。如果子类能够拥有自己的相等概念，则对称性需求将
强制采用getClass进行检测，`if (getClass() != otherObject.getClass()) return false;`

如果由超类相等的概念，那么就可以使用instanceof进行检测，这样可以在不同子类的对象之间进行
相等的比较。

5）将otherOject转换为相应的类类型变量`ClassName other = (ClassName) otherObject`

6）现在开始对所有需要比较的域进行比较了。使用==比较基本类型域，使用equals比较对象域，如果所有
的域都匹配，就返回true，否则返回false。
```
return field1 == other.field1
    && Objects.equals(field2, other.field2)
    && ...;
```
如果在子类中重新定义equals，就要在其中包含调用super.equals(otherObject)。

> 对于数组类型的域，可以使用静态的Arrays.equals方法检测相应的数组元素是否相等

### hashCode 方法

散列码（hash code）是由对象导出的一个整型值，散列码是没有规律的，如果x和y是两个不同的对象，
x.hashCode()与y.hashCode()基本上不会相同。

由于hashCode方法定义在Object类中，因此每个对象都有一个默认的散列码，其值为对象的存储地址。

如果重新定义equals方法，就必须重新定义hashCode方法，以便用户可以将对象插入到散列表中。

equals与hashCode的定义必须一致，如果x.equals(y)返回true，那么x.hashCode()就必须与
y.hashCode()具有相同的值。例如，如果用定义的Employee.equals比较雇员的ID，那么hashCode
方法就不要散列ID，而不是雇员的姓名或存储地址。

如果存在数组类型的域，那么可以使用静态的Arrays.hashCode方法计算一个散列码，这个散列码
有数组元素的散列码组成。

#### toString 方法

在Object还有一个重要的方法，就是toString方法，它用于返回表示对象值的字符串。
下面是一个典型的例子，Point类的toString方法将返回下面这样的字符串
```
java.awt.Point[x=10,y=20]
```

下面是Employee类中的toString方法的实现
```
public String toString()
{
    return getClass().getName()
        + "[name=" + name
        + ",salary=" + salary
        + ",hireDay=" + hireDay
        + "]";
}
```

toString方法也可以供子类调用。当然，设计子类的程序员也应该定义自己的toString方法，
并将子类域的描述添加进去。

随处可见toString方法的主要原因是，只要对象域一个字符串通过操作符 "+"连接起来，
Java编译就会自动地调用toString方法。

println方法就会直接地调用x.toString()，并打印输出得到字符串。如果x是任意一个对象。
```
System.out.println(x)
```

令人烦恼的是，数组继承了object类的toString方法，数组类型将按照旧的格式打印，
修正的方式是调用静态方法Arrays.toString
```
int[] luckNumbers = {2, 3, 5, 7, 11, 13};
String s = Arrays.toString(luckNumbers);
```

**提示：**强烈建议为自定义的每一个类增加toString方法，这样做不仅自己收益，而且所有使用
这个类的程序员也会从这个日志记录支持中受益匪浅。

















