/**
 * Created by dangchienhsgs on 04/05/2014.
 */
public class Tree {
    TreeNode root;
    private int width;
    int height;
    // Tao the first node
    public void makeRoot (int value){
        this.root=new TreeNode(value);
        root.position=0;
    }
    // add a node
    public int getHeight(TreeNode node){
        if ((node.left==null) && (node.right==null)){
            return 1;
        } else if (node.left==null){
            return 1+getHeight(node.right);
        } else if (node.right==null){
            return 1+getHeight(node.left);
        } else {
            return Math.max(1+getHeight(node.right), 1+getHeight(node.left));
        }
    }
    public void addNode (int value, TreeNode node){
        if (node.getValue()<value){
            if (node.right==null){
                node.right=new TreeNode(value);
            } else {
                addNode(value, node.right);
            }
        } else if (node.getValue()>=value){
            if (node.left==null){
                node.left=new TreeNode(value);
            } else {
                addNode(value, node.left);
            }
        }
    }
    // show the tree
    public void PrintTree(TreeNode node){
        if (node!=null){
            System.out.print (node.getValue());
            PrintTree(node.left);
            PrintTree(node.right);
        }
    }
    // get the width

    /* Day la phan tinh be ngang lon nhat cua tree, su dung
     * de ve tree tren DrawPanel
     */
    int left; // tan cung ben trai
    int right; // tan cung ben phai
    private  void CalWidth(TreeNode node){
        if (left>node.position) left=node.position;
        if (right<node.position) right=node.position;
        if (node.left!=null){
            node.left.position=node.position-1;
            CalWidth(node.left);
        }
        if (node.right!=null){
            node.right.position=node.position+1;
            CalWidth(node.right);
        }

    }
    int getLeft(){
        this.left=0;
        this.right=0;
        CalWidth(root);
        return this.left;
    }
    int getRight(){
        this.left=0;
        this.right=0;
        CalWidth(root);
        return this.right;
    }
    int getWidth (){
        this.root.position=0;
        this.left=0; this.right=0;
        CalWidth(this.root);
        return (this.right-this.left);
    }
}
