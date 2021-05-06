package cn.xiaohupao.list.arraylist;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.UnaryOperator;

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

    /**
     * 仅保留此list中包含指定集合中的元素
     * @param c 指定的集合
     * @return true则表示list成功更改
     */
    @Override
    boolean retainAll(Collection<?> c);

    /**
     * 用将该运算符应用于该元素的结果替换此列表中的每个元素
     * @param operator 应用于每个元素的运算符
     */
    default void replaceAll(UnaryOperator<E> operator){
        Objects.requireNonNull(operator);
        final ListIterator<E> li  = this.listIterator();
        while (li.hasNext()){
            li.set(operator.apply(li.next()));
        }
    }

    /**
     * 根据比较器的顺序对列表进行排序
     * @param c 用于比较列表元素的比较器
     */
    default void sort(Comparator<? super E> c){
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<E> i = this.listIterator();
        for (Object e : a){
            i.next();
            i.set((E) e);
        }
    }

    /**
     * 清空list
     */
    @Override
    void clear();

    /**
     * 比较指定对象与此list是否相等
     * @param o 指定的对象
     * @return true则表示指定对象等于此list
     */
    @Override
    boolean equals(Object o);

    /**
     * 返回此list的哈希码值
     * @return 此列表的哈希码值
     */
    @Override
    int hashCode();

    /**
     * 获取索引位置上的元素
     * @param index 指定的索引
     * @return 指定索引位置上的元素
     */
    E get(int index);

    /**
     * 设置指定索引位置上的元素
     * @param index 指定的索引位置
     * @param element 指定的元素
     * @return 被替换的元素
     */
    E set(int index, E element);

    /**
     * 在指定的索引位置插入元素
     * @param index 指定的索引位置
     * @param element 插入的元素
     */
    void add(int index, E element);

    /**
     * 删除指定索引位置的元素，并返回
     * @param index 指定的索引位置
     * @return 删除的元素
     */
    E remove(int index);

    /**
     * 返回指定元素在此列表中首次出现的索引
     * @param o 搜索元素
     * @return 指定元素在此列表中首次出现的索引
     */
    int indexOf(Object o);

    /**
     * 返回指定元素在此list中最后一次出现的索引
     * @param o 搜索元素
     * @return 返回指定元素在此list中最后一次出现的索引
     */
    int lastIndexOf(Object o);

    /**
     * 返回此list的列表迭代器
     * @return list的列表迭代器
     */
    ListIterator<E> listIterator();

    /**
     * 从列表中的指定位置开始，以适当的顺序返回此list的列表迭代器
     * @param index 指定的索引
     * @return 指定索引位置上的list的列表迭代器
     */
    ListIterator<E> listIterator(int index);

    /**
     * 返回此列表中指定范围的子序列
     * @param fromIndex 子序列的开始(包括)
     * @param toIndex 子序列的末尾(不包括)
     * @return 此list中指定范围的子序列
     */
    MyList<E> subList(int fromIndex, int toIndex);

    /**
     * 在此list上创建拆分器
     * @return 在此列表中的元素上使用分隔符
     */
    @Override
    default Spliterator<E> spliterator(){
        return Spliterators.spliterator(this, Spliterator.ORDERED);
    }
}
