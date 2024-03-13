package com.jacoobes.scratchpaper;

import java.util.ArrayList;

public class EvalFunction {
    ExprParser.ExprContext body;
    ArrayList<String> parms;
    String name;
    EvalFunction(
                String name,
                ExprParser.ExprContext body,
                 ArrayList<String> parms) {
        this.name=name;
        this.body = body; this.parms=parms;
    }

}
