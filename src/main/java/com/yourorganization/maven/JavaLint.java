package com.yourorganization.maven;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Paths;

public class JavaLint {
    public static void main(String[] args) {
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());

        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.fileInPackageAbsolutePath("src/main/resources", "", ""));
        CompilationUnit cu = sourceRoot.parse("", "Loop.java");

        Log.info("Running...");

        cu.accept(new ModifierVisitor<Void>() {
            @Override
            public Visitable visit(ForStmt n, Void arg) {
                if (n.getCompare().isPresent()) {
                    n.getCompare().get().ifBinaryExpr(binaryExpr -> {
                        if (binaryExpr.getOperator() == BinaryExpr.Operator.GREATER
                                || binaryExpr.getOperator() == BinaryExpr.Operator.GREATER_EQUALS
                                || binaryExpr.getOperator() == BinaryExpr.Operator.LESS
                                || binaryExpr.getOperator() == BinaryExpr.Operator.LESS_EQUALS) {
                            binaryExpr.getRight().ifBinaryExpr(b -> {
                                if (b.getOperator() == BinaryExpr.Operator.MINUS
                                        || b.getOperator() == BinaryExpr.Operator.PLUS) {
                                    Log.info("here");
                                }
                            });
                        }
                    });
                }
                return super.visit(n, arg);
            }
        }, null);

        cu.accept(new ModifierVisitor<Void>() {
            @Override
            public Visitable visit(WhileStmt n, Void arg) {
                n.getCondition().ifBinaryExpr(binaryExpr -> {
                    if (binaryExpr.getOperator() == BinaryExpr.Operator.GREATER
                            || binaryExpr.getOperator() == BinaryExpr.Operator.GREATER_EQUALS
                            || binaryExpr.getOperator() == BinaryExpr.Operator.LESS
                            || binaryExpr.getOperator() == BinaryExpr.Operator.LESS_EQUALS) {
                        binaryExpr.getRight().ifBinaryExpr(b -> {
                            if (b.getOperator() == BinaryExpr.Operator.MINUS
                                    || b.getOperator() == BinaryExpr.Operator.PLUS) {
                                Log.info("here");
                            }
                        });
                    }
                });
                return super.visit(n, arg);
            }
        }, null);

        Log.info("Completed.");
    }
}
