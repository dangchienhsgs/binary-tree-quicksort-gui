public class HeapNode {
    public int value;
    int position; // Khoang cach tu thang con trai nhat den node
    int distict;
    int level;
    public HeapNode(int value){
        this.value=value;
    }
    public Line lineConnectParent;
    HeapNode parent;
    HeapNode left;
    HeapNode right;
    Node node;
}
