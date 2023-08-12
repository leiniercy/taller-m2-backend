package tallerM2.tallerM2.utils;

public class SegmentTree {
    private int[] tree;

    public SegmentTree(int[] arr) {
        tree = new int[arr.length * 4];
        build(arr, 1, 0, arr.length - 1);
    }

    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, node * 2, start, mid);
            build(arr, node * 2 + 1, mid + 1, end);
            tree[node] = tree[node * 2] + tree[node * 2 + 1];
        }
    }

    public int query(int l, int r) {
        return query(1, 0, tree.length / 4 - 1, l, r);
    }

    private int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return 0;
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        return query(node * 2, start, mid, l, r) + query(node * 2 + 1, mid + 1, end, l, r);
    }
}
