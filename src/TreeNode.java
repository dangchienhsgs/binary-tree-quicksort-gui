public class TreeNode {
    Node node; // its node
    TreeNode parent; // its parent
    TreeNode left, right; // its left and right node
    private int value; // its value
    int position; // dung de tinh khoang cach cua node
    int level; // dung de tinh duong cao

    public TreeNode (int value){
        this.value=value;
        this.left=null;
        this.right=null;
        this.parent=null;
    }
    public void delete(){
        if ((parent.left!=null) && (parent.left.getValue()==value)) parent.left=null;
        if ((parent.right!=null) && (parent.right.getValue()==value)) parent.right=null;
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

