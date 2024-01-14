%{
#include <stdio.h>
#include <string.h>
#define YYDEBUG 1

int yylex();
void yyerror(char *s);
%}

%token BISON

%%

start:  BISON   {printf("Found a bison!\n");}
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
