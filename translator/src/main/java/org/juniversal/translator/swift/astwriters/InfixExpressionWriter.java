package org.juniversal.translator.swift.astwriters;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.juniversal.translator.core.Context;

import java.util.HashMap;
import java.util.List;


public class InfixExpressionWriter extends ASTWriter {
	// Data
	private HashMap<InfixExpression.Operator, String> equivalentOperators;  // Operators that have the same token in both Java & C++

	public InfixExpressionWriter(ASTWriters astWriters) {
		super(astWriters);

        // TODO: Handle fact that Swift's operator precedence is different than Java's

        equivalentOperators = new HashMap<>();
		equivalentOperators.put(InfixExpression.Operator.TIMES, "*");
		equivalentOperators.put(InfixExpression.Operator.DIVIDE, "/");
		equivalentOperators.put(InfixExpression.Operator.REMAINDER, "%");
		equivalentOperators.put(InfixExpression.Operator.PLUS, "+");
		equivalentOperators.put(InfixExpression.Operator.MINUS, "-");

		// TODO: Test signed / unsigned semantics here
		equivalentOperators.put(InfixExpression.Operator.LEFT_SHIFT, "<<");
		equivalentOperators.put(InfixExpression.Operator.RIGHT_SHIFT_SIGNED, ">>");
		//cppOperators.put(InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED, ">>>");

		equivalentOperators.put(InfixExpression.Operator.LESS, "<");
		equivalentOperators.put(InfixExpression.Operator.GREATER, ">");
		equivalentOperators.put(InfixExpression.Operator.LESS_EQUALS, "<=");
		equivalentOperators.put(InfixExpression.Operator.GREATER_EQUALS, ">=");
		equivalentOperators.put(InfixExpression.Operator.EQUALS, "==");
		equivalentOperators.put(InfixExpression.Operator.NOT_EQUALS, "!=");

        equivalentOperators.put(InfixExpression.Operator.AND, "&");
		equivalentOperators.put(InfixExpression.Operator.XOR, "^");
		equivalentOperators.put(InfixExpression.Operator.OR, "|");

		equivalentOperators.put(InfixExpression.Operator.CONDITIONAL_AND, "&&");
		equivalentOperators.put(InfixExpression.Operator.CONDITIONAL_OR, "||");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void write(ASTNode node, Context context) {
		InfixExpression infixExpression = (InfixExpression) node;

        // TODO: Add spaces to left & right of binary operators if needed, per Swift's rules about needing space on
        // both sides or neither
		
		InfixExpression.Operator operator = infixExpression.getOperator();

		if (operator == InfixExpression.Operator.RIGHT_SHIFT_UNSIGNED) {
            // TODO: Handle this
			context.write("rightShiftUnsigned(");
			getASTWriters().writeNode(infixExpression.getLeftOperand(), context);

			// Skip spaces before the >>> but if there's a newline (or comments) there, copy them
			context.skipSpacesAndTabs();
			context.copySpaceAndComments();
			context.matchAndWrite(">>>", ",");

			context.copySpaceAndComments();
			getASTWriters().writeNode(infixExpression.getRightOperand(), context);
			context.write(")");
		}
		else {
			getASTWriters().writeNode(infixExpression.getLeftOperand(), context);

			context.copySpaceAndComments();
			String operatorToken = this.equivalentOperators.get(infixExpression.getOperator());
			context.matchAndWrite(operatorToken);

			context.copySpaceAndComments();
			getASTWriters().writeNode(infixExpression.getRightOperand(), context);

			if (infixExpression.hasExtendedOperands()) {
				for (Expression extendedOperand : (List<Expression>) infixExpression.extendedOperands()) {
					
					context.copySpaceAndComments();
					context.matchAndWrite(operatorToken);
	
					context.copySpaceAndComments();
					getASTWriters().writeNode(extendedOperand, context);
				}
			}
		}
	}
}