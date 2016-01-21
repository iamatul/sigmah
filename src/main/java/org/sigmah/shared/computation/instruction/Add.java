package org.sigmah.shared.computation.instruction;

import java.util.Map;
import java.util.Stack;
import org.sigmah.shared.computation.value.ComputedValue;

/**
 * Add operator.
 * 
 * @author Raphaël Calabro (raphael.calabro@netapsys.fr)
 * @since 2.1
 */
class Add implements Operator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OperatorPriority getPriority() {
		return OperatorPriority.ADD_SUBSTRACT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "+";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Stack<ComputedValue> stack, Map<Integer, ComputedValue> variables) {
		stack.push(stack.pop().addTo(stack.pop()));
	}
	
}
