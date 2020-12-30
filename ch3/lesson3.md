
### 命令行参数

前面已经看到多个使用Java数组的示例，每一个Java应用程序都有一个带String arg[]参数的main方法，
这个参数表明main方法接收一个字符串数组，也就是命令行参数。
```
public class Message
{
    public static void main(String[] args)
    {
        if (args.length == 0 || args[0].equals("-h"))
            System.out.print("Hello,");
        else if (args[0].equals("-g"))
            System.out.print("Goodbye");
        for (int i = 1; i < args.length; i++)
            System.out.print(" " + args[i]);
        System.out.print("!");
    }
}
```
如果使用下面这种形式运行这个程序
```
java Message -g cruel world
```
args 数组将包含以下内容
```
args[0]: "-g"
args[1]: "cruel"
args[2]: "world"
```
在Java应用程序的main方法中，程序名并没有存储在args数组中，例如
`java Message -h world`args[0]是"-h"，而不是"Message"或"java"。

### 多维数组

多维数组将使用多个下标访问数组元素，它适用于表示表格或更加复杂的排列形式。
在Java中，声明一个二维数组相当简单
```
double[][] balances;
```
与一维数组一样，在调用new对多维数组进行初始化之前才能使用它，在这里可以这样初始化
```
balance = new double[10][6]
```
另外，如果知道数组元素，就可以不调用new，而直接使用简化的书写形式对多维数组进行初始化。
```
int[][] magicSquare = 
{
    {16, 3, 2, 13},
    {5, 10, 11, 8},
    {9, 6, 7, 12},
    {4, 15, 14, 1}
}
```
一旦数组被初始化，就可以利用两个方括号访问每个元素，例如`balances[i][j]` 

for each 循环语句不能自动处理二维数组的每一个元素，它是按照行，也就是一维数组处理的，要想访问二维
数组a的所有元素，需要使用两个嵌套的循环
```
for (double[] row: a)
    for (double value: row)
        do something with value
```

要想快速打印一个二维数组的数据元素列表，可以使用
```
System.out.print(Arrays.deepToString(a))
```

### 不规则数组

Java实际上没有多维数组，只有一维数组，多维数组被解释为"数组的数组"。
balance数组实际上是一个包含10个元素的数组，而每个元素又是一个由6个浮点数组组成的数组。

还可以方便的构造一个"不规则"数组，即数组的每一行有不同的长度。














