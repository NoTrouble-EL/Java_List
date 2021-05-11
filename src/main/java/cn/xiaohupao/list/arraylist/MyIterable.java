package cn.xiaohupao.list.arraylist;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * @Author: xiaohupao
 * @Date: 2021/5/11 11:57
 */
public interface MyIterable<T> {
    /**
     * 返回一个内部为T类型的迭代器
     * @return 一个内部为T类型的迭代器
     */
    Iterator<T> iterator();

    /**
     * 对于Iterator的每个元素执行给定的操作，知道处理完所有的元素或改操作引发异常为止
     * @param action 每个元素要执行的动作
     */
    /*default void forEach(Consumer<? super T> action){
        Objects.requireNonNull(action);
        for (T t : this){
            action.accept(t);
        }
    }*/

    /**
     * 创建并返回一个可分割迭代器
     * @return 可分割迭代器
     */
    default Spliterator<T> spliterator(){
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
