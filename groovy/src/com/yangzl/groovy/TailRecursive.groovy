
class TailRecursive {
    
    static long factorialTailRecursive(long n) {
        tailRecursiveHelp(1, n)
    }

    /**
     * “尾-递” tail-recursive：递归调用发生在最后一步
     *      
     *      首先理解什么是尾调用：(本质是只保留内层函数的调用记录)
     *          // 完全可以删除掉 f(x) 的调用记录，只保存 g(x)
     *          function f(x) {
     *              return g(x);
     *          }
     *          // 同上
     *          function f(x) {
     *              if (x > 0)
     *                  return m(x);
 *                  return n(x);
     *          }
     *      
     *      // 以下情况都是尾调
     *      function f(x) {
     *          let y = g(x);   // 调用g(x)后还有后续操作，栈帧无法复用
     *          return y;
     *      }
     *      function f(x) {
     *          return g(x) + 1;    // 同上
     *      }
     *              
     *          
     *      scala，groovy语言，（直接将中间结果 n * acc，作为参数传递给该方法）编译器会复用栈帧，即：所谓的尾调优化
     *      Java暂不支持这种优化
     *      
     *      普通递归需要通过栈帧来保存中间变量 n * factoria(n - 1)
     * @param acc
     * @param n
     * @return
     */
    static long tailRecursiveHelp(long acc, long n) { 
        n == 1 ? acc : tailRecursiveHelp(n * acc, n - 1)
    }
    

    static void main(String[] args) {
        println(factorialTailRecursive(20_000))
    }
}
