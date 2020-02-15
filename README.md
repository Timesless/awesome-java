### JAVA基础
> （final）变量捕获
#### 协变与逆变
> LSP
> + 子类完全拥有父类的方法，且具体子类必须实现父类抽象方法
> + 子类可增加自己的方法
> + 当子类覆盖或实现父类方法时，方法形参要比父类更宽松
> + 当子类覆盖或实现父类方法时，方法返回值比父类严格（具体）
>
> 逆变与协变用来描述类型转换（type transformation）后的继承关系，如果A、B表示类型，f(⋅)表示类型转换，≤表示继承关系（比如，A≤B表示A是由B派生出来的子类）；
> + f(⋅)是逆变（contravariant）的，当A≤B时有f(B)≤f(A)成立；
> + f(⋅)是协变（covariant）的，当A≤B时有f(A)≤f(B)成立；
> + f(⋅)是不变（invariant）的，当A≤B时上述两个式子均不成立，即f(A)与f(B)相互之间没有继承关系。

+ 泛型
> 令f(A)=ArrayList<A>，那么f(⋅)时逆变、协变还是不变的呢？如果是逆变，则ArrayList<Integer>是ArrayList<Number>的父类型；  
> 如果是协变，则ArrayList<Integer>是ArrayList<Number>的子类型；  
> 如果是不变，二者没有相互继承关系  
> 而List<Integer>与List<Number>没有关系，所以**泛型是不变的**
- - - -
 泛型通配符实现泛型的协变与逆变
 + ? extends T 实现协变，界定上界
 + ? super T 实现逆变，界定下界  

``` java
List<? extends Fruit> list = new ArrayList<Apple>();
list.add(new Apple()) // 编译错误
list.add(new Object()) // 编译错误
```
编译器不知道List<? extends Fruit>所持有的具体类型是什么，一旦执行这种类型的向上转型，将丢失掉向其中传递任何对象的能力
类比数组，尽管你可以把Apple[]向上转型成Fruit[]，然而往里面添加Fruit和Orange等对象都是非法的，  
会在运行时抛出ArrayStoreException异常。泛型把类型检查移到了编译期，协变过程丢掉了类型信息

我们还可以走另外一条路：逆变
``` java
List<? super Fruit> list = new ArrayList<>();
list.add(new Apple());
list.add(new Pear());
list.add(new Object()); // 编译错误
```
super指出泛型下界为Fruit，? super Fruit代表一个具体的类型，而这个类型是Fruit的基类

- - - -
+ 数组
> 令f(A)=[]A，容易证明数组是协变的   
> Number[] numbers = new Integer[3];   
> 这是因为数组在JAVA中是完全定义的，因此内建了编译器和运行时检查，但泛型是擦除的，所以只能把检测移到编译期

+ 方法 (~~输入是逆变的，输出是协变的~~)
> 调用方法result = method(n)；  
> Liskov替换原则，形参n的类型应为method形参的子类型，即typeof(n)≤typeof(method's parameter)；  
> result应为method返回值的基类型，即typeof(methods's return)≤typeof(result)
### 数据结构
#### 线性表
+ 静态数据结构（数组）
+ 动态数据结构（链表）
    > + 理解递归结构
    > + 深入理解指针
****
使用动态，静态数据结构实现栈，队列
****
##### 队列
##### 栈
#### 非线性结构
##### 树
+ 二叉搜索树
+ AVL树
+ 2-3树与左倾红黑树的等价性
+ 堆
    + 最大堆
    + 最小堆
+ 并查集
> 使用数组表示
+ 线段树
+ Trie（字典树，前缀树）
##### 图

### 算法

#### 分治与递归
> 分治通常需要递归实现
#### DP
> 动态规划：问题能分解为更小的子问题，子问题之间通常有公共子问题
#### 贪心算法
> 
#### 回溯


### LeedCode