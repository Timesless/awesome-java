尚硅谷面试总结
  面向对象基础
    引用类型:
      强引用: =
      软引用: 内存不足时会回收SoftRefrence<?>指向的对象
      弱引用: GC会回收WeakRefrence<?>指向的对象
        通常以匿名对象作为k，否则失去弱 | 软 引用的实际意义
      虚引用: 与ReferenceQueue<PhantomReference<?>>搭配使用
      
  *java.util.concurrent
    CAS
    AQS (AbstractQueuedSynchronizer)
      等待队列1
      条件队列N
    ReentrantLock
    ReentrantReadWriteLock
    CountDownLatch
    Cyclicbarrier
    Semaphore
    ExecutorService
      ThreadPoolExecutor
    BlockingQueue
      ArrayBlockingQueue
      LinkedBlockingQueue
	  Synchronous
    Synchronized关键字和lock的区别
  *
  JVM
    类加载：<main()所在的类需要先加载和初始化>
      方法区是JVM规范，实现：8以前永久代，8及以后Metaspace
      1加载<根据类全限定名读取类二进制字节流到JVM方法区，并生成对应的java.lang.Class对象>
      2链接<验证，准备，解析>
        验证：语义验证，操作验证<是否正确引用>
        准备：为静态变量分配内存空间，赋0值，被final修饰的<常量>被直接赋值为字面量
        解析：常量池中符号引用转换为直接引用<类，字段，方法在内存中的指针|偏移量，以便直接调用>，只能是静态绑定的内容<编译时能确定>
          静态绑定：
            invokespecial： 1<init>, 2private(), 3super.method()
          动态绑定：需要初始化之后才能确定
            invokevitrual: 引用类型不能决定方法属于哪个实际类型<引用类型，实际类型>
      链接：将加载到到JVM方法区中类数据合并到JVM运行时状态中  
      3初始化
        为静态变量赋值，执行静态代码块<按源码中顺序>
    最终：方法区中存储当前类信息包括：静态变量，类初始化代码<静态变量赋值，静态块>，实例变量的定义，初始化代码<成员变量赋值，构造块>，实例方法，父类类信息引用
    
    *方法栈，操作数栈  
      自增自减可直接修改变量值，无需压入操作数栈 
      枚举构造器私有，默认扩展Enum，重写toString() 
    
    参数：标准，-X，-XX（boolean，kv）
    -XX:+PrintGCDetails
    -XX:MetaspaceSize=128M
    gc算法：
      复制，标记清除，标记清除整理
    gc收集器：
      Serial: -XX:UseSerialGC 在新生代使用Serial，老年代使用Serial Old  
      ParNew: -XX:UseParNewGC 在新生代使用并行，老年代依旧使用Serial Old  
      ParallelScavenge: -XX:UseParallel[Old]GC 在新生代使用并行，老年代使用并行，可互相激活 
      以上算法，新生代使用复制，老年代使用标记整理，无内存碎片。 
      CMS: -XX:UseConcMarkSweep 新生代使用ParNew，老年代使用标清（1Initial Mark 2Concurrent Mark 3Remark 4Concurrent Sweep） 
      G1: -XX:UseG1GC 
    
    Linux：
      top： <load average 1min 5min 15min 1可以查看具体CPU消耗>
      vmstat -n 2 3，mpstat -P ALL 2， pidstat -u 1 -p pid
      free -h | m内存
      df -h[uman]硬盘
      iostat -xdk 2 3磁盘io
      ifstat网络io
	*
  
