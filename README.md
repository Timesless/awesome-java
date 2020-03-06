### 变量名派系
+ 字母派：a, b, c, a1, b1, c1
+ 现代字母派：array, brray, crray
+ 后缀派：img, jpg, doc, xls
+ 卖萌派：QAQ, QAT, QWQ, TAT, TQT
+ 复读派：n, nn, nnn
+ 重排派：next, enxt, xnet
+ 化学派：co2, h20, ch4
+ 下划线派：_, __, ___
+ 脏话派：f\*\*k, s\*\*t, t**d
+ 滚键盘派：qwer, asdf, zxcv
+ 学术派：alpha, beta, gamma
+ 月球派：saber, archer, rider
+ 圈圈派：O0,OO0,O0O
+ 条形码派：IIII, llll, iiii

### I/O原理
> 用户程序IO读写依赖OS底层read&write系统  
> 而read&write系统的调用并不是直接与物理设备进行数据交换（不是物理设备级别读写），而是缓冲区之间数据复制  
> read系统调用：数据从内核缓冲区复制到用户进程缓冲区  
> write系统调用：数据从用户缓冲区复制到内核缓冲区  
+ 为什么设置这么多缓冲区？
> 物理设备的直接读写涉及到操作系统的中断，开销较大，为减少底层系统时间性能损耗， 出现缓冲区   
> OS对缓冲区进行监控，等待缓冲区达到一定数量时再进行IO设备中断处理，至于什么时候中断（读，写中断），由内核维护，用户无需关心   
+ 典型socket调用流程  
client <--> 网卡 <-read & write-> 内核空间(kernel buffer, socket buffer) <-read & write-> 用户缓冲区
    + 数据准备（DMA copy: OS在物理设备与内核缓冲区交换数据，该阶段用户无感知）
    + 内核缓冲区数据复制到进程缓冲区(CPU copy)
> Java服务器完成一次socket请求和响应，流程如下   
> 请求到达网卡后，调用read系统从硬盘 -DMA-> kernel buffer -CPU-> JVM buffer -CPU-> socket buffer -DMA-> 网卡

名词解释：  
用户空间(user space): 此处指JVM进程  
内核空间(kernel space): 内核缓冲区(kernel buffer), socket缓冲区(kernel socket buffer)  
硬件： 硬盘，网卡(协议引擎)

+ zero-copy(DMA copy无法避免，减少CPU copy（kernel buffer & user buffer可能需要传输fd）)
> mmap & write：   
> 数据通过 DMA 拷贝到 OS内核缓冲区。接着应用程序跟OS共享该缓冲区,这样OS内核和应用程序空间就不需要再进行任何的数据拷贝。      
> 应用程序调用write()之后，OS内核将数据从原来的内核缓冲区中拷贝到与 socket 相关的内核缓冲区中。  
> 接下来，数据从内核 socket 缓冲区拷贝到协议引擎中去   
> sendfile：     
> Linux 在版本 2.1 中引入了 sendfile() 这个系统调用。sendfile()不仅减少了数据拷贝操作，它也减少了上下文切换。    
> 首先：sendfile()系统调用利用 DMA 引擎将文件中的数据拷贝到OS内核缓冲区中，然后数据被拷贝到与 socket 相关的内核缓冲区中去。   
> 接下来，DMA 引擎将数据从内核 socket 缓冲区中拷贝到协议引擎中去。   
> 如果在用户调用sendfile() 系统调用进行数据传输的过程中有其他进程截断了该文件，sendfile()会返回给用户应用程序中断前所传输的字节数   
> splice：   
> gather()&scatter()：gather()可以从多个缓冲区读数据聚集，scatter()可以分散到多个缓冲区写数据?   
- - - - 
+ 4种IO模型
    + 同步阻塞IO  
    + 同步非阻塞IO(Non-blocking IO)  
用户空间线程请求IO，内核返回用户一个状态值，继续执行用户空间代码流程  
    + IO多路复用(IO Multiplexing)  
即经典的Reactor反应器模式，需要底层Synchronous Event Demultiplexer支持，如Java的Selector，linux的select / epoll 
    + 异步IO(Asynchronous IO)   
    
Reactor基于事件驱动（回调）scalable IO in Java中Basic Reactor模型如下：    
> 1. Setup: 服务器端注册到selector并关注OP_ACCEPT事件   
> 2. Dispatch Loop: 循环selector.select()   
> 3. Acceptor: 处理OP_ACCEPT事件，生成SocketChannel   
> 4. Handler setup: 读写事件处理器注册到selector   
> 5. Request handling: 请求处理    
>
> 多个client连接到一个Reactor，Reactor分发连接事件到acceptor，分发读写事件到handler(每个handler开启一个线程)处理请求    

**单Reactor多线程**：多worker线程（处理读写事件的handler采用线程池）   
**主从Reactor**：多Reactor，多线程（处理连接事件多线程，处理读写事件采用线程池）   
**netty**基于主从Reactor改进，采用事件循环组NioEventLoopGroup  
每个事件循环(NioEventLoop)模式同主从Reactor  
> while轮询（BossGroup轮询accpet，WorkerGroup轮询读写事件）   
> 处理：BossGroup处理连接事件生成NioSockeChannel注册到selector，WorkerGroup处理读写事件   
> runAllTask 处理队列中任务   
> 阻塞 & 非阻塞：需要内核IO操作完成，才返回到用户空间执行用户操作，阻塞指用户空间程序执行状态   
> 同步 & 异步：指用户空间与内核空间IO发起方式。  
>   同步指IO用户空间线程发起IO请求，异步指内核发起IO请求（注册各种IO事件回调，内核完成后主动调用）  

### 面试
+ 项目经历
+ 项目遇到的实际问题
+ 印象最深的bug
+ 设计模式
+ 网络
+ 并发

> 解决问题的思路，方向，考虑问题的条件，能否给出提示  
> 选择技术，标准，小组运行模式，后续规划，产品某个问题怎么解决的

### JAVA基础
> 局部类与匿名内部类捕获作用域返回内final,effectively final变量
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