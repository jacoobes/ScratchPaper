grammar Expr;
prog:   def* expr?;
expr
    :   expr op=('*'|'/') expr #opexpr
    |   expr op=('+'|'-') expr #opexpr
    |   number                 #int
    |   call                   #funcall
    |   ID                     #id
    |   '(' expr ')'           #paren
    ;

number : (FLOAT | INT);

arglist :  ID (',' ID)*
        | //episolon
        ;

explist : expr (',' expr)*
        | //episolon
        ;
def : 'fn' name=ID '(' parms=arglist ')' '=' body=expr ';';
call : name=ID '(' args=explist  ')';

FLOAT   : INT+ '.' INT*
        | '.' INT+
        ;

NEWLINE : [\r\n ]+ -> skip ;
INT     : [0-9]+;
ID      : [a-zA-Z]+;