package com.jacoobes.scratchpaper;

import java.util.Map;
import java.util.Objects;

public class FunctionVisitor extends EvalVisitor {
    private final Map<String, ExprParser.ExprContext> boundArgs;
    private final EvalFunction callee;
    FunctionVisitor(
            Map<String, ExprParser.ExprContext> boundArgs,
            EvalFunction fn
    ) {
        super();
        this.boundArgs=boundArgs;
        this.callee=fn;
    }

    @Override
    public Double visitFuncall(ExprParser.FuncallContext ctx) {
        if(Objects.equals(ctx.call().name.getText(), callee.name)) {
            throw new RuntimeException("Recursion doesn't work");
        }
        return -1.;
    };

    @Override
    public Double visitDef(ExprParser.DefContext ctx) {
        throw new RuntimeException("Cannot have local functions");
    }

    @Override
    public Double visitId(ExprParser.IdContext ctx) {
        return switch (ctx.getText()) {
            case "CHUNK" -> 16.;
            case "PI" -> Math.PI;
            case "E"  -> Math.E;
            default -> {
                if(this.boundArgs.containsKey(ctx.getText())) {
                     yield visit(this.boundArgs.get(ctx.getText()));
                }
                throw new RuntimeException("Unknown arg " + ctx.getText());
            }
        };
    }
}
