package cn.xiaohupao.list.arraylist;

import lombok.NonNull;

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
    public MyArrayList(int initialCapacity){
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
    public MyArrayList(Collection<? extends E> c){
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
    @Override
    public E get(int index) {
        return null;
    }

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

}
