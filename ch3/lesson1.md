

## Java基本程序设计结构二


### 码点与代码单元

Java字符串由char值序列组成，char类型是一个采用UTF-16编码表示Unicode码点的代码单元，大多数的常用
Unicode字符使用一个代码单元表示，有些需要一对代码单元表示。

length方法将返回采用UTF-16编码表示的给定字符串所需要的代码单元数量。

一个字符串的码点数不一定等于代码单元数，因为有的字符包含两个代码单元（UTF-16编码），要想
得到实际的码点长度，可以调用
```
String greeting = "Hello";
int cpCount = greeting.codePointCount(0, greeting.length());
// 此时 cpCount = greeting.length() is 5
```
调用s.charAt(n)将返回位置n的代码单元，n介于0 ~ s.length()-1之间，如果字符串中有的字符包含两个代码单元，如果
此时使用chart返回字符串中的字符，可能会出现问题。

如果想要遍历一个字符串，并且一次查看每一个码点（相应字符Unicode十进制值），可以使用以下语句
```
int cp = sentence.codePointAt(i);
if (Character.isSupplementaryCodePoint(cp)) i += 2;
else i++;
```
显然这很麻烦，使用codePoints方法，它会生成一个int值的流，每个int值对应一个码点，可以将它转换成
为一个数组
```
int[] codePoints = str.codePoints().toArray();

// 此时str字符串真实字符个数为 codePoints.length
// str.length() 是代码单元个数
```
把一个码点数组转换为一个字符串，可以使用构造函数
```
String str = new String(codePoints, 0, codePoints.length)
```


### String API

- int indexOf(String str)
- int indexOf(String str, int fromIndex)
- int indexOf(int cp)
- int indexOf(int cp, int fromIndex)
返回与字符串str或代码点cp匹配的第一个子串的开始位置，这个位置从索引0或fromIndex开始计算，如果在原始
串中不存在str返回-1

- int lastIndexOf(String str)
- int lastIndexOf(String str, int fromIndex)
- int lastIndexOf(int cp)
- int lastIndexOf(int cp)
- int lastIndexOf(int cp, int fromIndex)

Returns the index within this string of the last occurrence of the specified substring, 
searching backward starting at the specified index.
返回与字符串str或代码点cp匹配的最后一个子串的开始位置，从fromIndex位置反向搜索，也就是从fromeIndex
反向搜索到的第一个子串匹配的位置。


[阅读联机API](https://docs.oracle.com/javase/8/docs/api/)

