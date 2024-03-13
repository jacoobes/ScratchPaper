package com.jacoobes.scratchpaper;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ExamplePlugin.
 *
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
public class EvalTest {

  private Double evalExpression(String expression) {
    var Lexer = new ExprLexer(CharStreams.fromString(expression));
    var Parser = new ExprParser(new CommonTokenStream(Lexer));
    ParseTree tree = Parser.prog();

    return new EvalVisitor().visit(tree);
  }
  @Test
  public void simpleAdd() {
    assertEquals(2, evalExpression("1 + 1"));
  };
  @Test
  public void numPlusId() {
    assertEquals(17, evalExpression("1 + CHUNK"));
  };
  @Test
  public void parenthesis() {
    String expression = "(2+3+ (4 * 2))*4";
    double expected = 13*4;
    double result = evalExpression(expression);
    assertEquals(expected, result, 0.0001);
  }

  @Test
  public void multiplication() {
    String expression = "4*5/2";
    double expected = 10.0;
    double result = evalExpression(expression);
    assertEquals(expected, result, 0.0001);
  }

  @Test
  public void def() {
    String program = "fn V () = 5;";
    Double result = evalExpression(program);
    assertNull(result);
  }

  @Test
  public void defCall() {
    String program = "fn V () = 5; V()";
    Double result = evalExpression(program);
    assertEquals(5, result);
  }

  @Test
  public void defCallArgs() {
    String program = "fn V (a,b) = a+b; V(1,2)";
    Double result = evalExpression(program);
    assertEquals(3, result);
  }
  @Test
  public void testFloatingPointAddition() {
    String expression = "3.14 + 2.71";
    double expected = 5.85;
    double result = evalExpression(expression);
    assertEquals(expected, result, 0.0001);
  }

  @Test
  public void testFloatingPointMultiplicationAndDivision() {
    String expression = "10.5 * 2.0 / 3.0";
    double expected = 7.0;
    double result = evalExpression(expression);
    assertEquals(expected, result, 0.0001);
  }

  @Test
  public void testNativeCall() {
    String expression = "sin(rad(30))";
    double result = evalExpression(expression);
    assertEquals(0.49999, result, 0.0001);
  }

  @Test
  public void testNativeMax() {
    String expression = "max(0,10)";
    double result = evalExpression(expression);
    assertEquals(10, result, 0.0001);
  }
}