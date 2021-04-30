package cn.xiaohupao.list.arraylist;

import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import sun.misc.SharedSecrets;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;


/**
 * 学习ArrayList源码
 * @author xiaohupao
 * 实现Serializable,Cloneable,RandomAccess接口
 * 这三个接口都是标记接口
 */
public class MyArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1998102519960221L;

    /**
     * 默认初始化容量
     * 默认为10
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

    @Override
    public ListIterator<E> listIterator(int index){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return new ListItr(index);
    }

    @Override
    public ListIterator<E> listIterator(){
        return new ListItr(0);
    }

    @Override
    public Iterator<E> iterator(){
        return new Itr();
    }
    /**
     * ArrayList中的迭代器
     */
    private class Itr implements Iterator<E>{
        //下一个元素的索引位置
        int cursor;
        //上一个元素的索引位置
        int lastRet = -1;
        //预期被修改的次数
        int expectedModCount = modCount;

        Itr(){}

        /**
         * 判断是否还有下一个元素
         * @return true表示还有下一个元素
         */
        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size){
                throw new NoSuchElementException();
            }
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length){
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        @Override
        public void remove(){
            if (lastRet < 0){
                throw new IllegalArgumentException();
            }
            checkForComodification();

            try {
                //调用ArrayList中的删除方法
                MyArrayList.this.remove(lastRet);
                //将游标指向删除元素的位置
                cursor = lastRet;
                //lastRet恢复默认值-1
                lastRet = -1;
                //将expectedModCount值和modCount同步，因为删除后modCount会+1
                expectedModCount = modCount;
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void forEachRemaining(Consumer<? super E> consumer){
            Objects.requireNonNull(consumer);
            final int size = MyArrayList.this.size;
            int i = cursor;
            if (i >= size){
                return;
            }
            final Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length){
                throw new ConcurrentModificationException();
            }
            while (i != size && modCount == expectedModCount){
                consumer.accept((E) elementData[i++]);
            }

            cursor = i;
            lastRet = i - 1;
            checkForComodification();
        }

        /**
         * 不能在使用迭代器遍历的时候添加或删除元素，否则将抛出并发修改异常
         * 实现fail-fast机制
         */
        final void checkForComodification(){
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
        }
    }

    private class ListItr extends Itr implements ListIterator<E>{
        ListItr(int index){
            super();
            cursor = index;
        }

        /**
         * 判断当前元素前面是否有元素
         * @return
         */
        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        /**
         * 返回集合当前元素的前一个元素
         * 并将迭代器的位置移到前一个位置
         * @return 上一个元素
         */
        @SuppressWarnings("unchecked")
        @Override
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0){
                throw new NoSuchElementException();
            }
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length){
                throw new ConcurrentModificationException();
            }
            //更改迭代器的游标值
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        /**
         * 返回当前位置的迭代器位置
         * @return 迭代器的位置
         */
        @Override
        public int nextIndex() {
            return cursor;
        }

        /**
         * 返回当前元素的前一个元素位置
         * @return 当前迭代器前一个位置
         */
        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        /**
         * 利用迭代器更新元素
         * @param e 要设置的元素
         */
        @Override
        public void set(E e) {
            if (lastRet < 0){
                throw new IllegalStateException();
            }
            checkForComodification();

            try {
                MyArrayList.this.set(lastRet, e);
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }

        /**
         * 利用迭代器向集合中添加元素
         * @param e 要添加的元素
         */
        @Override
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                MyArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            }catch (IndexOutOfBoundsException ex){
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * 根据起始索引和结尾索引，获得到一个子序列
     * @param fromIndex 起始索引
     * @param toIndex 结尾索引
     * @return 子序列
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex){
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    /**
     * 检查传入索引位置的正确性
     * @param fromIndex 起始索引
     * @param toIndex 结尾索引
     * @param size 元素的个数
     */
    static void subListRangeCheck(int fromIndex, int toIndex, int size){
        if (fromIndex < 0){
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        }
        if (toIndex > size){
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        }

        if (fromIndex > toIndex){
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
        }
    }

    /**
     * 子序列内部类
     * 继承AbstractList
     * 实现RandomAccess(随机访问)接口
     */
    private class SubList extends AbstractList<E> implements RandomAccess{
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        /**
         * 子序列的构造方法
         * @param parent 父类
         * @param offset 偏移量
         * @param fromIndex 起始索引
         * @param toIndex 结尾索引
         */
        SubList(AbstractList<E> parent, int offset, int fromIndex, int toIndex){
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = MyArrayList.this.modCount;
        }

        @Override
        public E set(int index, E e){
            rangeCheck(index);
            checkForComodification();
            E oldValue = MyArrayList.this.elementData(offset + index);
            MyArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        @Override
        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return MyArrayList.this.elementData(offset + index);
        }

        @Override
        public int size() {
            checkForComodification();
            return this.size;
        }

        @Override
        public void add(int index, E e){
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        @Override
        public E remove(int index){
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex){
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex, parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }

        @Override
        public boolean addAll(Collection<? extends E> c){
            return addAll(this.size, c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c){
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize == 0){
                return false;
            }
            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex){
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index){
            if (index < 0 || index >= this.size){
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
            }
        }

        private void rangeCheckForAdd(int index){
            if (index < 0 || index > this.size){
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
            }
        }

        private String outOfBoundsMsg(int index){
            return "Index: " + index + ", Size: " + this.size;
        }

        private void checkForComodification(){
            if (MyArrayList.this.modCount != this.modCount){
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public Spliterator<E> spliterator(){
            checkForComodification();
            return new ArrListSpliterator<E>(MyArrayList.this, offset, offset + this.size, this.modCount);
        }
    }


}
