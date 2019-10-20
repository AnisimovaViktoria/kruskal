import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static int[] arr;
    private static int countOfV;
    private static int[][] matrix;
    private static int[] tree_id;
    private static int cost = 0;
    private static int[][] result;

    //поиск количества вершин
    private static int findCountOfV(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == arr.length) {
                return i;
            }
        }
        return 0;
    }

    //читает входные данные
    public static void readGraph() throws FileNotFoundException {
        FileReader reader = new FileReader("in.txt");
        Scanner sc = new Scanner(reader);
        arr = new int[sc.nextInt()];
        sc.nextLine();
        int i = 0;
        while (sc.hasNextLine()) {
            while (sc.hasNextInt()) {
                arr[i] = sc.nextInt();
                ++i;
            }
        }
        countOfV = findCountOfV(arr);
        matrix = new int[(arr.length - countOfV - 1) / 2][3];
        tree_id = new int[countOfV];
    }

    //переводит массив смежности в матрицу ребер вида (начало ребра, конец ребра, вес)
    private static void convert(int[] arr) {
        int k = 0;
        for (int i = 0; i < countOfV; i++) {
            for (int j = arr[i] - 1; j < arr[i + 1] - 2; j += 2) {
                matrix[k] = new int[]{i + 1, arr[j], arr[j + 1]};
                k++;
            }
            tree_id[i] = i;
        }
    }

    //сортировка по возрастанию весов ребер
    private static void sortMatrix() {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length - 1; j++) {
                if (matrix[j][2] > matrix[j + 1][2]) {
                    int[] var = matrix[j + 1];
                    matrix[j + 1] = matrix[j];
                    matrix[j] = var;
                }
            }
    }

    //сортировка по возрастанию смежных вершин
    private static void sortResult() {
        for (int i=0;i<result.length;i++)
            for (int j=0;j<result.length-1;j++) {
                if (result[i][j]>result[i][j+1]) {
                    int var = result[i][j+1];
                    result[i][j+1]=result[i][j];
                    result[i][j]=var;
                }
            }
    }

    //алгоритм борувки краскала
    private static void kruskal() {
        result = new int[countOfV][countOfV];
        for (int i = 0; i < matrix.length; i++) {
            int a = matrix[i][0] - 1;
            int b = matrix[i][1] - 1;
            int l = matrix[i][2];
            if (tree_id[a] != tree_id[b]) {
                cost += l;
                writeInResult(a, b);
                writeInResult(b, a);
                int old_id = tree_id[b];
                int new_id = tree_id[a];
                for (int j = 0; j < countOfV; j++) {
                    if (tree_id[j] == old_id)
                        tree_id[j] = new_id;
                }
            }
        }
    }

    //добавление к результату смежных вершин с данной
    private static void writeInResult(int v1, int v2) {
        for (int i = 0; i < countOfV; i++) {
            if (result[v1][i] == 0) {
                result[v1][i] = v2 + 1;
                break;
            }
        }
    }

    private static void print() throws IOException {
        FileWriter writer = new FileWriter("out.txt");
        for (int i = 0; i < result.length; i++)
            for (int j = 0; j < result.length; j++) {
                if (result[i][j] != 0)
                    writer.write(result[i][j] + " ");
                if (j == result.length - 1)
                    writer.write("0\n");
            }
        writer.write(cost+"");
        writer.flush();
    }

    public static void main(String[] args) throws IOException {
        readGraph();
        convert(arr);
        sortMatrix();
        kruskal();
        sortResult();
        print();
    }
}
