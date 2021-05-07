package cn.xiaohupao.list.arraylist;

import org.jetbrains.annotations.Contract;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @Author: xiaohupao
 * @Date: 2021/5/7 19:05
 */
public interface MyCollection<E> extends Iterable<E> {
    //查询操作
    /**
     * 返回集合中元素个数
     * @return 集合中元素的个数
     */
    int size();

    /**
     * 判断集合是否为空
     * @return true则表示集合为空
     */
    boolean isEmpty();

    /**
     * 判断集合中是否包含指定元素
     * @param o 指定的元素
     * @return true则表示集合中包含指定的元素
     */
    boolean contains(Object o);

    /**
     * 返回此集合的迭代器
     * @return 返回迭代器
     */
    @Override
    Iterator<E> iterator();

    /**
     * 返回一个包含此集合所有元素的数组
     * @return 包含此集合中所有元素的数组
     */
    Object[] toArray();

    /**
     * 返回一个包含此集合所有元素的数组。
     * 返回数组的运行时类型是指定传入数组指定的数据类型。
     * 若传入数组的容量大小大于传入数组的大小，则将元素复制到该数组中。
     * 若传入数组的容量大小小于传入数组的大小，则重新分配数组。
     * @param a 指定的数组
     * @param <T> 指定的数据类型
     * @return 包含此集合所有元素的数组
     */
    <T> T[] toArray(T[] a);

    //修改操作

    /**
     * 在集合的尾部插入元素
     * @param e 待插入的元素
     * @return true则表示插入成功
     */
    boolean add(E e);

    /**
     * 移除指定的元素
     * @param o 指定的元素
     * @return true则表示移除成功
     */
    boolean remove(Object o);

    //批量操作

    /**
     * 判断此集合是否包含指定集合中的所有元素
     * @param c 指定的集合
     * @return true则表示此集合包含指定元素的所有元素
     */
    boolean containsAll(Collection<?> c);

    /**
     * 在此集合的尾部添加指定集合中包含的所有元素
     * @param c 指定的集合
     * @return true则表示添加成功
     */
    boolean addAll(Collection<? extends E> c);

    /**
     * 删除此集合中在指定集合中出现的元素
     * @param c 指定的集合
     * @return true则表示删除成功
     */
    boolean removeAll(Collection<?> c);

    /**
     * 删除此集合中满足给定条件的所有元素
     * @param filter 令元素移除成功的条件
     * @return true则表示所有的元素被移除
     */
    default boolean removeIf(Predicate<? super E> filter){
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()){
            if (filter.test(each.next())){
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * 保留此集合中包含在指定集合中的元素
     * @param c 指定的元素
     * @return true则表示集合应调用而发生改变
     */
    boolean retainAll(Collection<?> c);

    /**
     * 清空集合
     */
    void clear();

    //比较和哈希

    /**
     * 比较指定对象和此集合的相等性
     * @param o 指定的对象
     * @return true则表示与此集合相等
     */
    @Override
    boolean equals(Object o);

    /**
     * 返回此集合的哈希码
     * @return 此集合的哈希码
     */
    @Override
    int hashCode();

    /**
     * 在此集合中的元素上创建拆分器
     * @return 该集合中元素的分隔符
     */
    @Contract(pure = true)
    @Override
    default Spliterator<E> spliterator(){
        return Spliterators.spliterator(this, 0);
    }

    /**
     * 获取一个串行流接口
     * @return 获取串行流
     */
    default Stream<E> stream(){
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * 获取一个并行流
     * @return 获取并行流
     */
    default Stream<E> parallelStream(){
        return StreamSupport.stream(spliterator(), true);
    }
}
