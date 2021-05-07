package cn.xiaohupao.list.arraylist;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 学习阅读AbstractList
 * @author xiaohupao
 * @date 2021/5/1
 */
public abstract class MyAbstractList<E> extends AbstractCollection<E> implements MyList<E> {

    /**
     * 无参构造器
     */
    protected MyAbstractList(){}

    /**
     * 在list的尾部添加元素
     * @param e 要添加的元素
     * @return true表示添加成功
     */
    @Override
    public boolean add(E e){
        add(size(), e);
        return true;
    }

    /**
     * 获取索引位置的元素
     * @param index 指定的索引
     * @return 指定索引处的元素
     */
    @Override
    abstract public E get(int index);

    /**
     * 在指定索引处修改元素
     * @param index 指定的元素
     * @param element 新的元素
     * @return 旧的元素
     */
    @Override
    public E set(int index, E element){
        throw new UnsupportedOperationException();
    }

    /**
     * 在指定的索引位置添加元素
     * @param index 指定的索引位置
     * @param element 添加的元素
     */
    @Override
    public void add(int index, E element){
        throw new UnsupportedOperationException();
    }

    /**
     * 删除指定索引位置的元素并返回
     * @param index 指定的索引位置
     * @return 删除的元素
     */
    @Override
    public E remove(int index){
        throw new UnsupportedOperationException();
    }

    /**
     * 通过迭代器找到指定元素的第一次索引位置
     * @param o 指定的元素
     * @return 指定元素所在的索引位置
     */
    @Override
    public int indexOf(Object o){
        ListIterator<E> it = listIterator();
        if (o == null){
            while (it.hasNext()){
                if (it.next() == null){
                    return it.previousIndex();
                }
            }
        }else{
            while (it.hasNext()){
                if (o.equals(it.next())){
                    return it.previousIndex();
                }
            }
        }
        return -1;
    }

    /**
     * 通过迭代器找到指定元素最后一次的索引位置
     * @param o 指定的元素
     * @return 指定元素最后一所在的索引位置
     */
    @Override
    public int lastIndexOf(Object o){
        ListIterator<E> it = listIterator(size());
        if (o == null){
            while (it.hasPrevious()){
                if (it.previous() == null){
                    return it.nextIndex();
                }
            }
        }else{
            while (it.hasPrevious()){
                if (o.equals(it.previous())){
                    return it.nextIndex();
                }
            }
        }
        return -1;
    }

    /**
     * 清空list
     * 直接删除从索引0~元素个数(不包含)
     */
    @Override
    public void clear(){
        removeRange(0, size());
    }

    /**
     *  从指定的集合中添加元素到指定索引位置
     * @param index 指定的索引
     * @param c 指定的集合
     * @return true则表示修改成功
     */
    public boolean addAll(int index, MyCollection<? extends E> c){
        rangeCheckForAdd(index);
        boolean modified = false;
        for (E e : c){
            add(index++, e);
            modified = true;
        }
        return modified;
    }


    /**
     * 用于创建迭代器
     * @return 迭代器
     */
    @Override
    public Iterator<E> iterator(){
        return new Itr();
    }

    /**
     * 用于创建列表迭代器
     * @return 列表迭代器
     */
    @Override
    public ListIterator<E> listIterator(){
        return listIterator(0);
    }

    /**
     * 创建列表迭代器的真正方法
     * @param index 迭代器所在的索引位置
     * @return 列表迭代器
     */
    @Override
    public ListIterator<E> listIterator(final int index){
        rangeCheckForAdd(index);

        return new ListItr(index);
    }

    private class Itr implements Iterator<E>{
        /**
         * 用于在next中返回元素的索引
         * 表示下一个元素的索引位置
         */
        int cursor = 0;

        /**
         * 表示上一个元素的索引位置
         */
        int lastRet = -1;

        /**
         * 预期被修改的次数
         */
        int expectedModCount = modCount;

        /**
         * 判断是否还有下一个元素
         * @return true则表示还有下一个元素
         */
        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        /**
         *  获取迭代器的下一个元素
         * @return 迭代器cursor指向的元素
         */
        @Override
        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            }catch (IndexOutOfBoundsException e){
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        /**
         * 迭代器中中移除迭代器当前所在前一个元素
         */
        @Override
        public void remove(){
            if (lastRet < 0){
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                MyAbstractList.this.remove(lastRet);
                if (lastRet < cursor){
                    cursor--;
                }
                lastRet = -1;
                expectedModCount = modCount;
            }catch (IndexOutOfBoundsException e){
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification(){
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * 列表迭代器的类
     */
    private class ListItr extends Itr implements ListIterator<E>{
        ListItr(int index){
            cursor = index;
        }

        /**
         * 判断当前迭代器位置时是否存在上一个元素
         * @return true则便是存在上一个元素
         */
        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        /**
         * 返回迭代器所在位置时的上一个元素
         * @return 迭代器所在位置的上一个元素
         */
        @Override
        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            }catch (IndexOutOfBoundsException e){
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        /**
         * 迭代器所在位置的下一个元素的索引
         * @return 迭代器所在位置的下一个元素的索引
         */
        @Override
        public int nextIndex() {
            return cursor;
        }

        /**
         * 返回迭代器所在位置上一个元素的索引
         * @return 返回迭代器所在位置上一个元素的索引
         */
        @Override
        public int previousIndex() {
            return cursor-1;
        }

        /**
         * 在迭代器所在的位置上修改元素
         * @param e 修改的元素
         */
        @Override
        public void set(E e) {
            if (lastRet < 0){
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                MyAbstractList.this.set(lastRet ,e);
                expectedModCount = modCount;
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }

        /**
         * 在迭代器所在的位置添加元素
         * @param e 添加的元素
         */
        @Override
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                MyAbstractList.this.add(i, e);
                lastRet = -1;
                cursor = i + 1;
                expectedModCount = modCount;
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }
    }


    /**
     * 根据起始索引和结尾索引获取一个list子序列
     * @param fromIndex 起始索引
     * @param toIndex 结束索引
     * @return list子序列
     */
    @NotNull
    @Override
    public MyList<E> subList(int fromIndex, int toIndex) {
        return (this instanceof RandomAccess ?
                new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));
    }

    /**
     * 判断两个list是否相同
     * 通过使用列表迭代器遍历元素做判断
     * @param o 要比对的lsit
     * @return true则表示两个list中对象等价
     */
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof List)){
            return false;
        }

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()){
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2))){
                return false;
            }
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    /**
     * list中的哈希值
     * @return list的哈希值
     */
    @Override
    public int hashCode(){
        int hashCode = 1;
        for (E e : this){
            hashCode = 31*hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    /**
     * 移除从开始索引到结束索引位置上的元素(不包括结束索引)
     * @param fromIndex 开始索引
     * @param toIndex 结束索引
     */
    protected void removeRange(int fromIndex, int toIndex){
        ListIterator<E> it = listIterator(fromIndex);
        for (int i=0, n=toIndex-fromIndex; i<n; i++){
            it.next();
            it.remove();
        }
    }

    protected transient int modCount = 0;

    private void rangeCheckForAdd(int index){
        if (index < 0 || index > size()){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index){
        return "Index: " + index + ", Size: " + size();
    }
}

/**
 * 用于截取list中的元素作为子序列
 * @param <E> 子序列的范型
 */
class SubList<E> extends MyAbstractList<E>{
    /**
     * 传入的集合类
     */
    private final MyAbstractList<E> l;
    /**
     * 偏移量
     */
    private final int offset;
    /**
     * 截取的元素size
     */
    private int size;

    /**
     * 子序列的构造方法
     * @param list 传入的集合
     * @param fromIndex 其实索引
     * @param toIndex 结束索引
     */
    SubList(MyAbstractList<E> list, int fromIndex, int toIndex){
        if (fromIndex < 0){
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        }
        if (toIndex > list.size()){
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        }
        if (fromIndex > toIndex){
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
        }

        l = list;
        offset = fromIndex;
        size = toIndex - fromIndex;
        this.modCount = l.modCount;
    }

    /**
     * 修改指定索引处子序列的索引
     * @param index 指定的元素
     * @param element 新的元素
     * @return
     */
    @Override
    public E set(int index, E element){
        rangeCheck(index);
        checkForComodification();
        return l.set(index+offset, element);
    }

    /**
     * 获得子学列指定索引处的元素
     * @param index 指定的索引
     * @return 指定索引处的元素
     */
    @Override
    public E get(int index) {
        rangeCheck(index);
        checkForComodification();
        return l.get(index+offset);
    }

    /**
     * 子序列的元素个数
     * @return 子序列的元素个数
     */
    @Override
    public int size() {
        checkForComodification();
        return size;
    }

    /**
     * 在索引位置添加元素
     * @param index 指定的索引位置
     * @param element 添加的元素
     */
    @Override
    public void add(int index, E element){
        rangeCheckForAdd(index);
        checkForComodification();
        l.add(index+offset, element);
        this.modCount = l.modCount;
        size++;
    }

    /**
     * 移除指定索引位置的元素
     * @param index 指定的索引位置
     * @return
     */
    @Override
    public E remove(int index){
        rangeCheck(index);
        checkForComodification();
        E result = l.remove(index+offset);
        this.modCount = l.modCount;
        return result;
    }

    /**
     * 在子序列中移除从起始索引位置到结束位置的元素(不包括结束位置)
     * @param fromIndex 起始索引
     * @param toIndex 结束索引
     */
    @Override
    protected void removeRange(int fromIndex, int toIndex){
        checkForComodification();
        l.removeRange(fromIndex+offset, toIndex+offset);
        this.modCount = l.modCount;
        size -= (toIndex - fromIndex);
    }

    /**
     * 在子学列中添加传入集合中的元素
     * @param c 指定的集合
     * @return true则表示添加成功
     */
    public boolean addAll(MyCollection<? extends E> c){
        return addAll(size, c);
    }

    /**
     * 在子序列中在指定索引位置传入集合中的元素
     * @param index 指定的索引
     * @param c 指定的集合
     * @return
     */
    @Override
    public boolean addAll(int index, MyCollection<? extends E> c){
        rangeCheckForAdd(index);
        int cSize = c.size();
        if (cSize==0){
            return false;
        }
        checkForComodification();
        l.addAll(offset+index, c);
        this.modCount = l.modCount;
        size += cSize;
        return true;
    }

    /**
     * 获取列表迭代器
     * @return 列表迭代器
     */
    @Override
    public Iterator<E> iterator(){
        return listIterator();
    }

    /**
     * 在指定索引位置处获取列表迭代器
     * @param index 迭代器所在的索引位置
     * @return 列表迭代器
     */
    @Override
    public ListIterator<E> listIterator(final int index){
        checkForComodification();
        rangeCheckForAdd(index);

        return new ListIterator<E>() {
            private final ListIterator<E> i = l.listIterator(index+offset);

            /**
             * 判断是否还有下一个元素
             * @return true则表示还有下一个元素
             */
            @Override
            public boolean hasNext() {
                return nextIndex() < size;
            }

            /**
             * 获取下一个元素
             * @return 下一个元素
             */
            @Override
            public E next() {
                if (hasNext()) {
                    return i.next();
                }else {
                    throw new NoSuchElementException();
                }
            }

            /**
             * 判断是否还有上一个元素
             * @return true则表示还有上一个元素
             */
            @Override
            public boolean hasPrevious() {
                return previousIndex() >= 0;
            }

            /**
             * 获取迭代器前一个元素
             * @return 迭代器前一个元素
             */
            @Override
            public E previous() {
                if (hasPrevious()){
                    return i.previous();
                }else{
                    throw new NoSuchElementException();
                }
            }

            /**
             * 获取迭代器下一个索引位置
             * @return 下一个索引
             */
            @Override
            public int nextIndex() {
                return i.nextIndex() - offset;
            }

            /**
             * 获取迭代器上一个索引位置
             * @return 上一个索引位置
             */
            @Override
            public int previousIndex() {
                return i.previousIndex() - offset;
            }

            /**
             * 移除迭代器当前位置的元素
             */
            @Override
            public void remove() {
                i.remove();
                SubList.this.modCount = l.modCount;
                size--;
            }

            /**
             * 设置迭代器当前位置的元素
             * @param e 指定的元素
             */
            @Override
            public void set(E e) {
                i.set(e);
                SubList.this.modCount = l.modCount;
            }

            /**
             * 在迭代器当前位置添加元素
             * @param e 指定的元素
             */
            @Override
            public void add(E e) {
                i.add(e);
                SubList.this.modCount = l.modCount;
                size++;
            }
        };
    }
    @Override
    public MyList<E> subList(int fromIndex, int toIndex){
        return new SubList<>(this, fromIndex, toIndex);
    }

    private void rangeCheck(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private void rangeCheckForAdd(int index){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index){
        return "Index: " + index + ", Size: " + size;
    }

    private void checkForComodification(){
        if (this.modCount != l.modCount){
            throw new ConcurrentModificationException();
        }
    }
}

/**
 * 继承了SubList并实现了随机访问接口
 * @param <E> 范型的类型
 */
class RandomAccessSubList<E> extends SubList<E> implements RandomAccess{
    RandomAccessSubList(MyAbstractList<E> list, int fromIndex, int toIndex){
        super(list, fromIndex, toIndex);
    }
}
