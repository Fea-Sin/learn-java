

## 通配符类型

固定的泛型类型系统使用起来并没有那么令人愉快，类型系统的研究人员知道这一点已经有一段时间了。
Java的设计者发明了一种巧妙的（仍然是安全的）"解决方案"，通配符类型。

### 通配符概念

通配符类型中，允许类型参数变化。例如，通配符类型
```
Pair<? extends Employee>
```
表示任何泛型Pair类型，它的类型参数是Employee的子类，如Pair<Manager>，但不是Pair<String>

假设要编写一个打印雇员对的方法，
```
public static void printBuddies(Pair<Employee> p)
{
    Employee first = p.getFirst();
    Employee second = p.getSecond();
    System.out.println(first.getName() + "and" + second.getName() + "are buddies.");
}
```
正如前面将到的，不能将Pair<Manager>传递给这个方法，这一点很受限制。解决的方法很简单，使用通配符类型
```
public static void printBuddies(Pair<? extends Employee> p)
```
类型Pair<Manager>是Pair<? extends Employee>的子类型。

使用通配符会通过Pair<? extends Employee>的引用破坏Pair<Manager>吗？
```
Pair<Manager> managerBuddies = new Pair<>(ceo, cfo);
Pair<? extends Employee> wildcardBuddies = managerBuddies; // OK
wildcardBuddies.setFirst(lowlyEmployee); // compile-time error
```
这可能不会引起破坏。对setFirst的调用有一个类型错误。
这将不可能调用setFirst方法，编译器只知道需要某个Employee的子类型，但不知道具体什么类型。它拒绝
任何特定的类型。
使用getFirst就不存在这个问题，将getFirst的返回值赋给一个Employee的引用完全合法。
这就是引入有限定的通配符的关键之处。现在已经有办法区分安全的访问器方法和不安全的更改器方法了。

### 通配符的超类型限定

通配符限定与类型变量限定十分类似，但是，还有一个附加的能力，即可以指定一个超类型限定（supertype bound）
```
? super Manager
```
这个通配符限制为Manager的所有超类型。

例如，Pair<? super Manager>有方法
```
void setFirst(? super Manager)
? super Manager getFirst()
```
编译器无法知道setFirst方法的具体类型，因此调用这个方法时不能接收类型为Employee或Object的参数，
只能传递Manager类型的对象，或者某个子类型（如Executive）对象。另外，如果调用getFirst，不能
保证返回对象的类型，只能把它赋给一个Object。
下面是一个典型的示例。有一个经理的数组，并且想把奖金最高的和最低的经理放在一个Pair对象中。
Pair的类型是什么？在这里，Pair<Employee>是合理的，Pair<Object>也是合理的。下面的方法将可以接受
任何适当的Pair
```
public static void minmaxBonus(Manager[] a, Pair<? super Manager> result)
{
    if (a.length == 0) return;
    Manager min = a[0];
    Manager max = a[0];
    for (int i = 1; i < a.length; i++)
    {
        if (min.getBonus() > a[i].getBonus()) min = a[i];
        if (max.getBonus() < a[i].getBonus()) max = a[i];
    }
    result.setFirst(min);
    result.setSecond(max);
}
```
直观的讲，**带有超类型限定的通配符可以向泛型对象写入，带有子类型限定的通配符可以从泛系对象读取**。

### 无限定通配符

还可以使用无限定的通配符，例如，Pair<?>。初看起来，这好像与原始的Pair类型一样，实际上，
有很大不同。类型Pair<?>有以下方法
```
? getFirst()
void setFirst(?)
```
getFirst的返回值只能赋给一个Object。setFirst方法不能被调用，甚至不能用Object调用。
Pair<?>和Pair本质的不同在于，可以用任意Object对象调用原始Pair类的setObject方法。



























