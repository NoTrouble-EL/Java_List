package cn.xiaohupao.list.arraylist;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author xiaohupao
 * @date 2021/5/5
 */
public interface MyList <E> extends Collection<E> {
    /**
     * 返回列表中的元素数
     * @return list中的元素个数
     */
    @Override
    int size();

    /**
     *判断list是否为空
     * @return true表示list为空
     */
    @Override
    boolean isEmpty();

    /**
     *判断list中是否包含指定元素
     * @param o 指定的元素
     * @return true表示list中包含指定的元素
     */
    @Override
    boolean contains(Object o);

    /**
     *list的迭代器
     * @return 此list中的迭代器
     */
    @Override
    Iterator<E> iterator();

    /**
     *将list集合转换为数组
     * @return 包含此list中所有元素的数组
     */
    @Override
    Object[] toArray();

    /**
     *返回一个数组，该数组按正确的顺序包含此list中的所有元素
     * @param a 传入的数组，若该数组存储空间足够大，则将元素存储到该数组中，否则为此分配一个具有相同运行时类型的新数组
     * @param <T> 相同类型的运行时类型的数组
     * @return 包含此list元素的数组
     */
    @NotNull
    @Override
    <T> T[] toArray( T @NotNull [] a);

    /**
     * 将指定元素追加到list的末尾
     * @param e 指定的元素
     * @return true表示添加成功
     */
    @Override
    boolean add(E e);

    /**
     *删除指定元素在list中首次出现位置上的元素
     * @param o 指定的元素
     * @return true则表示删除成功
     */
    @Override
    boolean remove(Object o);

    /**
     * 检查此list中包含指定集合的所有元素
     * @param c 检查的元素
     * @return true则表示此list中包含指定集合中所有的元素
     */
    @Override
    boolean containsAll(Collection<?> c);

    /**
     * 将指定集合中的元素添加到此list的末尾
     * @param c 指定的元素集合
     * @return true则表示添加成功
     */
    @Override
    boolean addAll(Collection<? extends E> c);

    /**
     * 将指定集合中的所有元素插入此列表中的指定位置
     * @param index 要插入的指定位置
     * @param c 指定的添加元素集合
     * @return true则表示添加成功
     */
    boolean addAll(int index, Collection<? extends E> c);

    /**
     *从此list中删除所有包含在指定集合中的元素
     * @param c 包含要删除元素的集合
     * @return true则表示删除成功
     */
    @Override
    boolean removeAll(Collection<?> c);
}
