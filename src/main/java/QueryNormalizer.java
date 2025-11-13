import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class QueryNormalizer {
    
    /**
     * Normalize SQL query by replacing literals with placeholders
     * @throws RuntimeException if the SQL query cannot be parsed
     */
    public NormalizedQuery normalize(String sqlQuery) {
        // Step 1: Preprocess (trim, normalize whitespace)
        String cleaned = preprocessQuery(sqlQuery);
        
        // Step 2: Parse using ANTLR
        CharStream input = CharStreams.fromString(cleaned);
        SQLLexer lexer = new SQLLexer(input);
        
        // Add error listener to collect parse errors
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser(tokens);
        
        // Use a custom error listener that throws exceptions on syntax errors
        ((Parser) parser).removeErrorListeners();
        ((Lexer) lexer).removeErrorListeners();
        
        final StringBuilder errorMessages = new StringBuilder();
        
        BaseErrorListener errorListener = new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                  int line, int charPositionInLine, String msg,
                                  RecognitionException e) {
                errorMessages.append(String.format("Line %d:%d - %s%n", line, charPositionInLine, msg));
            }
        };
        
        lexer.addErrorListener(errorListener);
        parser.addErrorListener(errorListener);
        
        ParseTree tree = parser.statement(); // Get AST root
        
        // Check for parse errors
        if (errorMessages.length() > 0) {
            throw new RuntimeException("SQL parse error: " + errorMessages.toString());
        }
        
        // Validate that parsing succeeded
        if (tree == null) {
            throw new RuntimeException("Failed to parse SQL query: no parse tree generated");
        }
        
        // Step 3: Visit AST and normalize
        NormalizationVisitor visitor = new NormalizationVisitor();
        visitor.visit(tree);
        
        // Step 4: Build result
        return new NormalizedQuery(
            visitor.getNormalizedPattern(),
            visitor.getParameters(),
            visitor.getParameterMetadata(),
            visitor.getReferencedTables()
        );
    }
    
    /**
     * Preprocess query: trim, normalize whitespace, uppercase keywords
     */
    private String preprocessQuery(String sql) {
        return sql.trim()
                  .replaceAll("\\s+", " ")  // Multiple spaces to single
                  .replaceAll("\\s*,\\s*", ","); // Remove spaces around commas
    }
}
