# ScratchPaper
## A Minecraft plugin with a Math DSL
> I hate pulling out my calculator to do math for my redstone projects
```
fn slope(m,x,b) = m*x+b;
slope(1,5,2)
```
## Usage
```
/eval <program>
```
## Features
- Add, subtract, multiply, divide numbers in the minecraft commandline.
- Create simple functions to remove redundant routines.
- Bind native functions 
  - sin,cos,min,max, and more! 
  - PR's needed to add more.


## [Syntax](./src/main/antlr/Expr.g4) :
### Program
A program consists of zero or more functions and an optional expression.
If no expression exists, **0 is the result**. 
### Functions
```
fn ident(id, id1, id2) = expr
```
```
fn ident() = expr
```
### Expressions
Basically, any math calculator, positive numbers only!
```js
1 + 2 - 5
``` 
### Function calls
``` js
sin(rad(0)) 
```
- yields `.49999`

### Constants
Built in constants are `PI`, `E`, `CHUNK`, `SIG_MAX`
- A CHUNK is 16 ( in minecraft )
- SIG_MAX is 15 (the maximum signal strength of redstone)

### Calculate signal strength
- natives `cmp`, `cmp_sub`



## Developing
- This is a gradle project, and two main scripts exist
  - I use intellij to simplify the workflow.
- Running on graal jdk 21 while developing.
- generateGrammarSource
- build

## Contributing
- [Open Issues](https://github.com/jacoobes/ScratchPaper/issues)
- [Read this](./CONTRIBUTING.md)
- Open a pull request.
- I'll review it!