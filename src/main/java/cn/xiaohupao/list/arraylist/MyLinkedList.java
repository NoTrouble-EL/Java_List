package cn.xiaohupao.list.arraylist;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * @Author: xiaohupao
 * @Date: 2021/5/11 14:03
 */
public class MyLinkedList<E> extends MyAbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, Serializable {

    /**
     * 元素的个数
     */
    transient int size = 0;

    /**
     *链表的头节点
     */
    transient Node<E> first;
    /**
     * 链表的尾节点
     */
    transient Node<E> last;

    /**
     * 无参构造
     */
    public MyLinkedList(){
    }

    /**
     * 通过给定的集合构造一个链表
     * @param c 集合
     */
    public MyLinkedList(Collection<? extends E> c){
        this();
        addAll(c);
    }

    /**
     * 使用对应参数作为第一个节点，头插法
     * @param e 插入的元素
     */
    private void linkFirst(E e){
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        //若原链表为空，则尾节点为插入结点；否则，头插该元素
        if (f == null){
            last = newNode;
        }else{
            f.prev = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * 使用对应参数作为尾结点，尾插法
     * @param e 插入的元素
     */
    void linkLast(E e){
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        //若原链表为空，则头节点为插入结点；否则，尾插该元素
        if (l == null){
            first = newNode;
        }else{
            l.next = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * 在指定结点前插入结点
     * @param e 插入的元素
     * @param succ 指定的结点
     */
    void linkBefore(E e, Node<E> succ){
        //获取指定结点的前一个结点
        final Node<E> pred = succ.prev;
        final  Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        //若指定的结点为首结点，则头指针为新的结点
        if (pred == null){
            first = newNode;
        }else{
            pred.next = newNode;
        }
        size++;
        modCount++;
    }

    /**
     * 删除首结点，并返回首结点的元素
     * @param f 给定链表的头结点
     * @return 首结点的元素
     */
    private E unlinkFirst(Node<E> f){
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.next = null;
        first = next;
        //若链表只有一个元素，则last指针指向null；否则将下一个元素的prev指向null
        if (next == null){
            last = null;
        }else{
            next.prev = null;
        }
        size--;
        modCount++;
        return element;
    }

    /**
     * 删除尾结点
     * @param l 尾结点
     * @return 尾结点上的元素值
     */
    private E unlinkLast(Node<E> l){
        final E element = l.item;
        final Node<E> prev = l.prev;
        l.item = null;
        l.prev = null;
        last = prev;
        //若尾结点的前一个结点为null，则将首结点指向null
        if (prev == null){
            first = null;
        }else{
            prev.next = null;
        }
        size--;
        modCount++;
        return element;
    }

    /**
     * 删除指定结点x并返回删除结点中的值
     * @param x 指定的结点
     * @return 删除结点的值
     */
    E unlink(Node<E> x){
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        //若删除的是首结点，则头结点指向下一个结点；否则前一个结点的next指向后一个结点，将当前结点的prev指向空
        if (prev == null){
            first = next;
        }else {
            prev.next = next;
            x.prev = null;
        }
        //若删除的是尾结点，则尾结点指向前一个结点；否则下一个结点的prev指向前一个结点，将当前结点的next指向空
        if (next == null){
            last = prev;
        }else{
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        modCount++;
        return element;
    }

    /**
     * 获取首结点中的元素
     * @return 首结点中的元素值
     */
    @Override
    public E getFirst(){
        final Node<E> f = first;
        if (f == null){
            throw new NoSuchElementException();
        }
        return f.item;
    }

    /**
     * 获取尾结点的元素值
     * @return 尾结点中的元素值
     */
    @Override
    public E getLast(){
        final Node<E> l = last;
        if (l == null){
            throw new NoSuchElementException();
        }
        return l.item;
    }

    /**
     * 通过删除首结点的方法删除链表中的首结点
     * @return 删除首结点的元素值
     */
    @Override
    public E removeFirst(){
        final Node<E> f = first;
        if (f == null){
            throw new NoSuchElementException();
        }
        return unlinkFirst(f);
    }

    /**
     * 通过删除尾结点的方法删除链表中的尾结点
     * @return 删除尾结点的元素值
     */
    @Override
    public E removeLast(){
        final Node<E> l = last;
        if (l == null){
            throw new NoSuchElementException();
        }
        return unlinkLast(l);
    }

    /**
     * 通过头插法，在首结点前插入元素
     * @param e 插入的元素
     */
    @Override
    public void addFirst(E e) {
        linkFirst(e);
    }

    /**
     * 通过尾插法，在尾结点后插入元素
     * @param e 插入的元素
     */
    @Override
    public void addLast(E e) {
        linkLast(e);
    }

    /**
     * 判断此列表中是否包含指定的元素
     * @param o 指定的元素
     * @return true则表示
     */
    @Override
    public boolean contains(Object o){
        return indexOf(o) != -1;
    }

    /**
     * 返回链表中结点的个数
     * @return 链表中的结点个数
     */
    @Override
    public int size(){
        return size;
    }

    /**
     * 通过尾插法在尾结点后插入一个结点
     * @param e 要添加的元素
     * @return true则表示插入成功
     */
    @Override
    public boolean add(E e){
        linkLast(e);
        return true;
    }

    /**
     * 通过删除指定结点的方法，删除指定元素所在的结点
     * @param o 指定的元素
     * @return true则表示删除成功
     */
    @Override
    public boolean remove(Object o){
        if (o == null){
            for (Node<E> x = first; x != null; x = x.next){
                if (x.item == null){
                    unlink(x);
                    return true;
                }
            }
        }else {
            for (Node<E> x = first; x != null; x = x.next){
                if (o.equals(x.item)){
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在链表的尾部中添加指定集合中的元素
     * @param c 指定的集合
     * @return true则表示添加成功
     */
    @Override
    public boolean addAll(Collection<? extends E> c){
        return addAll(size, c);
    }

    /**
     * 在链表的指定索引中添加指定集合中的元素
     * @param index 指定的索引
     * @param c 指定的集合
     * @return true则表示添加成功
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c){
        checkPositionIndex(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0){
            return false;
        }

        //通过遍历找到需要插入位置的前一个位置和当前目标位置
        Node<E> pred, succ;
        if (index == size){
            succ = null;
            pred = last;
        }else{
            succ = node(index);
            pred = succ.prev;
        }

        //依次插入结点
        for (Object o : a){
            @SuppressWarnings("unchecked") E e = (E) o;
            Node<E> newNode = new Node<>(pred, e, null);
            //若是在首结点处插入，则将头结点指向第一个插入的结点
            if (pred == null){
                first = newNode;
            }else{
                //否则，将前一个结点的next指向新结点
                pred.next = newNode;
            }
            //将pred指向新插入的结点
            pred = newNode;
        }

        //若是在尾结点处插入则，尾指针指向最后一个插入的元素
        if (succ == null){
            last = pred;
        }else{
            //否则，将最后一个新插入的元素的next指向后一个结点
            pred.next = succ;
            //将后一个结点的prev指向最后一个插入的结点
            succ.prev = pred;
        }

        size += numNew;
        modCount++;
        return true;
    }

    /**
     * 清空链表
     */
    @Override
    public void clear(){
        for (Node<E> x = first; x != null;){
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }

        first = last = null;
        size = 0;
        modCount++;
    }

    //位置访问操作

    /**
     * 获取指定索引位置上的元素值
     * @param index 指定的索引
     * @return 元素的值
     */
    @Override
    public E get(int index){
        checkElementIndex(index);
        return node(index).item;
    }

    /**
     * 修改指定索引位置上的元素值
     * @param index 指定的元素
     * @param element 新的元素
     * @return 修改前的元素值
     */
    @Override
    public E set(int index, E element){
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    /**
     * 在指定的索引位置上添加元素
     * @param index 指定的索引位置
     * @param element 添加的元素
     */
    @Override
    public void add(int index, E element){
        checkPositionIndex(index);

        //若索引为元素的个数，即在尾结点处尾插元素
        if (index == size){
            linkLast(element);
        }else{
            //否则调用在指定结点前插元素的方法，用node(int index)获取指定索引的结点位置
            linkBefore(element, node(index));
        }
    }

    /**
     * 删除指定索引位置上的结点，并返回删除结点上的值
     * @param index 指定的索引位置
     * @return
     */
    @Override
    public E remove(int index){
        checkElementIndex(index);
        return unlink(node(index));
    }

    /**
     * 判断元素位置的索引是否合理
     * @param index 索引位置
     * @return true则表示位置合理
     */
    private boolean isElementIndex(int index){
        return index >= 0 && index < size;
    }

    /**
     * 判断元素插入的索引是否合理
     * @param index 索引位置
     * @return true则表示位置合理
     */
    private boolean isPositionIndex(int index){
        return index >= 0 && index <= size;
    }

    private String outOfBoundsMsg(int index){
        return "Index: " + index + "Size: " + size;
    }

    private void checkElementIndex(int index){
        if (!isElementIndex(index)){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private void checkPositionIndex(int index){
        if (!isPositionIndex(index)){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    /**
     * 通过遍历获取指定索引位置上结点位置
     * @param index 指定的索引
     * @return 指定索引位置上的结点
     */
    Node<E> node(int index){
        if (index < (size >> 1)){
            Node<E> x = first;
            for (int i = 0; i < index ; i++){
                x = x.next;
            }
            return x;
        }else{
            Node<E> x = last;
            for (int i = size - 1; i > index; i--){
                x = x.prev;
            }
            return x;
        }
    }

    //搜索操作

    /**
     * 返回指定元素第一次出现在链表中的索引位置，若没有则返回-1
     * @param o 指定的元素
     * @return 索引的位置
     */
    @Override
    public int indexOf(Object o){
        int index = 0;
        if (o == null){
            for (Node<E> x = first; x != null; x = x.next){
                if (x.item == null){
                    return index;
                }
                index++;
            }
        }else{
            for (Node<E> x = first; x != null; x = x.next){
                if (o.equals(x.item)){
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    /**
     * 返回指定元素最后一次出现在链表中的索引位置，若没有则返回-1
     * @param o 指定的元素
     * @return 索引的位置
     */
    @Override
    public int lastIndexOf(Object o){
        int index = size;
        if (o == null){
            for (Node<E> x = last; x != null; x = x.prev){
                index--;
                if (x.item == null){
                    return index;
                }
            }
        }else{
            for (Node<E> x = last; x != null; x = x.prev){
                index--;
                if (o.equals(x.item)){
                    return index;
                }
            }
        }
        return -1;
    }

    // 队列操作

    /**
     * 检索但并不删除链表的头结点
     * @return 此链表为空则返回null，否则返回链表的头结点上的元素
     */
    @Override
    public E peek(){
        final Node<E> f = first;
        return (f == null) ? null : f.item;
    }

    /**
     * 检索但不删除链表的头结点
     * @return 此链表的头
     */
    @Override
    public E element(){
        return getFirst();
    }

    /**
     * 弹出链表中的头结点的元素
     * @return 链表中头结点的元素
     */
    @Override
    public E poll(){
        final Node<E> f = first;
        return (f == null) ? null : unlinkFirst(f);
    }

    /**
     * 检索并删除链表中的头结点
     * @return 头结点中的元素
     */
    @Override
    public E remove(){
        return removeFirst();
    }

    /**
     * 将指定元素添加至链表的尾部
     * @param e 指定的元素
     * @return true则表示添加成功
     */
    @Override
    public boolean offer(E e){
        return add(e);
    }

    /**
     * 将指定的元素插入在链表的头部
     * @param e 指定的元素
     * @return true则表示成功插入
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * 将指定的元素插入在链表的尾部
     * @param e 指定的元素
     * @return true则表示插入成功
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    @Override
    public E pollFirst() {
        return null;
    }

    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E peekFirst() {
        return null;
    }

    @Override
    public E peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }


    @NotNull
    @Override
    public Iterator<E> descendingIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    /**
     * Node结点类
     * 双向链表
     * @param <E> 元素的类型
     */
    private static class Node<E>{
        /**
         * 存储元素
         */
        E item;

        /**
         * 下一结点
         */
        Node<E> next;

        /**
         * 上一结点
         */
        Node<E> prev;

        /**
         * 结点的构造器
         * @param prev 前一个结点
         * @param element 存储的元素
         * @param next 下一个结点
         */
        Node(Node<E> prev, E element, Node<E> next){
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
