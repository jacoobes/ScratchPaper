# ScratchPaper
## A Minecraft plugin with a Math DSL
> I hate pulling out my calculator to do math for my redstone projects
```
cmp(1,5,2)
```
> `cmp()` compare signal strengths, just like a comparator
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
  
## Building the JAR

To build the JAR file using Gradle, follow these steps:

1. Ensure you have Gradle installed. Usually Intellij has it built into the IDE.
   - If not, you can install following these instructions [here](https://gradle.org/install/).
3. Open a terminal and navigate to the root directory of the repository.
4. Run the following command to build the JAR file:
   ```sh
   ./gradlew build
5. If you are using intellij, run the build script might be easier

## Contributing
- [Open Issues](https://github.com/jacoobes/ScratchPaper/issues)
- [Read this](./CONTRIBUTING.md)
- Open a pull request.
- I'll review it!
