import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class CodeVisitor extends VoidVisitorAdapter<String> {

    public TagVector tags = new TagVector();

    public void visit(ClassOrInterfaceDeclaration n, String file) {
        tags.addElement(new Tag(n.getName(), file, n.getBeginLine() + ""));
        super.visit(n, file);
    }

    @Override
    public void visit(FieldDeclaration n, String file) {
    }

    @Override
    public void visit(ConstructorDeclaration n, String file) {
    }

    @Override
    public void visit(VariableDeclarationExpr n, String file) {
    }

    @Override
    public void visit(MethodDeclaration n, String file) {
    }

    @Override
    public void visit(MethodCallExpr n, String file) {
    }
}
