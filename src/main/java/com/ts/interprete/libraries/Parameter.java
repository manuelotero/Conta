package com.ts.interprete.libraries;

import java.util.ArrayList;

public class Parameter{
	
	private ArrayList<Expression> params = new ArrayList<Expression>();
	
	public void add(Expression expression)
	{
		this.params.add(expression);
	}

}
