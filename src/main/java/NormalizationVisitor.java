import java.util.*;

public class NormalizationVisitor extends SQLBaseVisitor<Void> {
    
    private StringBuilder normalizedPattern;
    private List<Object> parameters;
    private List<ParameterMetadata> parameterMetadata;
    private Set<String> referencedTables;
    private int parameterIndex;
    
    public NormalizationVisitor() {
        this.normalizedPattern = new StringBuilder();
        this.parameters = new ArrayList<>();
        this.parameterMetadata = new ArrayList<>();
        this.referencedTables = new HashSet<>();
        this.parameterIndex = 0;
    }
    
    // ========================================================================
    // SELECT STATEMENTS
    // ========================================================================
    
    /**
     * Visit SELECT statement
     */
    @Override
    public Void visitSelectStatement(SQLParser.SelectStatementContext ctx) {
        normalizedPattern.append("SELECT ");
        
        // Handle DISTINCT
        if (ctx.DISTINCT() != null) {
            normalizedPattern.append("DISTINCT ");
        }
        
        // Visit select list
        visit(ctx.selectList());
        
        normalizedPattern.append(" FROM ");
        
        // Visit table references
        visit(ctx.tableReferences());
        
        // Visit JOIN clauses
        if (ctx.joinClause() != null) {
            for (SQLParser.JoinClauseContext join : ctx.joinClause()) {
                visit(join);
            }
        }
        
        // Visit WHERE clause if present
        if (ctx.whereClause() != null) {
            normalizedPattern.append(" WHERE ");
            visit(ctx.whereClause());
        }
        
        // Visit GROUP BY
        if (ctx.groupByClause() != null) {
            normalizedPattern.append(" GROUP BY ");
            visit(ctx.groupByClause());
        }
        
        // Visit HAVING
        if (ctx.havingClause() != null) {
            normalizedPattern.append(" HAVING ");
            visit(ctx.havingClause());
        }
        
        // Visit ORDER BY
        if (ctx.orderByClause() != null) {
            normalizedPattern.append(" ORDER BY ");
            visit(ctx.orderByClause());
        }
        
        // Visit LIMIT/OFFSET
        if (ctx.limitClause() != null) {
            normalizedPattern.append(" LIMIT ");
            visit(ctx.limitClause());
        }
        
        return null;
    }
    
    /**
     * Visit JOIN clause
     */
    @Override
    public Void visitJoinClause(SQLParser.JoinClauseContext ctx) {
        // Determine join type
        if (ctx.INNER() != null) {
            normalizedPattern.append(" INNER JOIN ");
        } else if (ctx.LEFT() != null) {
            normalizedPattern.append(" LEFT JOIN ");
        } else if (ctx.RIGHT() != null) {
            normalizedPattern.append(" RIGHT JOIN ");
        } else if (ctx.FULL() != null) {
            normalizedPattern.append(" FULL OUTER JOIN ");
        } else {
            normalizedPattern.append(" JOIN ");
        }
        
        visit(ctx.tableName());
        
        if (ctx.expression() != null) {
            normalizedPattern.append(" ON ");
            visit(ctx.expression()); // ON expression
        }
        
        return null;
    }
    
    /**
     * Visit subquery in FROM clause
     */
    @Override
    public Void visitSubquery(SQLParser.SubqueryContext ctx) {
        normalizedPattern.append("(");
        visit(ctx.selectStatement());
        normalizedPattern.append(")");
        
        if (ctx.alias() != null) {
            normalizedPattern.append(" AS ");
            visit(ctx.alias());
        }
        
        return null;
    }
    
    // ========================================================================
    // INSERT STATEMENTS
    // ========================================================================
    
    /**
     * Visit INSERT statement
     */
    @Override
    public Void visitInsertStatement(SQLParser.InsertStatementContext ctx) {
        normalizedPattern.append("INSERT INTO ");
        
        visit(ctx.tableName());
        
        // Column list
        if (ctx.columnList() != null) {
            normalizedPattern.append(" (");
            visit(ctx.columnList());
            normalizedPattern.append(")");
        }
        
        // Handle either valueTuple OR insertSelect
        if (ctx.valueTuple() != null) {
            normalizedPattern.append(" ");
            visit(ctx.valueTuple());
        } else if (ctx.insertSelect() != null) {
            normalizedPattern.append(" ");
            visit(ctx.insertSelect());
        }
        
        return null;
    }
    
    /**
     * Visit value tuple (a,b,c) in INSERT
     * valueTuple: VALUES '(' expressionList ')'
     */
    @Override
    public Void visitValueTuple(SQLParser.ValueTupleContext ctx) {
        normalizedPattern.append("VALUES (");
        List<SQLParser.ExpressionContext> expressions = ctx.expressionList().expression();
        for (int i = 0; i < expressions.size(); i++) {
            visit(expressions.get(i));
            if (i < expressions.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        normalizedPattern.append(")");
        return null;
    }
    
    /**
     * Visit INSERT ... SELECT statement
     * Note: insertSelect is just a selectStatement, tableName and columnList
     * are handled by the parent insertStatement
     */
    @Override
    public Void visitInsertSelect(SQLParser.InsertSelectContext ctx) {
        // Just visit the selectStatement - tableName and columnList are in parent
        visit(ctx.selectStatement());
        return null;
    }
    
    // ========================================================================
    // UPDATE STATEMENTS
    // ========================================================================
    
    /**
     * Visit UPDATE statement
     */
    @Override
    public Void visitUpdateStatement(SQLParser.UpdateStatementContext ctx) {
        normalizedPattern.append("UPDATE ");
        
        visit(ctx.tableName());
        
        normalizedPattern.append(" SET ");
        
        // Handle SET clauses
        List<SQLParser.SetClauseContext> setClauses = ctx.setClause();
        for (int i = 0; i < setClauses.size(); i++) {
            visit(setClauses.get(i));
            if (i < setClauses.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        
        // WHERE clause
        if (ctx.whereClause() != null) {
            normalizedPattern.append(" WHERE ");
            visit(ctx.whereClause());
        }
        
        return null;
    }
    
    /**
     * Visit SET clause (column = value)
     */
    @Override
    public Void visitSetClause(SQLParser.SetClauseContext ctx) {
        normalizedPattern.append(ctx.columnName().getText());
        normalizedPattern.append(" = ");
        visit(ctx.expression());
        return null;
    }
    
    // ========================================================================
    // DELETE STATEMENTS
    // ========================================================================
    
    /**
     * Visit DELETE statement
     */
    @Override
    public Void visitDeleteStatement(SQLParser.DeleteStatementContext ctx) {
        normalizedPattern.append("DELETE FROM ");
        
        visit(ctx.tableName());
        
        if (ctx.whereClause() != null) {
            normalizedPattern.append(" WHERE ");
            visit(ctx.whereClause());
        }
        
        return null;
    }
    
    // ========================================================================
    // MERGE/UPSERT STATEMENTS
    // ========================================================================
    
    /**
     * Visit MERGE statement (UPSERT)
     */
    @Override
    public Void visitMergeStatement(SQLParser.MergeStatementContext ctx) {
        normalizedPattern.append("MERGE INTO ");
        visit(ctx.tableName(0)); // First tableName is target
        
        normalizedPattern.append(" USING ");
        visit(ctx.tableName(1)); // Second tableName is source
        
        normalizedPattern.append(" ON ");
        visit(ctx.expression()); // ON expression
        
        // WHEN MATCHED clauses
        for (SQLParser.WhenMatchedContext whenMatched : ctx.whenMatched()) {
            normalizedPattern.append(" WHEN MATCHED THEN ");
            if (whenMatched.updateStatement() != null) {
                visit(whenMatched.updateStatement());
            } else if (whenMatched.deleteStatement() != null) {
                visit(whenMatched.deleteStatement());
            }
        }
        
        // WHEN NOT MATCHED clauses
        for (SQLParser.WhenNotMatchedContext whenNotMatched : ctx.whenNotMatched()) {
            normalizedPattern.append(" WHEN NOT MATCHED THEN ");
            visit(whenNotMatched.insertClause());
        }
        
        return null;
    }
    
    // ========================================================================
    // CTE (Common Table Expressions)
    // ========================================================================
    
    /**
     * Visit WITH clause (CTE)
     */
    @Override
    public Void visitWithClause(SQLParser.WithClauseContext ctx) {
        normalizedPattern.append("WITH ");
        
        if (ctx.RECURSIVE() != null) {
            normalizedPattern.append("RECURSIVE ");
        }
        
        List<SQLParser.CteContext> ctes = ctx.cte();
        for (int i = 0; i < ctes.size(); i++) {
            visit(ctes.get(i));
            if (i < ctes.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        
        normalizedPattern.append(" ");
        visit(ctx.selectStatement()); // Last selectStatement is the main query
        
        return null;
    }
    
    /**
     * Visit individual CTE
     */
    @Override
    public Void visitCte(SQLParser.CteContext ctx) {
        normalizedPattern.append(ctx.cteName().getText());
        
        if (ctx.columnList() != null) {
            normalizedPattern.append(" (");
            visit(ctx.columnList());
            normalizedPattern.append(")");
        }
        
        normalizedPattern.append(" AS (");
        visit(ctx.selectStatement());
        normalizedPattern.append(")");
        
        return null;
    }
    
    
    
    // ========================================================================
    // FUNCTION CALLS
    // ========================================================================
    
    /**
     * Visit function call (handles all functions: scalar, aggregate, window)
     */
    @Override
    public Void visitFunctionCall(SQLParser.FunctionCallContext ctx) {
        String funcName = ctx.functionName().getText().toUpperCase();
        normalizedPattern.append(funcName).append("(");
        
        // Handle optional DISTINCT
        if (ctx.DISTINCT() != null) {
            normalizedPattern.append("DISTINCT ");
        }
        
        // Handle arguments: STAR, single expression, or argumentList
        if (ctx.STAR() != null) {
            normalizedPattern.append("*");
        } else if (ctx.argumentList() != null) {
            List<SQLParser.ExpressionContext> args = ctx.argumentList().expression();
            for (int i = 0; i < args.size(); i++) {
                visit(args.get(i));
                if (i < args.size() - 1) {
                    normalizedPattern.append(",");
                }
            }
        } else if (ctx.expression() != null) {
            visit(ctx.expression());
        }
        
        normalizedPattern.append(")");
        
        // Handle optional OVER clause (window function)
        if (ctx.windowSpec() != null) {
            SQLParser.WindowSpecContext windowSpec = ctx.windowSpec();
            normalizedPattern.append(" OVER (");
            
            if (windowSpec.partitionByClause() != null) {
                normalizedPattern.append("PARTITION BY ");
                visit(windowSpec.partitionByClause().expressionList());
            }
            
            if (windowSpec.orderByClause() != null) {
                if (windowSpec.partitionByClause() != null) {
                    normalizedPattern.append(" ");
                }
                visit(windowSpec.orderByClause());
            }
            
            if (windowSpec.windowFrame() != null) {
                normalizedPattern.append(" ");
                visit(windowSpec.windowFrame());
            }
            
            normalizedPattern.append(")");
        }
        
        return null;
    }
    
    // ========================================================================
    // CASE EXPRESSIONS
    // ========================================================================
    
    /**
     * Visit CASE expression
     */
    @Override
    public Void visitCaseExpression(SQLParser.CaseExpressionContext ctx) {
        normalizedPattern.append("CASE ");
        
        // Simple CASE: CASE expr WHEN ...
        if (ctx.expression() != null) {
            visit(ctx.expression());
            normalizedPattern.append(" ");
        }
        
        // WHEN clauses
        for (SQLParser.WhenClauseContext when : ctx.whenClause()) {
            normalizedPattern.append("WHEN ");
            visit(when.expression().get(0));
            normalizedPattern.append(" THEN ");
            visit(when.expression().get(1));
            normalizedPattern.append(" ");
        }
        
        // ELSE clause
        if (ctx.elseClause() != null) {
            normalizedPattern.append("ELSE ");
            visit(ctx.elseClause().expression());
            normalizedPattern.append(" ");
        }
        
        normalizedPattern.append("END");
        
        return null;
    }
    
    // ========================================================================
    // PREDICATES AND CONDITIONS
    // ========================================================================
    
    /**
     * Visit BETWEEN predicate
     */
    @Override
    public Void visitBetweenPredicate(SQLParser.BetweenPredicateContext ctx) {
        visit(ctx.arithmeticExpression(0)); // First arithmeticExpression
        
        if (ctx.NOT() != null) {
            normalizedPattern.append(" NOT");
        }
        
        normalizedPattern.append(" BETWEEN ");
        visit(ctx.arithmeticExpression(0)); // Changed from lowerBound() to lower (second arithmeticExpression)
        normalizedPattern.append(" AND ");
        visit(ctx.arithmeticExpression(1)); // Changed from upperBound() to upper (third arithmeticExpression)
        
        return null;
    }
    
    /**
     * Visit LIKE predicate
     */
    @Override
    public Void visitLikePredicate(SQLParser.LikePredicateContext ctx) {
        visit(ctx.arithmeticExpression(0)); // First arithmeticExpression
        
        if (ctx.NOT() != null) {
            normalizedPattern.append(" NOT");
        }
        
        normalizedPattern.append(" LIKE ");
        visit(ctx.arithmeticExpression(1)); // Second arithmeticExpression
        
        if (ctx.ESCAPE() != null) {
            normalizedPattern.append(" ESCAPE ");
            visit(ctx.arithmeticExpression(2)); // Third arithmeticExpression
        }
        
        return null;
    }
    
    /**
     * Visit IS NULL predicate
     */
    @Override
    public Void visitIsNullPredicate(SQLParser.IsNullPredicateContext ctx) {
        visit(ctx.arithmeticExpression()); // arithmeticExpression
        normalizedPattern.append(" IS ");
        
        if (ctx.NOT() != null) {
            normalizedPattern.append("NOT ");
        }
        
        normalizedPattern.append("NULL");
        
        return null;
    }
    
    /**
     * Visit EXISTS predicate
     */
    @Override
    public Void visitExistsPredicate(SQLParser.ExistsPredicateContext ctx) {
        normalizedPattern.append("EXISTS (");
        visit(ctx.subquery());
        normalizedPattern.append(")");
        
        return null;
    }
    
    
    // ========================================================================
    // LITERALS - These get parameterized
    // ========================================================================
    
    /**
     * Visit integer literal - replace with placeholder
     */
    @Override
    public Void visitIntegerLiteral(SQLParser.IntegerLiteralContext ctx) {
        int value = Integer.parseInt(ctx.getText());
        
        // Replace with placeholder
        normalizedPattern.append("?");
        
        // Store parameter value and metadata
        parameters.add(value);
        parameterMetadata.add(new ParameterMetadata(
            parameterIndex++,
            ParameterType.INTEGER,
            value
        ));
        
        return null;
    }
    
    /**
     * Visit string literal - replace with placeholder
     */
    @Override
    public Void visitStringLiteral(SQLParser.StringLiteralContext ctx) {
        String value = ctx.getText();
        // Remove quotes and handle escaped quotes
        value = value.substring(1, value.length() - 1)
                     .replace("''", "'")
                     .replace("\\'", "'");
        
        normalizedPattern.append("?");
        parameters.add(value);
        parameterMetadata.add(new ParameterMetadata(
            parameterIndex++,
            ParameterType.STRING,
            value
        ));
        
        return null;
    }
    
    /**
     * Visit float/decimal literal - replace with placeholder
     */
    @Override
    public Void visitFloatLiteral(SQLParser.FloatLiteralContext ctx) {
        double value = Double.parseDouble(ctx.getText());
        
        normalizedPattern.append("?");
        parameters.add(value);
        parameterMetadata.add(new ParameterMetadata(
            parameterIndex++,
            ParameterType.FLOAT,
            value
        ));
        
        return null;
    }
    
    /**
     * Visit boolean literal - replace with placeholder
     */
    @Override
    public Void visitBooleanLiteral(SQLParser.BooleanLiteralContext ctx) {
        boolean value = Boolean.parseBoolean(ctx.getText());
        
        normalizedPattern.append("?");
        parameters.add(value);
        parameterMetadata.add(new ParameterMetadata(
            parameterIndex++,
            ParameterType.BOOLEAN,
            value
        ));
        
        return null;
    }
    
    /**
     * Visit date/timestamp literal - replace with placeholder
     */
    @Override
    public Void visitDateLiteral(SQLParser.DateLiteralContext ctx) {
        String value = ctx.getText();
        
        normalizedPattern.append("?");
        parameters.add(value);
        parameterMetadata.add(new ParameterMetadata(
            parameterIndex++,
            ParameterType.DATE,
            value
        ));
        
        return null;
    }
    
    /**
     * Visit NULL - keep as keyword, don't parameterize
     */
    @Override
    public Void visitNullLiteral(SQLParser.NullLiteralContext ctx) {
        normalizedPattern.append("NULL");
        return null;
    }
    
    // ========================================================================
    // IDENTIFIERS - Keep as-is, track table dependencies
    // ========================================================================
    
    /**
     * Visit table reference and track dependency
     */
    @Override
    public Void visitTableName(SQLParser.TableNameContext ctx) {
        String tableName = ctx.getText();
        
        // Handle schema.table notation
        if (tableName.contains(".")) {
            String[] parts = tableName.split("\\.");
            tableName = parts[parts.length - 1];  // Get table name only
        }
        
        normalizedPattern.append(ctx.getText());
        referencedTables.add(tableName);
        return null;
    }
    
    /**
     * Visit column reference - keep as-is
     */
    @Override
    public Void visitColumnReference(SQLParser.ColumnReferenceContext ctx) {
        normalizedPattern.append(ctx.getText());
        return null;
    }
    
    /**
     * Visit alias (alias is just an IDENTIFIER token)
     */
    @Override
    public Void visitAlias(SQLParser.AliasContext ctx) {
        // alias is just an IDENTIFIER, so getText() should work
        normalizedPattern.append(ctx.getText());
        return null;
    }
    
    // ========================================================================
    // OPERATORS AND KEYWORDS - Keep as-is
    // ========================================================================
    
    /**
     * Visit comparison operator - keep as-is
     */
    @Override
    public Void visitComparisonOperator(SQLParser.ComparisonOperatorContext ctx) {
        normalizedPattern.append(" ").append(ctx.getText()).append(" ");
        return null;
    }
    
    /**
     * Visit logical operator (AND, OR, NOT)
     */
    @Override
    public Void visitLogicalOperator(SQLParser.LogicalOperatorContext ctx) {
        normalizedPattern.append(" ").append(ctx.getText().toUpperCase()).append(" ");
        return null;
    }
    
    /**
     * Visit arithmetic operator (+, -, *, /)
     */
    @Override
    public Void visitArithmeticOperator(SQLParser.ArithmeticOperatorContext ctx) {
        normalizedPattern.append(" ").append(ctx.getText()).append(" ");
        return null;
    }
    
    // ========================================================================
    // COMPLEX PREDICATES
    // ========================================================================
    
    /**
     * Visit IN predicate with multiple values
     */
    @Override
    public Void visitInPredicate(SQLParser.InPredicateContext ctx) {
        visit(ctx.arithmeticExpression()); // arithmeticExpression
        
        if (ctx.NOT() != null) {
            normalizedPattern.append(" NOT");
        }
        
        normalizedPattern.append(" IN (");
        
        if (ctx.selectStatement() != null) {
            // IN with subquery
            visit(ctx.selectStatement());
        } else {
            // IN with literal list
            List<SQLParser.ExpressionContext> expressions = ctx.expressionList().expression();
            for (int i = 0; i < expressions.size(); i++) {
                visit(expressions.get(i));
                if (i < expressions.size() - 1) {
                    normalizedPattern.append(",");
                }
            }
        }
        
        normalizedPattern.append(")");
        return null;
    }
    
    /**
     * Visit ANY/ALL predicate
     */
    @Override
    public Void visitQuantifiedPredicate(SQLParser.QuantifiedPredicateContext ctx) {
        visit(ctx.arithmeticExpression()); // arithmeticExpression
        normalizedPattern.append(" ").append(ctx.comparisonOperator().getText()).append(" ");
        
        if (ctx.ANY() != null) {
            normalizedPattern.append("ANY");
        } else if (ctx.ALL() != null) {
            normalizedPattern.append("ALL");
        } else if (ctx.SOME() != null) {
            normalizedPattern.append("SOME");
        }
        
        normalizedPattern.append(" (");
        visit(ctx.subquery());
        normalizedPattern.append(")");
        
        return null;
    }
    
    // ========================================================================
    // SCALAR FUNCTIONS
    // ========================================================================
    

    
    /**
     * Visit CAST expression
     */
    @Override
    public Void visitCastExpression(SQLParser.CastExpressionContext ctx) {
        normalizedPattern.append("CAST(");
        visit(ctx.expression());
        normalizedPattern.append(" AS ");
        normalizedPattern.append(ctx.dataType().getText());
        normalizedPattern.append(")");
        
        return null;
    }
    
    /**
     * Visit EXTRACT expression
     */
    @Override
    public Void visitExtractExpression(SQLParser.ExtractExpressionContext ctx) {
        normalizedPattern.append("EXTRACT(");
        normalizedPattern.append(ctx.dateField().getText());
        normalizedPattern.append(" FROM ");
        visit(ctx.expression());
        normalizedPattern.append(")");
        
        return null;
    }
    
    // ========================================================================
    // UTILITY METHODS
    // ========================================================================
    
    /**
     * Visit parenthesized expression
     */
    @Override
    public Void visitParenthesizedExpr(SQLParser.ParenthesizedExprContext ctx) {
        normalizedPattern.append("(");
        visit(ctx.expression());
        normalizedPattern.append(")");
        return null;
    }
    
    /**
     * Visit expression list (for SELECT list, function args, etc.)
     */
    @Override
    public Void visitExpressionList(SQLParser.ExpressionListContext ctx) {
        List<SQLParser.ExpressionContext> expressions = ctx.expression();
        for (int i = 0; i < expressions.size(); i++) {
            visit(expressions.get(i));
            if (i < expressions.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        return null;
    }
    
    // ========================================================================
    // EXPRESSION ALTERNATIVES (labeled in grammar)
    // ========================================================================
    
    /**
     * Visit literal expression alternative
     */
    @Override
    public Void visitLiteralExpr(SQLParser.LiteralExprContext ctx) {
        return visit(ctx.literal());
    }
    
    /**
     * Visit column reference expression alternative
     */
    @Override
    public Void visitColumnRefExpr(SQLParser.ColumnRefExprContext ctx) {
        return visit(ctx.columnReference());
    }
    
    /**
     * Visit logical expression (not an alt label, it's a separate rule)
     */
    @Override
    public Void visitLogicalExpression(SQLParser.LogicalExpressionContext ctx) {
        List<SQLParser.ComparisonExpressionContext> comparisons = ctx.comparisonExpression();
        for (int i = 0; i < comparisons.size(); i++) {
            visit(comparisons.get(i));
            if (i < comparisons.size() - 1) {
                normalizedPattern.append(" ");
                visit(ctx.logicalOperator().get(i));
                normalizedPattern.append(" ");
            }
        }
        return null;
    }
    
    /**
     * Visit comparison expression (not an alt label, it's a separate rule)
     */
    @Override
    public Void visitComparisonExpression(SQLParser.ComparisonExpressionContext ctx) {
        if (ctx.arithmeticExpression().size() == 2) {
            // Comparison: expr op expr
            visit(ctx.arithmeticExpression().get(0));
            normalizedPattern.append(" ");
            visit(ctx.comparisonOperator());
            normalizedPattern.append(" ");
            visit(ctx.arithmeticExpression().get(1));
        } else if (ctx.inPredicate() != null) {
            visit(ctx.inPredicate());
        } else if (ctx.existsPredicate() != null) {
            visit(ctx.existsPredicate());
        } else if (ctx.betweenPredicate() != null) {
            visit(ctx.betweenPredicate());
        } else if (ctx.likePredicate() != null) {
            visit(ctx.likePredicate());
        } else if (ctx.isNullPredicate() != null) {
            visit(ctx.isNullPredicate());
        } else if (ctx.quantifiedPredicate() != null) {
            visit(ctx.quantifiedPredicate());
        } else {
            // Just an arithmetic expression
            visit(ctx.arithmeticExpression().get(0));
        }
        return null;
    }
    
    /**
     * Visit arithmetic expression (not an alt label, it's a separate rule)
     */
    @Override
    public Void visitArithmeticExpression(SQLParser.ArithmeticExpressionContext ctx) {
        List<SQLParser.UnaryExpressionContext> unaryExprs = ctx.unaryExpression();
        for (int i = 0; i < unaryExprs.size(); i++) {
            visit(unaryExprs.get(i));
            if (i < unaryExprs.size() - 1) {
                normalizedPattern.append(" ");
                visit(ctx.arithmeticOperator().get(i));
                normalizedPattern.append(" ");
            }
        }
        return null;
    }
    
    /**
     * Visit subquery expression alternative
     */
    @Override
    public Void visitSubqueryExpr(SQLParser.SubqueryExprContext ctx) {
        return visit(ctx.subquery());
    }
    
    // ========================================================================
    // SELECT LIST AND TABLE REFERENCES
    // ========================================================================
    
    /**
     * Visit select list
     */
    @Override
    public Void visitSelectList(SQLParser.SelectListContext ctx) {
        if (ctx.STAR() != null) {
            normalizedPattern.append("*");
        } else {
            List<SQLParser.SelectItemContext> items = ctx.selectItem();
            for (int i = 0; i < items.size(); i++) {
                visit(items.get(i));
                if (i < items.size() - 1) {
                    normalizedPattern.append(",");
                }
            }
        }
        return null;
    }
    
    /**
     * Visit select item
     */
    @Override
    public Void visitSelectItem(SQLParser.SelectItemContext ctx) {
        if (ctx.tableName() != null && ctx.STAR() != null) {
            visit(ctx.tableName());
            normalizedPattern.append(".*");
        } else if (ctx.STAR() != null) {
            normalizedPattern.append("*");
        } else {
            visit(ctx.expression());
        }
        if (ctx.alias() != null) {
            normalizedPattern.append(" AS ").append(ctx.alias().getText());
        }
        return null;
    }
    
    /**
     * Visit table references
     */
    @Override
    public Void visitTableReferences(SQLParser.TableReferencesContext ctx) {
        List<SQLParser.TableReferenceContext> refs = ctx.tableReference();
        for (int i = 0; i < refs.size(); i++) {
            visit(refs.get(i));
            if (i < refs.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        return null;
    }
    
    /**
     * Visit table reference
     */
    @Override
    public Void visitTableReference(SQLParser.TableReferenceContext ctx) {
        if (ctx.tableName() != null) {
            visit(ctx.tableName());
            if (ctx.alias() != null) {
                normalizedPattern.append(" AS ");
                visit(ctx.alias());
            }
        } else if (ctx.subquery() != null) {
            visit(ctx.subquery());
        }
        return null;
    }
    
    /**
     * Visit order item
     */
    @Override
    public Void visitOrderItem(SQLParser.OrderItemContext ctx) {
        visit(ctx.expression());
        if (ctx.ASC() != null) {
            normalizedPattern.append(" ASC");
        } else if (ctx.DESC() != null) {
            normalizedPattern.append(" DESC");
        }
        return null;
    }
    
    /**
     * Visit where clause
     */
    @Override
    public Void visitWhereClause(SQLParser.WhereClauseContext ctx) {
        return visit(ctx.expression());
    }
    
    /**
     * Visit group by clause
     */
    @Override
    public Void visitGroupByClause(SQLParser.GroupByClauseContext ctx) {
        return visit(ctx.expressionList());
    }
    
    /**
     * Visit having clause
     */
    @Override
    public Void visitHavingClause(SQLParser.HavingClauseContext ctx) {
        return visit(ctx.expression());
    }
    
    /**
     * Visit order by clause
     */
    @Override
    public Void visitOrderByClause(SQLParser.OrderByClauseContext ctx) {
        List<SQLParser.OrderItemContext> items = ctx.orderItem();
        for (int i = 0; i < items.size(); i++) {
            visit(items.get(i));
            if (i < items.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        return null;
    }
    
    /**
     * Visit limit clause
     */
    @Override
    public Void visitLimitClause(SQLParser.LimitClauseContext ctx) {
        visit(ctx.integerLiteral().get(0));
        if (ctx.OFFSET() != null && ctx.integerLiteral().size() > 1) {
            normalizedPattern.append(" OFFSET ");
            visit(ctx.integerLiteral().get(1));
        }
        return null;
    }
    
    /**
     * Visit column list
     */
    @Override
    public Void visitColumnList(SQLParser.ColumnListContext ctx) {
        List<SQLParser.ColumnNameContext> columns = ctx.columnName();
        for (int i = 0; i < columns.size(); i++) {
            normalizedPattern.append(columns.get(i).getText());
            if (i < columns.size() - 1) {
                normalizedPattern.append(",");
            }
        }
        return null;
    }
    
    /**
     * Visit column name
     */
    @Override
    public Void visitColumnName(SQLParser.ColumnNameContext ctx) {
        normalizedPattern.append(ctx.getText());
        return null;
    }
    
    // Getters
    public String getNormalizedPattern() {
        return normalizedPattern.toString();
    }
    
    public List<Object> getParameters() {
        return parameters;
    }
    
    public List<ParameterMetadata> getParameterMetadata() {
        return parameterMetadata;
    }
    
    public Set<String> getReferencedTables() {
        return referencedTables;
    }
}
