%{
#include <stdio.h>
#include <string.h>
#define YYDEBUG 1

extern int yylineno;
extern char* yytext;
int yylex();
void yyerror(const char *s);
%}

%token INT
%token ALPHA
%token ARRR
%token FIBRE
%token MAKE
%token IF
%token FI
%token OF
%token PRGR
%token READ
%token SHOW
%token DEFINE
%token NOW
%token PERSISTENT
%token FOR
%token WHILE
%token STARTS
%token FROM
%token TRANSFORMS
%token INTO
%token STOPS
%token AT
%token STDIN
%token STDOUT

%token IDENTIFIER
%token INTEGER
%token STRING
%token CHARACTER

%token LE
%token GE
%token EQ
%token INCREMENT
%token DECREMENT

%left '+' '-'
%left '/' '%' '*'
%left OR
%left AND
%left NOT

%%

program: stmtlist
stmtlist:   stmt
        |   stmt stmtlist
        ;
stmt:   declaration ';'
    |   simplstmt ';'
    |   structstmt
    ;
declaration:    DEFINE type IDENTIFIER
            |   declaration_and_assignment
            |   array_declaration
            ;
type:   ALPHA
    |   FIBRE
    |   INT
    ;
declaration_and_assignment: DEFINE type IDENTIFIER '=' constant
    ;
constant:   INTEGER
        |   CHARACTER
        |   STRING
        ;
array_declaration: DEFINE ARRR OF integer_constant_or_identifier IDENTIFIER
    ;
integer_constant_or_identifier: INTEGER
                            |   IDENTIFIER
                            ;
simplstmt:  assignstmt
        |   iostmt
        |   increment_or_decrement_variable
        ;
assignstmt: IDENTIFIER '='  expression
        |   array_access '=' expression
expression:  expression '+' term
        |   expression '-' term
        |   term
        ;
term:   term '*' factor
    |   term '/' factor
    |   term '%' factor
    |   factor
    ;
factor: '(' expression ')'
    | integer_constant_or_identifier
    ;
iostmt: readstmt
    |   showstmt
    ;
readstmt:   READ '(' identifier_or_arrayaccess ',' channel ')'
    ;
identifier_or_arrayaccess:  IDENTIFIER
                        |   array_access
                        ;
array_access: IDENTIFIER '[' integer_constant_or_identifier ']'
    ;
channel:    STDIN
        |   STDOUT
        ;
showstmt:   SHOW '(' variable_or_constant ',' channel ')'
    ;
variable_or_constant:   identifier_or_arrayaccess
                    |   constant
                    ;
structstmt: |   ifstmt
            |   whilestmt
            |   forstmt
            ;
ifstmt: IF condition '{' stmtlist '}' 
    |   IF condition '{' stmtlist '}' FI '{' stmtlist '}'
    ;
whilestmt:  WHILE condition '{' stmtlist '}'
    ;
forstmt:    FOR IDENTIFIER STARTS FROM variable_or_constant TRANSFORMS INTO assignstmt STOPS AT condition '{' stmt '}'
    ;
condition:  expression relation condition
        |   expression relation expression
        ;
relation:   '<'
        |   '>'
        |   EQ
        |   GE
        |   LE
        |   AND
        |   OR
        |   NOT
        ;
increment_or_decrement_variable:    identifier_or_arrayaccess increment_or_decrement variable_or_constant
    ;
increment_or_decrement: INCREMENT
                    |   DECREMENT
                    ;
   

%%

void yyerror(const char *s)
{
  printf("%s on line: %d for token: %s\n", s, yylineno, yytext);
}

extern FILE *yyin;

int main(int argc, char** argv){
    if(argc>1) 
        yyin = fopen(argv[1], "r");
    if(argc>2 && !strcmp(argv[2], "-d"))
        yydebug = 1;
    if(!yyparse())
        fprintf(stderr, "Syntactically correct.\n");
    return 0;
}
