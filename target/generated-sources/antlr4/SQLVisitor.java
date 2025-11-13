// Generated from SQL.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SQLParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SQLParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectStatement(SQLParser.SelectStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectList(SQLParser.SelectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#selectItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectItem(SQLParser.SelectItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#tableReferences}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableReferences(SQLParser.TableReferencesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#tableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableReference(SQLParser.TableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#tableName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTableName(SQLParser.TableNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#alias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlias(SQLParser.AliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#joinClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoinClause(SQLParser.JoinClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#subquery}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubquery(SQLParser.SubqueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#whereClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhereClause(SQLParser.WhereClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#groupByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupByClause(SQLParser.GroupByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#havingClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHavingClause(SQLParser.HavingClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#orderByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderByClause(SQLParser.OrderByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#orderItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderItem(SQLParser.OrderItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#limitClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLimitClause(SQLParser.LimitClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#insertStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertStatement(SQLParser.InsertStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#insertSelect}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertSelect(SQLParser.InsertSelectContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#valueTuple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueTuple(SQLParser.ValueTupleContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#columnList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnList(SQLParser.ColumnListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#columnName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnName(SQLParser.ColumnNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#updateStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdateStatement(SQLParser.UpdateStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#setClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetClause(SQLParser.SetClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#deleteStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteStatement(SQLParser.DeleteStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#mergeStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMergeStatement(SQLParser.MergeStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#whenMatched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenMatched(SQLParser.WhenMatchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#whenNotMatched}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenNotMatched(SQLParser.WhenNotMatchedContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#insertClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertClause(SQLParser.InsertClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#withClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithClause(SQLParser.WithClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#cte}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCte(SQLParser.CteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#cteName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCteName(SQLParser.CteNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(SQLParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#logicalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(SQLParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(SQLParser.ComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpression(SQLParser.ArithmeticExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(SQLParser.LiteralExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ColumnRefExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnRefExpr(SQLParser.ColumnRefExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpr(SQLParser.ParenthesizedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunctionCallExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCallExpr(SQLParser.FunctionCallExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CaseExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpr(SQLParser.CaseExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CastExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpr(SQLParser.CastExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExtractExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractExpr(SQLParser.ExtractExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubqueryExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubqueryExpr(SQLParser.SubqueryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(SQLParser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#columnReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnReference(SQLParser.ColumnReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(SQLParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(SQLParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#integerLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(SQLParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#floatLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLiteral(SQLParser.FloatLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#booleanLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanLiteral(SQLParser.BooleanLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#dateLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateLiteral(SQLParser.DateLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#nullLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullLiteral(SQLParser.NullLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#literalList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralList(SQLParser.LiteralListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticOperator(SQLParser.ArithmeticOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonOperator(SQLParser.ComparisonOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOperator(SQLParser.LogicalOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(SQLParser.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(SQLParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#windowSpec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowSpec(SQLParser.WindowSpecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(SQLParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#partitionByClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartitionByClause(SQLParser.PartitionByClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#windowFrame}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWindowFrame(SQLParser.WindowFrameContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#lowerBound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLowerBound(SQLParser.LowerBoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#upperBound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpperBound(SQLParser.UpperBoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#caseExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaseExpression(SQLParser.CaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#whenClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhenClause(SQLParser.WhenClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#elseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseClause(SQLParser.ElseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#castExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpression(SQLParser.CastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(SQLParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#extractExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExtractExpression(SQLParser.ExtractExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#dateField}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateField(SQLParser.DateFieldContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#inPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInPredicate(SQLParser.InPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#existsPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsPredicate(SQLParser.ExistsPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#betweenPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetweenPredicate(SQLParser.BetweenPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#likePredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLikePredicate(SQLParser.LikePredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#isNullPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsNullPredicate(SQLParser.IsNullPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link SQLParser#quantifiedPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuantifiedPredicate(SQLParser.QuantifiedPredicateContext ctx);
}