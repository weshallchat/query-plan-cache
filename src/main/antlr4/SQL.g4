grammar SQL;

// Parser Rules

statement
    : withClause
    | selectStatement
    | insertStatement
    | updateStatement
    | deleteStatement
    | mergeStatement
    ;

// SELECT Statement
selectStatement
    : SELECT DISTINCT? selectList
      FROM tableReferences
      (joinClause)*
      (whereClause)?
      (groupByClause)?
      (havingClause)?
      (orderByClause)?
      (limitClause)?
    ;

selectList
    : selectItem (',' selectItem)*
    | STAR
    ;

selectItem
    : tableName '.' STAR
    | STAR
    | expression (AS? alias)?
    ;

tableReferences
    : tableReference (',' tableReference)*
    ;

tableReference
    : tableName (AS? alias)?
    | subquery
    ;

tableName
    : IDENTIFIER ('.' IDENTIFIER)?
    ;

alias
    : IDENTIFIER
    ;

joinClause
    : (INNER | LEFT | RIGHT | FULL OUTER?)? JOIN tableName (AS? alias)? (ON expression)?
    ;

subquery
    : '(' selectStatement ')' (AS? alias)?
    ;

whereClause
    : WHERE expression
    ;

groupByClause
    : GROUP BY expressionList
    ;

havingClause
    : HAVING expression
    ;

orderByClause
    : ORDER BY orderItem (',' orderItem)*
    ;

orderItem
    : expression (ASC | DESC)?
    ;

limitClause
    : LIMIT integerLiteral (OFFSET integerLiteral)?
    ;

// INSERT Statement
insertStatement
    : INSERT INTO tableName ('(' columnList ')')?
      (valueTuple | insertSelect)
    ;

insertSelect
    : selectStatement
    ;

valueTuple
    : VALUES '(' expressionList ')'
    ;

columnList
    : columnName (',' columnName)*
    ;

columnName
    : IDENTIFIER ('.' IDENTIFIER)?
    ;

// UPDATE Statement
updateStatement
    : UPDATE tableName SET setClause (',' setClause)* (whereClause)?
    ;

setClause
    : columnName '=' expression
    ;

// DELETE Statement
deleteStatement
    : DELETE FROM tableName (whereClause)?
    ;

// MERGE Statement
mergeStatement
    : MERGE INTO tableName AS? alias?
      USING tableName AS? alias?
      ON expression
      (whenMatched | whenNotMatched)*
    ;

whenMatched
    : WHEN MATCHED THEN (updateStatement | deleteStatement)
    ;

whenNotMatched
    : WHEN NOT MATCHED THEN insertClause
    ;

insertClause
    : INSERT ('(' columnList ')')? VALUES '(' expressionList ')'
    ;

// WITH Clause (CTE)
withClause
    : WITH RECURSIVE? cte (',' cte)* selectStatement
    ;

cte
    : cteName ('(' columnList ')')? AS '(' selectStatement ')'
    ;

cteName
    : IDENTIFIER
    ;

// Expressions
expression
    : logicalExpression
    ;

logicalExpression
    : comparisonExpression (logicalOperator comparisonExpression)*
    ;

comparisonExpression
    : arithmeticExpression (comparisonOperator arithmeticExpression)?
    | inPredicate
    | existsPredicate
    | betweenPredicate
    | likePredicate
    | isNullPredicate
    | quantifiedPredicate
    ;

arithmeticExpression
    : unaryExpression (arithmeticOperator unaryExpression)*
    ;

unaryExpression
    : literal                                    # LiteralExpr
    | columnReference                           # ColumnRefExpr
    | '(' expression ')'                        # ParenthesizedExpr
    | functionCall                              # FunctionCallExpr
    | caseExpression                            # CaseExpr
    | castExpression                            # CastExpr
    | extractExpression                         # ExtractExpr
    | subquery                                  # SubqueryExpr
    ;

expressionList
    : expression (',' expression)*
    ;

columnReference
    : IDENTIFIER ('.' IDENTIFIER)?
    ;

// Literals
literal
    : stringLiteral
    | integerLiteral
    | floatLiteral
    | booleanLiteral
    | dateLiteral
    | nullLiteral
    ;

stringLiteral
    : STRING
    ;

integerLiteral
    : INTEGER
    ;

floatLiteral
    : FLOAT
    ;

booleanLiteral
    : TRUE | FALSE
    ;

dateLiteral
    : DATE STRING
    | TIMESTAMP STRING
    ;

nullLiteral
    : NULL
    ;

literalList
    : literal (',' literal)*
    ;

// Operators
arithmeticOperator
    : '+' | '-' | '*' | '/' | '%'
    ;

comparisonOperator
    : '=' | '!=' | '<>' | '<' | '<=' | '>' | '>='
    ;

logicalOperator
    : AND | OR | NOT
    ;

// Functions
// scalarFunction
//     : functionName '(' (argumentList | STAR)? ')'
//     ;

functionName
    : IDENTIFIER
    ;

argumentList
    : expression (',' expression)*
    ;

// aggregateFunction
//     : functionName '(' (DISTINCT)? (expression | STAR)? ')'
//     ;

// windowFunction
//     : functionName '(' (argumentList)? ')' windowSpec?
//     ;

windowSpec
    : OVER '(' (partitionByClause)? (orderByClause)? (windowFrame)? ')'
    ;

functionCall
    : functionName '(' DISTINCT? (argumentList | expression | STAR)? ')' windowSpec?
    ;

partitionByClause
    : PARTITION BY expressionList
    ;

windowFrame
    : (ROWS | RANGE) (BETWEEN lowerBound AND upperBound | CURRENT ROW)
    ;

lowerBound
    : UNBOUNDED PRECEDING | integerLiteral PRECEDING | CURRENT ROW
    ;

upperBound
    : UNBOUNDED FOLLOWING | integerLiteral FOLLOWING | CURRENT ROW
    ;

caseExpression
    : CASE (caseValue=expression)? (whenClause)+ (elseClause)? END
    ;

whenClause
    : WHEN condition=expression THEN result=expression
    ;

elseClause
    : ELSE result=expression
    ;

castExpression
    : CAST '(' expression AS dataType ')'
    ;

dataType
    : IDENTIFIER ('(' INTEGER (',' INTEGER)? ')')?
    ;

extractExpression
    : EXTRACT '(' dateField FROM expression ')'
    ;

dateField
    : YEAR | MONTH | DAY | HOUR | MINUTE | SECOND
    ;

// Predicates
inPredicate
    : arithmeticExpression NOT? IN '(' (expressionList | selectStatement) ')'
    ;

existsPredicate
    : EXISTS subquery
    ;

betweenPredicate
    : arithmeticExpression NOT? BETWEEN lower=arithmeticExpression AND upper=arithmeticExpression
    ;

likePredicate
    : arithmeticExpression NOT? LIKE pattern=arithmeticExpression (ESCAPE escapeChar=arithmeticExpression)?
    ;

isNullPredicate
    : arithmeticExpression IS NOT? NULL
    ;

quantifiedPredicate
    : arithmeticExpression comparisonOperator (ANY | ALL | SOME) subquery
    ;

// Lexer Rules

// Keywords
SELECT : S E L E C T;
FROM : F R O M;
WHERE : W H E R E;
GROUP : G R O U P;
BY : B Y;
HAVING : H A V I N G;
ORDER : O R D E R;
ASC : A S C;
DESC : D E S C;
LIMIT : L I M I T;
OFFSET : O F F S E T;
DISTINCT : D I S T I N C T;
AS : A S;
JOIN : J O I N;
INNER : I N N E R;
LEFT : L E F T;
RIGHT : R I G H T;
FULL : F U L L;
OUTER : O U T E R;
ON : O N;
INSERT : I N S E R T;
INTO : I N T O;
VALUES : V A L U E S;
UPDATE : U P D A T E;
SET : S E T;
DELETE : D E L E T E;
MERGE : M E R G E;
USING : U S I N G;
WHEN : W H E N;
MATCHED : M A T C H E D;
THEN : T H E N;
WITH : W I T H;
RECURSIVE : R E C U R S I V E;
AND : A N D;
OR : O R;
NOT : N O T;
IN : I N;
EXISTS : E X I S T S;
BETWEEN : B E T W E E N;
LIKE : L I K E;
ESCAPE : E S C A P E;
IS : I S;
NULL : N U L L;
TRUE : T R U E;
FALSE : F A L S E;
CASE : C A S E;
ELSE : E L S E;
END : E N D;
CAST : C A S T;
EXTRACT : E X T R A C T;
YEAR : Y E A R;
MONTH : M O N T H;
DAY : D A Y;
HOUR : H O U R;
MINUTE : M I N U T E;
SECOND : S E C O N D;
PARTITION : P A R T I T I O N;
OVER : O V E R;
ROWS : R O W S;
RANGE : R A N G E;
PRECEDING : P R E C E D I N G;
FOLLOWING : F O L L O W I N G;
CURRENT : C U R R E N T;
ROW : R O W;
UNBOUNDED : U N B O U N D E D;
ANY : A N Y;
ALL : A L L;
SOME : S O M E;
DATE : D A T E;
TIMESTAMP : T I M E S T A M P;

// Operators and Symbols
STAR : '*';
PLUS : '+';
MINUS : '-';
DIVIDE : '/';
MOD : '%';
EQUALS : '=';
NOT_EQUALS : '!=';
NOT_EQUALS_ALT : '<>';
LT : '<';
LE : '<=';
GT : '>';
GE : '>=';

// Literals
STRING
    : '\'' ( ~'\'' | '\'\'' )* '\''
    | '"' ( ~'"' | '""' )* '"'
    ;

INTEGER
    : [0-9]+
    ;

FLOAT
    : [0-9]+ '.' [0-9]* EXPONENT?
    | '.' [0-9]+ EXPONENT?
    | [0-9]+ EXPONENT
    ;

fragment EXPONENT : [eE] [+-]? [0-9]+;

IDENTIFIER
    : [a-zA-Z_][a-zA-Z0-9_]*
    | '`' ~'`'+ '`'
    | '[' ~']'+ ']'
    ;

// Whitespace and Comments
WS : [ \t\r\n]+ -> skip;

LINE_COMMENT
    : '--' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;

// Fragment rules for case-insensitive keywords
fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

