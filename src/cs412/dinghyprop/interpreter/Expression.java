package cs412.dinghyprop.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a section of a program tree.
 */
public class Expression {
    private String operator;
    private List<Object> operands;

    /**
     * Create a new expression tree node.
     * @param operator    The operator at this node.
     */
    public Expression(String operator) {
        this.operator = operator;
        operands = new ArrayList<Object>(3);
    }

    /**
     * Appends an expression tree as an operand to this node.
     * @param operand    The subtree to add.
     */
    public void addOperand(Object operand) {
        operands.add(operand);
    }

    /**
     * Get this node's operator name.
     * @return  The name of the function to call to evaluate this node.
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Obtain the child subtrees under this node.
     * @return  An array of this node's children.
     */
    public Object[] getOperands() {
        return operands.toArray();
    }
}
