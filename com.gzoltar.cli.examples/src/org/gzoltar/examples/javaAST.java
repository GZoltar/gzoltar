import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;

public class JavaASTExample {

    public static void main(String[] args) throws Exception {
        // Path to the Java file you want to parse
        FileInputStream in = new FileInputStream(new File("/CharacterCounter.java"));

        // Parse the file
        CompilationUnit cu = JavaParser.parse(in);

        // Visit and print the AST
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(CompilationUnit n, Void arg) {
                super.visit(n, arg);
                System.out.println(n.toString());
            }
        }, null);
    }
}
