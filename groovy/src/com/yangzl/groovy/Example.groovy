
class Example {
    
    static String path = System.getProperty("user.dir")
    
    static void main(String[] args) {
        def range = 5..10
        range.each {print(it + ' ')}
        println()
        for (i in range) {
            print(i + ' ')
        }
        println()
        println('method return: ' + defaultParam(4, 2))
        println()
        readThis()
        writeHello()
        contentCopy()
        
        visitWithRecurse()
        
        def map = [:]
        map.put('key1', 'val1')
        println(map)
        
        closure()

    }

    static def mth(Closure c) {
        c.call("Inner")
    }
    static def closure() {
        def cl = { println('hello closure') }
        cl.call()

        // 闭包中的形参
        def c2 = { param -> println("hello $param") }
        c2.call("param")
        def c3 = {println("hello $it")}
        c3.call("it")

        // 闭包可以引用外部类的变量，这与lambda不同
        String str = "hello"
        def c4 = {println("$str $it")}
        c4.call("param")
        str = "welcome"
        c4.call("param")

        println('============')

        // 在方法中使用闭包
        mth(c4)

        println('============')

        /*
         * 在 列表，映射中使用 闭包
         * find
         * findAll
         * any
         * collect 映射
         * grep 过滤
         */
        def map = ['tik': 'tok', 'duck': '不必', '不讲': 'wood']
        map.each {
            println(it.key + ": " + it.value)
        }
        def list = [1, 2, 3, 4]
        list.collect {it * it}.each {print it + ' '}
    }
    
    static def defaultParam(p1, p2 = 0, p3 = 0) {
        printf("p1: %s, p2: %s, p3: %s\n", p1, p2, p3)
        p1
    }
    
    static def readThis() {
        new File(path + '/src/com/yangzl/groovy/Example.groovy').each {print(it.length() + ' ')}
    }
    
    static def writeHello() {
        def list = ["hello groovy", "hello python3", "hello java"]
        new File(path + '/src/com/yangzl/groovy/hello.txt').withWriter('utf-8') {
            w -> list.each {
                w.writeLine(it)
            }
        }
    }
    
    static def contentCopy() {
        def source = new File(path + '/src/com/yangzl/groovy/hello.txt')
        def dst = new File(path + '/src/com/yangzl/groovy/copy.txt')
        
        dst << source.text
    }
    
    // 递归遍历文件夹与文件
    static def visitWithRecurse() {
        new File('d:/test').eachFileRecurse {
            println(it.getAbsolutePath())
        }
    }
}



