package com.jacoobes.scratchpaper;

import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EvalVisitor extends ExprBaseVisitor<Double> {
    protected HashMap<String, EvalFunction> fns = new HashMap<>();
    protected HashMap<String, Method> natives = new HashMap<>();
     {
         Class<?> _math;
         Class<?> _thisclass;
         try {
             _math = Class.forName("java.lang.Math");
             _thisclass = Class.forName("com.jacoobes.scratchpaper.EvalVisitor");
         } catch (ClassNotFoundException e) {
             throw new RuntimeException("Could not find java.lang.Math");
         }
         Method max, sin, cos,
                rad, min, rand,
                tan, cmpSub, cmp;
         try {
             max = _math.getDeclaredMethod("max", double.class, double.class);
             min = _math.getDeclaredMethod("min", double.class, double.class);
             sin = _math.getDeclaredMethod("sin", double.class);
             cos = _math.getDeclaredMethod("cos", double.class);
             rad = _math.getDeclaredMethod("toRadians", double.class);
             rand = _thisclass.getDeclaredMethod("rand", double.class, double.class);
             tan = _math.getDeclaredMethod("tan", double.class);
             cmp  = _thisclass.getDeclaredMethod("cmp", double.class, double.class, double.class);
             cmpSub = _thisclass.getDeclaredMethod("cmpSub", double.class, double.class, double.class);
         } catch (NoSuchMethodException e) {
             throw new RuntimeException(e);
         }
         natives.put("max", max);
         natives.put("min", min);
         natives.put("sin", sin);
         natives.put("cos", cos);
         natives.put("rad", rad);
         natives.put("rand", rand);
         natives.put("tan", tan);
         natives.put("cmp", cmp);
         natives.put("cmp_sub", cmpSub);
     }
     public static double cmp(double rear, double left, double right) {
         //output = rear × [left ≤ rear AND right ≤ rear]
        if(left > rear || right > rear) {
            return 0;
        }
        return rear;
     }
     public static double cmpSub(double rear, double left, double right) {
         //output = max(rear − max(left, right), 0)
         return Math.max(rear - Math.max(left, right), 0);
     }
     public static double rand(double limit1,double limit2){
         double temp;
         if(limit1==limit2) return limit1;
         if(limit2<limit1){
             temp=limit1;
             limit1=limit2;
             limit2=temp;
         }
         return limit1 + (Math.random() * (limit2 - limit1));
     }
    @Override
    public Double visitParen(ExprParser.ParenContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Double visitOpexpr(ExprParser.OpexprContext ctx) {
        var lhs = ctx.expr(0);
        var rhs = ctx.expr(1);
        String op = ctx.op.getText();
        double left = visit(lhs);
        double right = visit(rhs);
        return switch (op.charAt(0)) {
            case '*' -> left * right;
            case '/' -> left / right;
            case '+' -> left + right;
            case '-' -> left - right;
            default -> throw new IllegalArgumentException("Unknown operator " + op);
        };
    }

    @Override
    public Double visitInt(ExprParser.IntContext ctx) {
        return Double.parseDouble(ctx.getText());
    }

    @Override
    public Double visitFuncall(ExprParser.FuncallContext ctx)  {
        var name = ctx.call().name.getText();
        var fn = fns.get(name);
        var exprCalls = ctx.call().args.expr();

        if (fn == null) {
            if(!natives.containsKey(name)) {
                throw new RuntimeException("Could not find function " + name);
            } else {
                var fnnative = natives.get(name);
                if(fnnative.getParameterCount() != exprCalls.size()) {
                    throw new RuntimeException("Invalid arity exception");
                }
                try {
                    return (Double) fnnative.invoke(null, exprCalls.stream().map(this::visit).toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (exprCalls.size() != fn.parms.size()) {
            throw new RuntimeException("Invalid arity exception");
        }
        var boundArgs = IntStream.range(0, fn.parms.size())
                .mapToObj(i -> Map.entry(fn.parms.get(i), exprCalls.get(i)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        var fnvis = new FunctionVisitor(boundArgs, fn);
        return fnvis.visit(fn.body);
    }

    @Override
    public Double visitDef(ExprParser.DefContext ctx) {
        var parms = ctx
                .arglist()
                .ID()
                .stream()
                .map(ParseTree::getText).collect(Collectors.toCollection(ArrayList::new));
        fns.put(ctx.name.getText(),
                new EvalFunction(ctx.name.getText(), ctx.body, parms));
        return null;
    }

    @Override
    public Double visitId(ExprParser.IdContext ctx) {
        return switch (ctx.getText()) {
            case "CHUNK" -> 16.;
            case "PI" -> Math.PI;
            case "E" -> Math.E;
            case "T" -> 1.;
            case "F" -> 0.;
            case "SIG_MAX" -> 15.;
            default -> throw new IllegalStateException("Unexpected value: " + ctx.getText());
        };
    }
}
