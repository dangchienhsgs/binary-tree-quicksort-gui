public class HeapTree {
    int n;
    HeapNode heapNode[];
    HeapNode root;

    public int getHeight() {
        return (int) Math.log(n) + 1;
    }

    HeapNode left;
    public void getLeft(HeapNode treeNode){
        if (treeNode.left!=null) {
            left=treeNode.left;
            getLeft(treeNode.left);
        }

    }
    public HeapTree(int a[]) {
        this.heapNode = new HeapNode[a.length];
        n = a.length;
        // Gan gia tri
        for (int i = 0; i < a.length; i++) {
            this.heapNode[i] = new HeapNode(a[i]);
        }
        for (int i = 0; i < n / 2; i++) {
            if (2 * i + 1 < n) {
                heapNode[i].left = heapNode[2 * i + 1];
                heapNode[2 * i + 1].parent = heapNode[i];
            }
            if (2 * i + 2 < n) {
                heapNode[i].right = heapNode[2 * i + 2];
                heapNode[2 * i + 2].parent = heapNode[i];
            }
        }
        root = heapNode[0];
    }
    public void setLevel (HeapNode heapNode){
        if (heapNode == this.root) {
            heapNode.level=1;
        }
        if (heapNode.left != null) {
            heapNode.left.level=heapNode.level+1;
            setLevel(heapNode.left);
        }
        if (heapNode.right != null) {
            heapNode.right.level=heapNode.level+1;
            setLevel(heapNode.right);
        }
    }
    public void setDistict(HeapNode heapNode) {
        if (heapNode == this.root) {
            heapNode.distict = this.getHeight() + 1;
        }
        if (heapNode.left != null) {
            heapNode.left.distict = heapNode.distict - 1;
            System.out.println ("Left: "+heapNode.left.value+": "+heapNode.left.distict);
            setDistict(heapNode.left);
        }
        if (heapNode.right != null) {
            heapNode.right.distict = heapNode.distict - 1;
            System.out.println ("Right: "+heapNode.right.value+": "+heapNode.right.distict);
            setDistict(heapNode.right);
        }
    }

    public void setPostion(HeapNode heapNode) {
        if (heapNode == this.heapNode[0]) {
            heapNode.position = (heapNode.distict+2) * 100;
        }
        if (heapNode.left != null) {
            heapNode.left.position = heapNode.position - heapNode.distict * 100 / 2;
            setPostion(heapNode.left);
        }
        if (heapNode.right != null) {
            heapNode.right.position = heapNode.position + heapNode.distict * 100 / 2;
            setPostion(heapNode.right);
        }

    }

    public void printInfor(HeapNode heapNode) {
        if (heapNode != null) {
            System.out.println(heapNode.value + " " + heapNode.distict + " " + heapNode.position);

            if (heapNode.left != null) {
                printInfor(heapNode.left);
            }
            if (heapNode.right != null) {
                printInfor(heapNode.right);
            }
        }
    }

    public static void main(String args[]) {
        int a[] = new int[]{
                4, 1, 3, 6, 7, 8, 10,
        };
        HeapTree heapTree = new HeapTree(a);
        heapTree.setDistict(heapTree.root);
        heapTree.setPostion(heapTree.root);

        heapTree.printInfor(heapTree.heapNode[0]);
    }
}
