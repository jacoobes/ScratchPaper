package com.jacoobes.scratchpaper;

import java.util.ArrayList;

/**
 * Evaluates user-defined functions
 */
public class EvalFunction {
    ExprParser.ExprContext body;
    ArrayList<String> parms;
    String name;

    /**
     * @param name The function's identifier
     * @param body The function's AST expression context
     * @param parms List of parameter names
     */
    EvalFunction(
            String name,
            ExprParser.ExprContext body,
            ArrayList<String> parms) {
        this.name = name;
        this.body = body;
        this.parms = parms;
    }
}