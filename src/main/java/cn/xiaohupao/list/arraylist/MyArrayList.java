package cn.xiaohupao.list.arraylist;

import lombok.NonNull;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import sun.misc.SharedSecrets;

import java.io.Serializable;
import java.util.*;


/**
 * 学习ArrayList源码
 * @author xiaohupao
 */
public class MyArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1998102519960221L;

    /**
     * 默认初始化容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 用于空Arraylist的共享数组
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 用于无参构造的共享空数组
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * Arraylist中实际存储元素的数组
     * 声明为transient，将不会被自动序列化，而是通过自定义的序列化方式
     */
    transient Object[] elementData;

    /**
     * ArrayList中存储元素的个数
     */
    private int size;

    /**
     * 空参构造
     */
    public MyArrayList(){
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 通过指定容量的构造方法
     * @param initialCapacity 指定容量的大小
     */
    public MyArrayList(@Range(from = 0, to = Integer.MAX_VALUE) int initialCapacity){
        if (initialCapacity > 0){
            this.elementData = new Object[initialCapacity];
        }else if(initialCapacity == 0){
            this.elementData = EMPTY_ELEMENTDATA;
        }else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    /**
     * 通过指定的collection来创建ArrayList
     * @param c 要将元素放入改list的集合
     */
    public MyArrayList(@NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Collection<? extends E> c){
        Object[] a = c.toArray();
        if ((size = a.length) != 0){
            if (c.getClass() == MyArrayList.class){
                elementData = a;
            }else{
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        }else{
            elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * 将ArrayList中的容量大小变为list中实际的元素个数大小
     */
    public void trimToSize(){
        modCount++;
        if (size < elementData.length){
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    /**
     * 确保容量的大小能够存储期望的最小容量值
     * @param minCapacity 所需最小的容量
     */
    public void ensureCapacity(int minCapacity){
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                ? 0
                : DEFAULT_CAPACITY;

        if (minCapacity > minExpand){
            ensureExplicitCapacity(minCapacity);
        }
    }

    /**
     * 计算容量的大小
     * @param elementData 存储元素的数组
     * @param minCapacity 所需最小的容量
     * @return 所需最小的容量
     */
    private static int calculateCapacity(Object[] elementData, int minCapacity){
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA){
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    private void ensureCapacityInternal(int minCapacity){
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }


    private void ensureExplicitCapacity(int minCapacity){
        modCount++;

        if (minCapacity - elementData.length > 0){
            grow(minCapacity);
        }
    }

    /**
     * 要分配数组的最大的大小
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 扩容的真正方法
     * @param minCapacity 所需最小的容量大小
     */
    private void grow(int minCapacity){
        int oldCapacity = elementData.length;
        //扩容中新的数组大小为原来的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0){
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0){
            newCapacity = hugeCapacity(minCapacity);
        }
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity){
        //判断是否溢出
        if (minCapacity < 0){
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    /**
     * list中元素的个数
     * @return list中元素的个数
     */
    @Override
    public int size(){
        return size;
    }

    /**
     * 判断list是否为空
     * @return true标识集合为空
     */
    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * 判断list是否包含指定元素
     * @param o 指定元素
     * @return true标识list中包含了指定的元素
     */
    @Override
    public boolean contains(Object o){
        return indexOf(o) >= 0;
    }

    /**
     * 找到指定元素的第一次出现的索引位置
     * @param o 指定元素
     * @return 指定元素第一次出现的索引位置
     */
    @Override
    public int indexOf(Object o){
        if (o == null){
            for (int i = 0; i < size; i++){
                if (elementData[i] == null){
                    return i;
                }
            }
        }else{
            for (int i = 0; i < size; i++){
                if (o.equals(elementData[i])){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 找到指定元素最后一次出现的索引位置
     * @param o 指定的元素
     * @return 指定元素最后一次出现的索引位置
     */
    @Override
    public int lastIndexOf(Object o){
        if (o == null){
            for (int i = size-1; i >= 0; i--){
                if (elementData[i] == null){
                    return i;
                }
            }
        }else{
            for (int i = size-1; i >= 0; i--){
                if (o.equals(elementData[i])){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * clone方法，返回ArrayList实力的浅克隆
     * @return ArrayList实例的一个副本
     */
    @Override
    public Object clone(){
        try {
            MyArrayList<?> v = (MyArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        }catch (CloneNotSupportedException e){
            throw new InternalError(e);
        }
    }

    /**
     * 将ArrayList转为一个object数组
     * @return
     */
    @Override
    public Object[] toArray(){
        return Arrays.copyOf(elementData, size);
    }

    /**
     * 将ArrayList中的元素转为一个传入数组的类型
     * 若传入数组的容量小于元素的个数则返回一个新的数组；
     * 否则将元素拷贝到传入的数组中。
     * @param a 传入的指定类型数组
     * @param <T> 元素的类型
     * @return 以数组的类型返回list中的元素
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a){
        if (a.length < size){
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size){
            a[size] = null;
        }
        return a;
    }

    /**
     * 返回指定索引位置的元素
     * @param index 指定的索引
     * @return 指定索引的元素
     */
    @SuppressWarnings("unchecked")
    E elementData(int index){
        return (E) elementData[index];
    }

    /**
     * 获取指定索引处的元素
     * @param index 指定的索引
     * @return 指定索引位置元素
     */
    @Override
    public E get(int index){
        rangeCheck(index);

        return elementData(index);
    }

    /**
     * 在指定索引位置跟新元素，并返回旧的元素
     * @param index 指定索引
     * @param element 插入元素
     * @return 返回旧的元素
     */
    @Override
    public E set(int index, E element){
        rangeCheck(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /**
     * 在最后插入一个元素
     * @param e 插入的元素
     * @return 若为true则表示插入成功
     */
    @Override
    public boolean add(E e){
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    /**
     * 在指定索引位置上插入元素
     * @param index 指定索引
     * @param element 待插入元素
     */
    @Override
    public void add(int index, E element){
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * 删除指定索引位置上的元素
     * @param index 指定索引
     * @return 返回删除的元素
     */
    @Override
    public E remove(int index){
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if (numMoved > 0){
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
        return oldValue;
    }

    /**
     * 删除指定元素
     * @param o 指定元素
     * @return true则为删除成功
     */
    @Override
    public boolean remove(Object o){
        if (o == null){
            for (int index = 0; index < size; index++){
                if (elementData[index] == null){
                    fastRemove(index);
                    return true;
                }
            }
        }else {
            for (int index = 0; index < size; index++){
                if (o.equals(elementData[index])){
                    fastRemove(index);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除指定索引位置上的元素
     * @param index 指定的索引
     */
    private void fastRemove(int index){
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0){
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
    }

    /**
     * 清空list
     */
    @Override
    public void clear(){
        modCount++;

        for (int i = 0; i < size ; i++) {
            elementData[i] = null;
        }

        size = 0;
    }

    /**
     * 将所指定的集合中的元素添加到list的末尾中
     * @param c 指定要添加的集合
     * @return 返回true则表示添加成功
     */
    @Override
    public boolean addAll(Collection<? extends E> c){
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * 在指定索引位置将指定集合中的元素加入到list中
     * @param index 指定的索引
     * @param c 指定的集合
     * @return 若为true则添加成功
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c){
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0){
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }
        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * 删除指定索引区间中的元素
     * @param fromIndex 开始的索引
     * @param toIndex 结束的索引
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex){
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);

        int newSize = size - (toIndex - fromIndex);
        for (int i = newSize; i < size; i++){
            elementData[i] = null;
        }
        size = newSize;
    }

    /**
     * add和addAll使用的检查索引规则
     * @param index 传入要检查 的索引
     */
    private void rangeCheckForAdd(int index){
        if (index > size || index < 0){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    /**
     * 检查索引规则
     * @param index 传入要检查的索引
     */
    private void rangeCheck(int index){
        if (index >= size){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    /**
     * 构造一个索引异常的信息
     * @param index 传入检查的索引
     * @return 异常信息
     */
    private String outOfBoundsMsg(int index){
        return "Index: " + index + ", Size: " + size;
    }

    /**
     * 移除与传入集合中元素相同的元素
     * @param c 传入的集合
     * @return true为操作成功
     */
    @Override
    public boolean removeAll(Collection<?> c){
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    /**
     * 保留与传入集合中元素相同的元素
     * @param c 传入的集合
     * @return true为操作成功
     */
    @Override
    public boolean retainAll(Collection<?> c){
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    /**
     * 移除与传入集合中元素相同的元素
     * @param c 传入的集合
     * @param complement 用于操作保留还是移除与传入集合相同元素的开关
     * @return true为操作成功
     */
    private boolean batchRemove(Collection<?> c, boolean complement){
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        //把需要移除的元素全部替换掉，不要移除的元素前移
        try {
            for (; r < size; r++){
                if (c.contains(elementData[r]) == complement){
                    elementData[w++] = elementData[r];
                }
            }
        }finally {
            if ( r != size){
                System.arraycopy(elementData, r, elementData, w, size - r);
                w += size - r;
            }
            if (w != size){
                for (int i = w; i < size; i++){
                    elementData[i] = null;
                }
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    /**
     * 将ArrayList实例的状态保存到流
     * @param s 序列化流
     * @throws java.io.IOException
     */
    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
        int expectedModCount = modCount;
        s.defaultWriteObject();

        s.writeInt(size);

        for (int i = 0; i < size; i++){
            s.writeObject(elementData[i]);
        }

        if (modCount != expectedModCount){
            throw new ConcurrentModificationException();
        }
    }

    /**
     * 将流读取成为一个对象，反序列化
     * @param s 读取序列化流
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException{
        elementData = EMPTY_ELEMENTDATA;

        //读取大小，以及隐藏的内容
        s.defaultReadObject();
        //读取容量
        s.readInt();

        if (size > 0){
            int capacity = calculateCapacity(elementData, size);
            SharedSecrets.getJavaOISAccess().checkArray(s, Object[].class, capacity);
            ensureCapacityInternal(size);

            Object[] a = elementData;

            for (int i = 0; i < size; i++){
                a[i] = s.readObject();
            }
        }
    }

}
