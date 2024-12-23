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
  private static final double DELTA = 1e-10;
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

  @Test
  public void testNativeRand() {
    String expression = "rand(0,10)";
    double result = evalExpression(expression);
    assertTrue(result>=0 && result<=10);
  }

  @Test
  public  void testNativeTanRadian() {
    String expression = "tan(rad(0))";
    double result = evalExpression(expression);
    assertEquals(0, result);
  }
  @Test
  public  void testNativeTanAngle() {
    String expression = "tan(45)";
    double result = evalExpression(expression);
    assertEquals(0.999, result, 1);
  }

  @Test
  public void testTrue() {
    String expression = "T";
    double result = evalExpression(expression);
    assertEquals(result, 1);
  }
  @Test
  public void testFalse() {
    String expression = "F";
    double result = evalExpression(expression);
    assertEquals(result, 0);
  }
  @Test
  public void testRSignalMax() {
    String expression = "SIG_MAX";
    double result = evalExpression(expression);
    assertEquals(result, 15);
  }
  @Test
  public void testCmp() {
    String expression = "cmp(5, 3, 4)";
    double result = evalExpression(expression);
    assertEquals(5.0, result, DELTA);
  }

  @Test
  public void testCmpFalse() {
    String expression = "cmp(5, 6, 4)";
    double result = evalExpression(expression);
    assertEquals(0.0, result, DELTA);
  }

  @Test
  public void testCmpEqual() {
    String expression = "cmp(5, 5, 5)";
    double result = evalExpression(expression);
    assertEquals(5.0, result, DELTA);
  }

  @Test
  public void testCmpZero() {
    String expression = "cmp(0, 0, 0)";
    double result = evalExpression(expression);
    assertEquals(0.0, result, DELTA);
  }
  @Test
  public void testCmpSub() {
    String expression = "cmp_sub(5, 3, 4)";
    double result = evalExpression(expression);
    assertEquals(1.0, result, DELTA);
  }

  @Test
  public void testCmpSubZero() {
    String expression = "cmp_sub(5, 6, 4)";
    double result = evalExpression(expression);
    assertEquals(0.0, result, DELTA);
  }

  @Test
  public void testCmpSubEqual() {
    String expression = "cmp_sub(5, 5, 5)";
    double result = evalExpression(expression);
    assertEquals(0.0, result, DELTA);
  }
}