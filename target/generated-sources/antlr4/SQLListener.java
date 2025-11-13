// Generated from SQL.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SQLParser}.
 */
public interface SQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SQLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SQLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SQLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectStatement(SQLParser.SelectStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectStatement(SQLParser.SelectStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectList}.
	 * @param ctx the parse tree
	 */
	void enterSelectList(SQLParser.SelectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectList}.
	 * @param ctx the parse tree
	 */
	void exitSelectList(SQLParser.SelectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void enterSelectItem(SQLParser.SelectItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#selectItem}.
	 * @param ctx the parse tree
	 */
	void exitSelectItem(SQLParser.SelectItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#tableReferences}.
	 * @param ctx the parse tree
	 */
	void enterTableReferences(SQLParser.TableReferencesContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#tableReferences}.
	 * @param ctx the parse tree
	 */
	void exitTableReferences(SQLParser.TableReferencesContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#tableReference}.
	 * @param ctx the parse tree
	 */
	void enterTableReference(SQLParser.TableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#tableReference}.
	 * @param ctx the parse tree
	 */
	void exitTableReference(SQLParser.TableReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#tableName}.
	 * @param ctx the parse tree
	 */
	void enterTableName(SQLParser.TableNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#tableName}.
	 * @param ctx the parse tree
	 */
	void exitTableName(SQLParser.TableNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#alias}.
	 * @param ctx the parse tree
	 */
	void enterAlias(SQLParser.AliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#alias}.
	 * @param ctx the parse tree
	 */
	void exitAlias(SQLParser.AliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#joinClause}.
	 * @param ctx the parse tree
	 */
	void enterJoinClause(SQLParser.JoinClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#joinClause}.
	 * @param ctx the parse tree
	 */
	void exitJoinClause(SQLParser.JoinClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#subquery}.
	 * @param ctx the parse tree
	 */
	void enterSubquery(SQLParser.SubqueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#subquery}.
	 * @param ctx the parse tree
	 */
	void exitSubquery(SQLParser.SubqueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void enterWhereClause(SQLParser.WhereClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#whereClause}.
	 * @param ctx the parse tree
	 */
	void exitWhereClause(SQLParser.WhereClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void enterGroupByClause(SQLParser.GroupByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#groupByClause}.
	 * @param ctx the parse tree
	 */
	void exitGroupByClause(SQLParser.GroupByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void enterHavingClause(SQLParser.HavingClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#havingClause}.
	 * @param ctx the parse tree
	 */
	void exitHavingClause(SQLParser.HavingClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(SQLParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(SQLParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#orderItem}.
	 * @param ctx the parse tree
	 */
	void enterOrderItem(SQLParser.OrderItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#orderItem}.
	 * @param ctx the parse tree
	 */
	void exitOrderItem(SQLParser.OrderItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void enterLimitClause(SQLParser.LimitClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#limitClause}.
	 * @param ctx the parse tree
	 */
	void exitLimitClause(SQLParser.LimitClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void enterInsertStatement(SQLParser.InsertStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#insertStatement}.
	 * @param ctx the parse tree
	 */
	void exitInsertStatement(SQLParser.InsertStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#insertSelect}.
	 * @param ctx the parse tree
	 */
	void enterInsertSelect(SQLParser.InsertSelectContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#insertSelect}.
	 * @param ctx the parse tree
	 */
	void exitInsertSelect(SQLParser.InsertSelectContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#valueTuple}.
	 * @param ctx the parse tree
	 */
	void enterValueTuple(SQLParser.ValueTupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#valueTuple}.
	 * @param ctx the parse tree
	 */
	void exitValueTuple(SQLParser.ValueTupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#columnList}.
	 * @param ctx the parse tree
	 */
	void enterColumnList(SQLParser.ColumnListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#columnList}.
	 * @param ctx the parse tree
	 */
	void exitColumnList(SQLParser.ColumnListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#columnName}.
	 * @param ctx the parse tree
	 */
	void enterColumnName(SQLParser.ColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#columnName}.
	 * @param ctx the parse tree
	 */
	void exitColumnName(SQLParser.ColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void enterUpdateStatement(SQLParser.UpdateStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#updateStatement}.
	 * @param ctx the parse tree
	 */
	void exitUpdateStatement(SQLParser.UpdateStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#setClause}.
	 * @param ctx the parse tree
	 */
	void enterSetClause(SQLParser.SetClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#setClause}.
	 * @param ctx the parse tree
	 */
	void exitSetClause(SQLParser.SetClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeleteStatement(SQLParser.DeleteStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#deleteStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeleteStatement(SQLParser.DeleteStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#mergeStatement}.
	 * @param ctx the parse tree
	 */
	void enterMergeStatement(SQLParser.MergeStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#mergeStatement}.
	 * @param ctx the parse tree
	 */
	void exitMergeStatement(SQLParser.MergeStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#whenMatched}.
	 * @param ctx the parse tree
	 */
	void enterWhenMatched(SQLParser.WhenMatchedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#whenMatched}.
	 * @param ctx the parse tree
	 */
	void exitWhenMatched(SQLParser.WhenMatchedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#whenNotMatched}.
	 * @param ctx the parse tree
	 */
	void enterWhenNotMatched(SQLParser.WhenNotMatchedContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#whenNotMatched}.
	 * @param ctx the parse tree
	 */
	void exitWhenNotMatched(SQLParser.WhenNotMatchedContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#insertClause}.
	 * @param ctx the parse tree
	 */
	void enterInsertClause(SQLParser.InsertClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#insertClause}.
	 * @param ctx the parse tree
	 */
	void exitInsertClause(SQLParser.InsertClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#withClause}.
	 * @param ctx the parse tree
	 */
	void enterWithClause(SQLParser.WithClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#withClause}.
	 * @param ctx the parse tree
	 */
	void exitWithClause(SQLParser.WithClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#cte}.
	 * @param ctx the parse tree
	 */
	void enterCte(SQLParser.CteContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#cte}.
	 * @param ctx the parse tree
	 */
	void exitCte(SQLParser.CteContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#cteName}.
	 * @param ctx the parse tree
	 */
	void enterCteName(SQLParser.CteNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#cteName}.
	 * @param ctx the parse tree
	 */
	void exitCteName(SQLParser.CteNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(SQLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(SQLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(SQLParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(SQLParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(SQLParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(SQLParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpression(SQLParser.ArithmeticExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpression(SQLParser.ArithmeticExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(SQLParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(SQLParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ColumnRefExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterColumnRefExpr(SQLParser.ColumnRefExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ColumnRefExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitColumnRefExpr(SQLParser.ColumnRefExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpr(SQLParser.ParenthesizedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpr(SQLParser.ParenthesizedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunctionCallExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCallExpr(SQLParser.FunctionCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunctionCallExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCallExpr(SQLParser.FunctionCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CaseExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaseExpr(SQLParser.CaseExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CaseExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaseExpr(SQLParser.CaseExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CastExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpr(SQLParser.CastExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CastExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpr(SQLParser.CastExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ExtractExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtractExpr(SQLParser.ExtractExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExtractExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtractExpr(SQLParser.ExtractExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubqueryExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubqueryExpr(SQLParser.SubqueryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubqueryExpr}
	 * labeled alternative in {@link SQLParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubqueryExpr(SQLParser.SubqueryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(SQLParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(SQLParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#columnReference}.
	 * @param ctx the parse tree
	 */
	void enterColumnReference(SQLParser.ColumnReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#columnReference}.
	 * @param ctx the parse tree
	 */
	void exitColumnReference(SQLParser.ColumnReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(SQLParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(SQLParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(SQLParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(SQLParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(SQLParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(SQLParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(SQLParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#floatLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(SQLParser.FloatLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBooleanLiteral(SQLParser.BooleanLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#booleanLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBooleanLiteral(SQLParser.BooleanLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#dateLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDateLiteral(SQLParser.DateLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#dateLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDateLiteral(SQLParser.DateLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#nullLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNullLiteral(SQLParser.NullLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#nullLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNullLiteral(SQLParser.NullLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#literalList}.
	 * @param ctx the parse tree
	 */
	void enterLiteralList(SQLParser.LiteralListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#literalList}.
	 * @param ctx the parse tree
	 */
	void exitLiteralList(SQLParser.LiteralListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticOperator(SQLParser.ArithmeticOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#arithmeticOperator}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticOperator(SQLParser.ArithmeticOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(SQLParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(SQLParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(SQLParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(SQLParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(SQLParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(SQLParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(SQLParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(SQLParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void enterWindowSpec(SQLParser.WindowSpecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#windowSpec}.
	 * @param ctx the parse tree
	 */
	void exitWindowSpec(SQLParser.WindowSpecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(SQLParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(SQLParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#partitionByClause}.
	 * @param ctx the parse tree
	 */
	void enterPartitionByClause(SQLParser.PartitionByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#partitionByClause}.
	 * @param ctx the parse tree
	 */
	void exitPartitionByClause(SQLParser.PartitionByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void enterWindowFrame(SQLParser.WindowFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#windowFrame}.
	 * @param ctx the parse tree
	 */
	void exitWindowFrame(SQLParser.WindowFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#lowerBound}.
	 * @param ctx the parse tree
	 */
	void enterLowerBound(SQLParser.LowerBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#lowerBound}.
	 * @param ctx the parse tree
	 */
	void exitLowerBound(SQLParser.LowerBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#upperBound}.
	 * @param ctx the parse tree
	 */
	void enterUpperBound(SQLParser.UpperBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#upperBound}.
	 * @param ctx the parse tree
	 */
	void exitUpperBound(SQLParser.UpperBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#caseExpression}.
	 * @param ctx the parse tree
	 */
	void enterCaseExpression(SQLParser.CaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#caseExpression}.
	 * @param ctx the parse tree
	 */
	void exitCaseExpression(SQLParser.CaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void enterWhenClause(SQLParser.WhenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#whenClause}.
	 * @param ctx the parse tree
	 */
	void exitWhenClause(SQLParser.WhenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#elseClause}.
	 * @param ctx the parse tree
	 */
	void enterElseClause(SQLParser.ElseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#elseClause}.
	 * @param ctx the parse tree
	 */
	void exitElseClause(SQLParser.ElseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(SQLParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(SQLParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(SQLParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(SQLParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#extractExpression}.
	 * @param ctx the parse tree
	 */
	void enterExtractExpression(SQLParser.ExtractExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#extractExpression}.
	 * @param ctx the parse tree
	 */
	void exitExtractExpression(SQLParser.ExtractExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#dateField}.
	 * @param ctx the parse tree
	 */
	void enterDateField(SQLParser.DateFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#dateField}.
	 * @param ctx the parse tree
	 */
	void exitDateField(SQLParser.DateFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#inPredicate}.
	 * @param ctx the parse tree
	 */
	void enterInPredicate(SQLParser.InPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#inPredicate}.
	 * @param ctx the parse tree
	 */
	void exitInPredicate(SQLParser.InPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#existsPredicate}.
	 * @param ctx the parse tree
	 */
	void enterExistsPredicate(SQLParser.ExistsPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#existsPredicate}.
	 * @param ctx the parse tree
	 */
	void exitExistsPredicate(SQLParser.ExistsPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#betweenPredicate}.
	 * @param ctx the parse tree
	 */
	void enterBetweenPredicate(SQLParser.BetweenPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#betweenPredicate}.
	 * @param ctx the parse tree
	 */
	void exitBetweenPredicate(SQLParser.BetweenPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#likePredicate}.
	 * @param ctx the parse tree
	 */
	void enterLikePredicate(SQLParser.LikePredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#likePredicate}.
	 * @param ctx the parse tree
	 */
	void exitLikePredicate(SQLParser.LikePredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#isNullPredicate}.
	 * @param ctx the parse tree
	 */
	void enterIsNullPredicate(SQLParser.IsNullPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#isNullPredicate}.
	 * @param ctx the parse tree
	 */
	void exitIsNullPredicate(SQLParser.IsNullPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link SQLParser#quantifiedPredicate}.
	 * @param ctx the parse tree
	 */
	void enterQuantifiedPredicate(SQLParser.QuantifiedPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link SQLParser#quantifiedPredicate}.
	 * @param ctx the parse tree
	 */
	void exitQuantifiedPredicate(SQLParser.QuantifiedPredicateContext ctx);
}