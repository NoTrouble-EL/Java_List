package cn.xiaohupao.list.arraylist;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @Author: xiaohupao
 * @Date: 2021/5/11 13:34
 */
public abstract class MyAbstractSequentialList<E> extends MyAbstractList<E>{
    /**
     * 无参构造
     */
    protected MyAbstractSequentialList(){
    }

    /**
     * 通过列表迭代器获取指定索引位置的元素
     * @param index 指定的索引
     * @return 指定索引位置上的元素
     */
    @Override
    public E get(int index) {
        try {
            return listIterator(index).next();
        }catch (NoSuchElementException exc){
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    /**
     * 通过列表迭代器修改指定索引位置上的元素
     * @param index 指定的元素
     * @param element 新的元素
     * @return 被替换元素的值
     */
    @Override
    public E set(int index, E element){
        try {
            ListIterator<E> e = listIterator(index);
            E oldVal = e.next();
            e.set(element);
            return oldVal;
        }catch (NoSuchElementException exc){
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    /**
     * 通过列表迭代器在指定的索引上添加元素
     * @param index 指定的索引位置
     * @param element 添加的元素
     */
    @Override
    public void add(int index, E element){
        try {
            listIterator(index).add(element);
        }catch (NoSuchElementException exc){
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    /**
     * 通过列表迭代器删除指定索引上的元素
     * @param index 指定的索引位置
     * @return 删除元素的值
     */
    @Override
    public E remove(int index){
        try {
            ListIterator<E> e = listIterator(index);
            E outCast = e.next();
            e.remove();
            return outCast;
        }catch (NoSuchElementException exc){
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    //批量操作

    /**
     * 通过列表迭代器添加元素，通过迭代器获取指定集合中的元素
     * @param index 指定的索引
     * @param c 指定的集合
     * @return true则表示修改成功(添加元素成功)
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c){
        try {
            boolean modified = false;
            ListIterator<E> e1 = listIterator(index);
            Iterator<? extends E> e2 = c.iterator();
            while (e2.hasNext()){
                e1.add(e2.next());
                modified = true;
            }
            return modified;
        }catch (NoSuchElementException exc){
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    //迭代器

    /**
     * 返回此列表中的元素的迭代器
     * @return 此列表中的迭代器
     */
    @Override
    public Iterator<E> iterator(){
        return listIterator();
    }

    /**
     * 返回此列表中元素的列表迭代器
     * @param index 迭代器所在的索引位置
     * @return 此列表中与那苏的列表迭代器
     */
    @Override
    public abstract ListIterator<E> listIterator(int index);
}
