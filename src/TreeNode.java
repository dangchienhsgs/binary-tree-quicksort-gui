public class TreeNode {
    Node node;
    TreeNode left, right;
    private int value;
    int position; // dung de tinh khoang cach cua node

    public TreeNode (int value){
        this.value=value;
        this.left=null;
        this.right=null;
    }
    public void setValue(int value){
        this.value=value;
    }
    public int getValue(){
        return this.value;
    }
    public TreeNode getRightNode(){
        return this.right;
    }
    public void setRightNode(int value){
        this.right=new TreeNode(value);
    }
    public TreeNode getLeftNode(){
        return this.left;
    }
    public void setLeftNode(int value){
        this.left=new TreeNode(value);
    }
}

