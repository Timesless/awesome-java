package com.yangzl.volume.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author yangzl
 * @Date: 2020/6/22 20:57
 * @Desc: JDK动态代理， -Dfile.encoding == System.getPropertites().put("file.encoding", "")
 */
public interface IDao {

    void sayHello();
}

class IDaoImpl implements IDao {
    @Override
    public void sayHello() {
        System.out.println("hello world");
    }
}

class DynamicProxy implements InvocationHandler {

    // 目标对象
    private final Object target;
    public DynamicProxy(Object target) { this.target = target; }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("before target call----------------------");
        Object rs = method.invoke(target, args);
        System.out.println("after target call-----------------------");
        return rs;
    }
}

class Client {
    public static void main(String[] args) {
        // System.getProperties().put("file.encoding", "gbk");

        DynamicProxy proxy = new DynamicProxy(new IDaoImpl());
        // sun.misc.ProxyGenerator会生成字节码，加载，连接，初始化。并创建代理对象
        IDao idao = (IDao) Proxy.newProxyInstance(IDao.class.getClassLoader(), new Class[]{ IDao.class }, proxy);
        idao.sayHello();

        System.out.println(System.getProperty("file.encoding"));
    }
}
