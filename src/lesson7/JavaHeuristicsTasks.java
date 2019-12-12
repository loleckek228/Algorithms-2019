package lesson7;

import kotlin.NotImplementedError;
import lesson5.Graph;
import lesson5.Path;
import lesson6.knapsack.Fill;
import lesson6.knapsack.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.max;


// Примечание: в этом уроке достаточно решить одну задачу
@SuppressWarnings("unused")
public class JavaHeuristicsTasks {

    /**
     * Решить задачу о ранце (см. урок 6) любым эвристическим методом
     * <p>
     * Очень сложная
     * <p>
     * load - общая вместимость ранца, items - список предметов
     * <p>
     * Используйте parameters для передачи дополнительных параметров алгоритма
     * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
     */
    public static Fill fillKnapsackHeuristics(int load, List<Item> items, Object... parameters) {
        Set<Item> back = new HashSet<>();
        int maxWeght = Integer.MIN_VALUE;
        for (int i = 0; i < items.size(); i++) {
            if (maxWeght < items.get(i).getWeight()) {
                maxWeght = items.get(i).getWeight();
            }
        }

        int[][] cell = new int[items.size() + 1][maxWeght + 1];


        for (int i = 1; i < items.size(); i++) {
            for (int w = 1; w < maxWeght; w++) {
                if (items.get(i).getWeight() <= w) {
                    cell[i][w] = max(cell[i - 1][w],
                            items.get(i).getCost() + cell[i - 1][w - items.get(i).getWeight()]);

                } else {
                    cell[i][w] = cell[i - 1][w];
                }
            }
        }


        int maxCost = 0;
        int weight = maxWeght;
        for (int i = items.size() - 1; i > 0; i--) {
            if (cell[i][weight] != cell[i - 1][weight]) {
                back.add(items.get(i));
                maxCost += items.get(i).getCost();
                weight -= items.get(i).getWeight();
            }
        }

        return new Fill(maxCost, back);
    }

    /**
     * Решить задачу коммивояжёра (см. урок 5) методом колонии муравьёв
     * или любым другим эвристическим методом, кроме генетического и имитации отжига
     * (этими двумя методами задача уже решена в под-пакетах annealing & genetic).
     * <p>
     * Очень сложная
     * <p>
     * Граф передаётся через параметр graph
     * <p>
     * Используйте parameters для передачи дополнительных параметров алгоритма
     * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
     */
    public static Path findVoyagingPathHeuristics(Graph graph, Object... parameters) {
        throw new NotImplementedError();
    }
}
