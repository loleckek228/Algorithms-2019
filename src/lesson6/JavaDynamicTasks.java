package lesson6;

import kotlin.NotImplementedError;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.max;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * сложность:  O(nm),память: O(nm)
     */
    public static String longestCommonSubSequence(String first, String second) {

        int[][] cell = new int[first.length() + 1][second.length() + 1];

        for (int f = first.length() - 1; f >= 0; f--) {
            for (int s = second.length() - 1; s >= 0; s--) {
                if (first.charAt(f) == second.charAt(s)) {
                    cell[f][s] = cell[f + 1][s + 1] + 1;
                } else {
                    cell[f][s] = max(cell[f + 1][s], cell[f][s + 1]);
                }
            }
        }

        StringBuilder lcs = new StringBuilder();

        int f = 0;
        int s = 0;

        while (f < first.length() && s < second.length()) {
            if (first.charAt(f) == second.charAt(s)) {
                lcs.append(first.charAt(f));
                f++;
                s++;
            } else if (cell[f + 1][s] > cell[f][s + 1]) {
                f++;
            } else {
                s++;
            }
        }
        return lcs.toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     * сложность: О(n^2), память: O(n), пытался сделать сделать за 0(n*log(n)), но не вышло
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {

        if (list.size() <= 1) {
            return list;
        }

        int[] subSequenceLength = new int[list.size()];
        int[] prev = new int[list.size()];
        for (int i = 0; i < subSequenceLength.length; i++) {
            subSequenceLength[i] = 1;
            prev[i] = -1;
        }


        for (int q = 1; q < list.size(); q++) {
            for (int w = 0; w < q; w++) {
                if (list.get(q) > list.get(w) && subSequenceLength[q] <= subSequenceLength[w]) {
                    subSequenceLength[q] = subSequenceLength[w] + 1;
                    prev[q] = w;
                }
            }
        }

        int maximum = 0;
        int pos = 0;

        for (int i = 0; i < list.size(); i++) {
            if (maximum < subSequenceLength[i]) {
                maximum = subSequenceLength[i];
                pos = i;
            }
        }

        List<Integer> result = new LinkedList<>();

        while (pos != -1) {
            result.add(list.get(pos));
            pos = prev[pos];
        }
        Collections.reverse(result);
        return result;
    }
        /*int maxIndex = 0;
        int listLength = -1;

        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Integer> minSequence = new ArrayList<>();
        ArrayList<Integer> subSequence = new ArrayList<>();

        minSequence.add(0, list.get(0));
        indexes.add(0, 0);

        for (int i = 1; i < list.size(); i++) {
            indexes.add(i, Integer.MAX_VALUE);
            minSequence.add(i, Integer.MAX_VALUE);
        }



        for (int i = 1; i < list.size(); ++i) {
            indexes.add(i, ceilIndex(minSequence, 0, i, list.get(i)));
            if (indexes.get(i) > listLength) {
                listLength = indexes.get(i);
            }
        }

        while (maxIndex <= listLength) {
            for (int i = 0; i < list.size(); i++) {
                if (indexes.get(i) == maxIndex) {
                    subSequence.add(list.get(i));
                    maxIndex++;
                }
            }
        }

        return subSequence;
    }

    public static int ceilIndex(ArrayList<Integer> subSequence, int startLeft, int startRight, int key) {
        int mid;
        int left = startLeft;
        int right = startRight;
        int ceilIndex = 0;
        boolean ceilIndexFound = false;


        for (mid = (left + right) / 2; left <= right && !ceilIndexFound; mid = (left + right) / 2) {
            if (subSequence.get(mid) > key) {
                right = mid - 1;
            } else if (subSequence.get(mid) == key) {
                ceilIndex = mid;
                ceilIndexFound = true;
            } else if (mid + 1 <= right && subSequence.get(mid + 1) >= key) {
                subSequence.add(mid + 1, key);
                ceilIndex = mid + 1;
                ceilIndexFound = true;
            } else {
                left = mid + 1;
            }
        }

        if (!ceilIndexFound) {
            if (mid == left) {
                subSequence.add(mid, key);
                ceilIndex = mid;
            } else {
                subSequence.add(mid + 1, key);
                ceilIndex = mid + 1;
            }
        }
        return ceilIndex;
    }*/

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
