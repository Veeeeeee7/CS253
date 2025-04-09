// THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
// A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - Victor Li

// As given, this is already a fast enough solution to the "Array Max"
// challenge on hackerrank.  However, we want a solution using less
// space, and that does not rely on java.util.* data structures.
// See the Canvas "code3" assignment for a more precise statement
// of constraints and advice.

// Reminders:
//   1. remember to put your name in the SPCA comment above.
//   2. read the Canvas "code3" assignment for more details.
//   3. remember to submit your link and paragraph on Canvas.

// This solution uses java.util.PriorityQueue, a binary min-heap.  It
// also uses buffered input and output, for speed.  Note there could
// be many "stale" entries in the queue.  So in the worst case this
// solution uses O(N) space and O(N lg N) time.

import java.io.*;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

public class ArrayMax {
    // We will maintain a min-PQ of Entry objects.
    // Each Entry (i,v) represents an assignment "A[i]=v".
    static class Entry implements Comparable<Entry> {
        int i, v;

        Entry(int i, int v) {
            this.i = i;
            this.v = v;
        }

        // We negate the "v" comparison, so that PriorityQueue (a MinPQ)
        // returns the Entry with the maximum v. We break ties with i,
        // so we can find the leftmost appearance of the maximum.
        public int compareTo(Entry that) {
            int dif = -(this.v - that.v);
            if (dif == 0) // break ties with the index i
                dif = this.i - that.i;
            return dif;
        }
    }

    public static void main(String[] args) throws IOException {
        // Buffered output (for faster printing)
        PrintWriter out = new PrintWriter(System.out);
        // Buffered input (we also avoid java.util.Scanner)
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int M = Integer.parseInt(st.nextToken()),
                N = Integer.parseInt(st.nextToken());
        int[] a = new int[M]; // initially all zero
        // Create MinPQ, and add an entry for a[0]=0 (that's all we need)
        IndexMinPQ pq = new IndexMinPQ(M);
        pq.insert(new Entry(0, 0));
        // Loop through the N assignment lines
        for (int n = 0; n < N; ++n) {
            // read the line, parse i and v
            st = new StringTokenizer(br.readLine());
            int i = Integer.parseInt(st.nextToken()),
                    v = Integer.parseInt(st.nextToken());
            // do the assignment in the array
            a[i] = v;
            // Add an Entry recording this assignment
            pq.insert(new Entry(i, v));
            // Get the head of the queue (Entry with maximum v value)
            Entry head = pq.minKey();
            // While the head is stale (no longer in the array), discard it.
            while (a[head.i] != head.v) {
                pq.delMin(); // discard it
                head = pq.minKey(); // the next head
            }
            // Report location of the largest value, a[head.i]==head.v
            out.println(head.i);
        }
        out.close();
    }

    static class IndexMinPQ {
        private int maxN; // maximum number of elements on PQ
        private int n; // number of elements on PQ
        private int[] pq; // binary heap using 1-based indexing
        private int[] qp; // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
        private Entry[] entries; // keys[i] = priority of i

        public IndexMinPQ(int maxN) {
            if (maxN < 0)
                throw new IllegalArgumentException();
            this.maxN = maxN;
            n = 0;
            entries = new Entry[maxN + 1];
            pq = new int[maxN + 1];
            qp = new int[maxN + 1];
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }

        public boolean isEmpty() {
            return n == 0;
        }

        public boolean contains(int i) {
            validateIndex(i);
            return qp[i] != -1;
        }

        public int size() {
            return n;
        }

        public void insert(Entry entry) {
            int i = entry.i;
            int v = entry.v;
            validateIndex(i);
            if (contains(i)) {
                // throw new IllegalArgumentException("index is already in the priority queue");
                changeKey(entry);
            } else {
                n++;
                qp[i] = n;
                pq[n] = i;
                entries[i] = entry;
                swim(n);
            }
        }

        public int minIndex() {
            if (n == 0)
                throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        public Entry minKey() {
            if (n == 0)
                throw new NoSuchElementException("Priority queue underflow");
            return entries[pq[1]];
        }

        public int delMin() {
            if (n == 0)
                throw new NoSuchElementException("Priority queue underflow");
            int min = pq[1];
            exch(1, n--);
            sink(1);
            assert min == pq[n + 1];
            qp[min] = -1; // delete
            entries[min] = null; // to help with garbage collection
            pq[n + 1] = -1; // not needed
            return min;
        }

        public Entry keyOf(int i) {
            validateIndex(i);
            if (!contains(i))
                throw new NoSuchElementException("index is not in the priority queue");
            else
                return entries[i];
        }

        public void changeKey(Entry entry) {
            int i = entry.i;
            int v = entry.v;
            validateIndex(i);
            if (!contains(i))
                throw new NoSuchElementException("index is not in the priority queue");
            entries[i] = entry;
            swim(qp[i]);
            sink(qp[i]);
        }

        private void validateIndex(int i) {
            if (i < 0)
                throw new IllegalArgumentException("index is negative: " + i);
            if (i >= maxN)
                throw new IllegalArgumentException("index >= capacity: " + i);
        }

        /***************************************************************************
         * General helper functions.
         ***************************************************************************/
        private boolean greater(int i, int j) {
            return entries[pq[i]].compareTo(entries[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }

        /***************************************************************************
         * Heap helper functions.
         ***************************************************************************/
        private void swim(int k) {
            while (k > 1 && greater(k / 2, k)) {
                exch(k, k / 2);
                k = k / 2;
            }
        }

        private void sink(int k) {
            while (2 * k <= n) {
                int j = 2 * k;
                if (j < n && greater(j, j + 1))
                    j++;
                if (!greater(k, j))
                    break;
                exch(k, j);
                k = j;
            }
        }
    }
}