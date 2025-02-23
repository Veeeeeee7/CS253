import java.io.*;
import java.util.StringTokenizer;

public class Solution {

    public static class Pair {
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    public static int[] computeL(int[] A) {
        int[] L = new int[A.length];
        Pair[] aux = new Pair[A.length];

        Pair[] pairs = new Pair[A.length];
        for (int i = 0; i < A.length; i++) {
            pairs[i] = new Pair(A[i], i);
        }
        count(pairs, aux, 0, A.length - 1, L);
        return L;

    }

    private static void count(Pair[] A, Pair[] aux, int lo, int hi, int[] L) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        count(A, aux, lo, mid, L);
        count(A, aux, mid + 1, hi, L);
        merge(A, aux, lo, mid, hi, L);
    }

    private static void merge(Pair[] A, Pair[] aux, int lo, int mid, int hi, int[] L) {
        int i = lo, j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            aux[k] = A[k];
        }

        for (int k = lo; k <= hi; k++) {
            if (i > mid)
                A[k] = aux[j++];
            else if (j > hi)
                A[k] = aux[i++];
            else if (aux[j].first < aux[i].first) {
                L[aux[j].second] += (mid - i + 1);
                A[k] = aux[j++];
            } else
                A[k] = aux[i++];
        }
    }

    // This main() handles the input and output in a fast buffered
    // way, you DO NOT need to modify main().
    public static void main(String[] args) throws IOException {
        // Read input array A. We avoid java.util.Scanner, for speed.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine()); // first line
        int[] A = new int[N];
        StringTokenizer st = new StringTokenizer(br.readLine()); // second line
        for (int i = 0; i < N; ++i)
            A[i] = Integer.parseInt(st.nextToken());

        // Solve the problem!
        int[] L = computeL(A);

        // Print the output array L.
        PrintWriter out = new PrintWriter(System.out);
        out.print(L[0]);
        for (int i = 1; i < N; ++i)
            out.print(" " + L[i]);
        out.close();
    }
}