%{
#include <stdio.h>
#include <string.h>
#define YYDEBUG 1

int yylex();
void yyerror(char *s);
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
%token AND
%token OR
%token NOT
%token STARTS
%token FROM
%token TRANSFORMS
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

%%

program:    stmt ';'
        |   stmt ';' program
        ;
stmt:   declaration
    |   simplstmt
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
declaration_and_assignment: DEFINE type IDENTIFIER = constant
    ;
constant:   INTEGER
        |   CHARACTER
        |   STRING
        ;
array_declaration:  ARRR OF integer_constant_or_identifier IDENTIFIER
    ;
integer_constant_or_identifier: INTEGER
                            |   IDENTIFIER
                            ;
simplstmt:  assignstmt
        |   iostmt
        ;


%%

void yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

int main(int argc, char** argv){
    if(argc>1) 
        yyin = fopen(argv[1], "r");
    if(argc>2 && !strcmp(argv[2], "-d"))
        yydebug = 1;
    if(!yyparse())
        fprintf(stderr, "\tOK.\n");
    return 0;
}
