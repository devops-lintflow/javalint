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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaLint {
    public static void main(String[] args) {
        Log.setAdapter(new Log.SilentAdapter());

        if (args.length == 0 || args[0].equals("--help")) {
            System.out.println("usage: java -jar javalint.jar --file FILE [--help]");
            System.out.println("");
            System.out.println("javalint");
            System.out.println("");
            System.out.println("optional arguments:");
            System.out.println("  --file  file name (/path/to/name.java)");
            System.out.println("  --help  show this help message and exit");
            return;
        }

        if (!args[0].equals("--file")) {
            System.out.println("--file required");
            return;
        }

        if (args.length == 1) {
            System.out.println("file required");
            return;
        }

        File f = new File(args[1]);
        if (!f.exists()) {
            System.out.println("file not exist");
            return;
        }

        Path p = Paths.get(args[1]);
        String path = p.getParent().toString();
        String name = p.getFileName().toString();

        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.fileInPackageAbsolutePath(path, "", ""));
        CompilationUnit cu = sourceRoot.parse("", name);

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
                                    int line = n.getBegin().get().line;
                                    System.out.println("javalint" + ":" + name + ":" + line + ":" + "Error" + ":" + "Possible incorrect condition in range-based for loop");
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
                                int line = n.getBegin().get().line;
                                System.out.println("javalint" + ":" + name + ":" + line + ":" + "Error" + ":" + "Possible incorrect condition in range-based while loop");
                            }
                        });
                    }
                });
                return super.visit(n, arg);
            }
        }, null);
    }
}
