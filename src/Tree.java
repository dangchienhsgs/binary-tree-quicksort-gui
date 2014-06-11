import org.eclipse.swt.widgets.Shell;

public class Tree {
    TreeNode root;

    private int width;
    int height;

    private int left;
    private int right;

    // Tao the first node
    public void makeRoot (int value){
        this.root=new TreeNode(value);
        root.position=1;
        root.level=1;
    }

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

    public TreeNode addNode (int value, TreeNode node){
        if (node==null){
            makeRoot(value);
            return root;
        } else
        if (node.getValue()==value){
            new PopupDialog(new Shell()).open("Add a node","This node has been create");
            return null;
        } else
            if (node.getValue()<value){
            if (node.right==null){
                node.right=new TreeNode(value);
                node.right.level=node.level+1;
                node.right.position=node.position+1;
                node.right.parent=node;
                computeAllNodeTree();
                return node.right;
            } else {
                return addNode(value, node.right);
            }
        } else if (node.getValue()>value){
            if (node.left==null){
                node.left=new TreeNode(value);
                node.left.level=node.level+1;
                node.left.position=node.position-1;
                node.left.parent=node;
                computeAllNodeTree();
                return node.left;
            } else {
                return addNode(value, node.left);
            }
        }
        return null;
    }

    public void computeAllNodeTree(){
        if (root!=null){
            root.position=1;
            root.level=1;
            this.left=0;
            this.right=0;
            computePropertiesNode(root);    
        }        
    }
    /////////////////////////////
    private int min;
    private TreeNode minNode;
    void findNodeMinStep(TreeNode treeNode){
        if (treeNode!=null){
            if (treeNode.getValue()<min){
                min=treeNode.getValue();
                minNode=treeNode;
            }
            findNodeMinStep(treeNode.left);
            findNodeMinStep(treeNode.right);
        }
    }

    TreeNode findNodeMin(TreeNode treeNode){
        min=treeNode.getValue();
        minNode=treeNode;
        findNodeMinStep(treeNode);
        return minNode;
    }
    /////////////////////////////////////
    public void removeNode (TreeNode treeNode){
        if (treeNode!=null) {
            if (treeNode.right==null){
                treeNode=treeNode.left;
            }
            TreeNode minNode=findNodeMin(treeNode.right);
            treeNode=minNode;
            if (minNode.right==null){
                minNode=minNode.left;
            } else {
                minNode=minNode.right;
            }
        }
    }
    ///////////////////////////////
    public void computePropertiesNode(TreeNode treeNode){
        if (left>treeNode.position) {
            left=treeNode.position;
        }
        if (right<treeNode.position) right=treeNode.position;
        if (treeNode.left!=null){
            treeNode.left.position=treeNode.position-1;
            treeNode.left.level=treeNode.level+1;
            computePropertiesNode(treeNode.left);
        }
        if (treeNode.right!=null){
            treeNode.right.position=treeNode.position+1;
            treeNode.right.level=treeNode.level+1;
            computePropertiesNode(treeNode.right);
        }
    }

    public TreeNode findNode(TreeNode treeNode, int value){
        if (treeNode!=null){
            if (treeNode.getValue()==value) return treeNode;
            else if (treeNode.getValue()<value) return findNode (treeNode.right, value);
            else return findNode (treeNode.left, value);
        }
        else {
            return null;
        }
    }

    int getLeft(){
        return left;
    }
    int getRight(){
        return right;
    }
    int getWidth (){
        computeAllNodeTree();
        return (right-left);
    }

}
