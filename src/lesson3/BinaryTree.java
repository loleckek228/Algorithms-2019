package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @Override
    public boolean remove(Object o) {

        if (find((T) o) == null) return false;

        root = removeNode(root, (T) o);
        size--;

        return true;
    }

    private Node<T> removeNode(Node<T> node, T value) {

        if (node == null) return null;

        int comparator = value.compareTo(node.value);

        if (comparator < 0) node.left = removeNode(node.left, value);
        else if (comparator > 0) node.right = removeNode(node.right, value);
        else if (node.left != null && node.right != null) {
            Node<T> newNode = new Node<T>(min(node.right));
            newNode.left = node.left;
            newNode.right = node.right;
            node = newNode;
            node.right = removeNode(node.right, node.value);
        } else {
            if (node.left != null) return node.left;
            else return node.right;
        }

        return node;
    }

    private T min(Node<T> node) {

        while (node.left != null) node = node.left;

        return node.value;
    }


    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Stack<Node<T>> stack = new Stack<>();
        private Node<T> current = root;

        private BinaryTreeIterator() {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        @Override
        public boolean hasNext() {return !stack.isEmpty();}


        /**
         * Поиск следующего элемента
         * Средняя
         */
        @Override
        public T next() {

            current = stack.pop();

            Node<T> node = current;

            if (node != null) {
                node = node.right;
            }
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            BinaryTree.this.remove(current.value);
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SubTree(this, fromElement, toElement);
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SubTree(this, null, toElement);
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SubTree(this, fromElement, null);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public class SubTree extends BinaryTree<T> {

        private BinaryTree<T> tree;

        T fromElement, toElement;

        public SubTree(BinaryTree<T> tree, T fromElement, T toElement) {
            this.tree = tree;
            this.fromElement = fromElement;
            this.toElement = toElement;
        }


        public boolean sub(T element) {

            if (fromElement != null && toElement != null)
                return element.compareTo(fromElement) >= 0 && element.compareTo(toElement) < 0;
            else if (fromElement == null) return element.compareTo(toElement) < 0;
            else return element.compareTo(fromElement) >= 0;
        }

        @Override
        public boolean add(T t) {

            if (sub(t)) return tree.add(t);

            throw new IllegalArgumentException();
        }

        @Override
        public boolean contains(Object o) {
            if (sub((T) o)) return tree.contains(o);

            return false;
        }

        @Override
        public int size() {
            return (int) tree.stream().filter(this::sub).count();
        }

    }
}
