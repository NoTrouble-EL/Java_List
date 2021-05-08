package cn.xiaohupao.list.arraylist;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * @Author: xiaohupao
 * @Date: 2021/5/8 9:09
 */
public abstract class MyAbstractCollection<E> implements MyCollection<E>{
    /**
     * 空间构造
     */
    protected MyAbstractCollection(){
    }

    //查询操作

    /**
     * 返回此集合的迭代器
     * @return 此集合的迭代器
     */
    @Override
    public abstract Iterator<E> iterator();

    /**
     * 此集合中的元素个数
     * @return 此集合中的元素个数
     */
    @Override
    public abstract int size();

    /**
     * 判断此集合是否为空
     * @return true则表示集合为空
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 判断集合中是否包含指定的元素
     * @param o 指定的元素
     * @return true则表示集合中包含指定的元素
     */
    @Override
    public boolean contains(Object o) {
        Iterator<E> it = iterator();
        if (o == null){
            while (it.hasNext()){
                if (it.next() == null){
                    return true;
                }
            }
        }else{
            while (it.hasNext()){
                if (o.equals(it.next())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回一个包含此集合中的所有元素的数组
     * @return list to 数组
     */
    @Override
    public Object[] toArray() {
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++){
            if (! it.hasNext()){
                return Arrays.copyOf(r, i);
            }
            r[i] = it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * 将集合中的元素转换为一个数组，返回数组的运行时类型是指定数组的运行时类型
     * @param a 指定的数组
     * @param <T> 指定的运行时类型
     * @return 包含集合中所有元素的数组
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T @NotNull [] a) {
        int size = size();
        T[] r = a.length >= size ? a :
                (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();

        for (int i = 0; i < r.length; i++){
            if (! it.hasNext()){
                if (a == r){
                    r[i] = null;
                }else if (a.length < i){
                    return Arrays.copyOf(r, i);
                }else{
                    System.arraycopy(r,0, a,0, i);
                    if (a.length > i){
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (T) it.next();
        }
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * 要分配的数组的最大大小
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 当迭代器返回的元素数量超出预期时，在toArray中重新分配正在使用的数组，并从迭代器完成填充
     * @param r 传入的素组
     * @param it 迭代器
     * @param <T> 数组的运行时类型
     * @return 包含给定数组中的元素的数量，以及迭代器返回的所有其他元素。
     */
    private static <T> T[] finishToArray(T[] r, Iterator<?> it){
        int i = r.length;
        while (it.hasNext()){
            int cap = r.length;
            if (i == cap){
                int newCap = cap + (cap >> 1) + 1;
                if (newCap - MAX_ARRAY_SIZE > 0) {
                    newCap = hugeCapacity(cap + 1);
                }
                r = Arrays.copyOf(r, newCap);
            }
            r[i++] = (T) it.next();
        }
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    private static int hugeCapacity(int minCapacity){
        if (minCapacity < 0){
            throw new OutOfMemoryError("Required array size too large");
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    //修改操作

    /**
     * 在集合尾部插入元素
     * @param e 待插入的元素
     * @return true则表示插入成功
     */
    @Override
    public boolean add(E e) {
        throw  new UnsupportedOperationException();
    }

    /**
     * 删除指定的元素的第一次出现的位置
     * @param o 指定的元素
     * @return true则表示修改成功
     */
    @Override
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o == null){
            while (it.hasNext()){
                if (it.next() == null){
                    it.remove();
                    return true;
                }
            }
        }else{
            while (it.hasNext()){
                if (o.equals(it.next())){
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    // 批量操作

    /**
     * 判断此集合是否包含指定集合中所有的元素
     * @param c 指定的集合
     * @return true则表示此集合包含指定集合中的所有元素
     */
    @Override
    public boolean containsAll(MyCollection<?> c) {
        for (Object e : c){
            if (!contains(e)){
                return false;
            }
        }
        return true;
    }

    /**
     * 添加指定集合中的所有元素
     * @param c 指定的集合
     * @return true则表示修改成功
     */
    @Override
    public boolean addAll(MyCollection<? extends E> c) {
        boolean modified = false;
        for (E e : c){
            if (add(e)){
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 移除此集合中的元素其中这些元素出现在指定的集合中
     * @param c 指定的集合
     * @return true则表示修改成功
     */
    @Override
    public boolean removeAll(MyCollection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<?> it = iterator();
        while (it.hasNext()){
            if (c.contains(it.next())){
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 保留此集合中的元素，其中出现在指定集合中的元素
     * @param c 指定的元素
     * @return true则表示修改成功
     */
    @Override
    public boolean retainAll(MyCollection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()){
            if (!c.contains(it.next())){
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 清空集合
     */
    @Override
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()){
            it.next();
            it.remove();
        }
    }

    /**
     * 返回此集合的字符串表示形式
     * @return 此集合的字符串表示形式
     */
    @Override
    public String toString(){
        Iterator<E> it = iterator();
        if (! it.hasNext()){
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;){
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext()){
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }
}
