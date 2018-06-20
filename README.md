# SudokuSolver
A java console programme that is derived from my repository called SudokuGame. This programme will allow you to type in a string representing a valid board of Sudoku, and the programme will determine whether the Sudoku is solvable or not. If it is solvable, the programme will display the solved Sudoku to the user.

# Introduction
## Currently the only way to solve your own Sudoku is to open the source code and change one of the difficulty board and then choose 'I give up' later to show the solution. The better interface will be implemented later.

This version has an added recursion brute force solver that I got inspired online by baeldung on the website of http://www.baeldung.com/java-sudoku. The recursion solver implements backtracking and tries all the combinations possible on this board and eventually finding the final solution. This allows the programme to be able to solve any valid Sudoku board given by the user possible and whenever it is not possible to solve it would show the user that this board is not solved. There will be a brute force solver that will do the same thing but instead of using the dangerous tail recursion I will be using a while loop instead of tail recursion which should make the solver faster and more efficient.

### ALL THE FEATURES WILL BE IMPLEMENTED LATER...

This programme starts off to let the user to type in a string of a Sudoku board. A valid string of a Sudoku board is that there is no repeat of numbers in a row, column, and cell. The programme will check that automatically and if the user input doesn't satisfy the requirements, the programme will exit before it even starts trying to solve the Sudoku.

Also, the programme was added a new bruteForce() method, which is a brute force solver for the Sudoku board that is not using recursion. It is a back tracking method but instead of using recursion I decided to use while loop instead. There is a variable in the method that stores the previous value of a certain block, and then whenever it realize that at some block that all numbers from 1 to 9 won't be able to fill in, it will back track to the previous blocks and try different numbers that are after the stored value on that block before. If the solver couldn't solve a Sudoku, this means that the Sudoku board can only be invalid to be unsolveable because the brute force method will check all possible combination of a certain board, and if the board has no matching combination, this means that there is a mistake in this certain board.

The user interface has completely changed from the game. Instead of having to choose difficulty and fill in a value to a Sudoku board, there is no need for that as the user is not going to be trying to fill in any value for the board and trying to solve it themselves, and instead the machine is going to do that for the user. The user will be prompted to type in the string for Sudoku board, which is comprised with numbers ranging from 0 to 9. Whenever there is an empty block in the Sudoku board that the user wants to solve, the user will replace that with a number 0, and it doesn't matter if the user types in the board like this (000000000) or (000 000 000), the machine would turn it into a valid board anyways. If the user typed string with all the spaces removed isn't 81 characters long that is comprised with all numbers, the machine would tell the user that this is an invalid input, and ask the user to try again. After the machine verifies that the board is valid, it will display the Sudoku board to the user to let the user verify if the board is correct or not. If the user choose that it doesn't match their Sudoku that wants to be solves, and they select the option that says 'no', the programme would quit and if the user will try to solve again they would have to restart the programme. If they choose 'yes' and they confirmed that the board is correct, the machine would try to solve it using the logic method first, which analyses the board in the process of doing it, and that is what allows the brute force solver to be able to solve the Sudoku, and after all the solver is complete, the machine would display out the board that is solved so the user can see what it looks like.

# Solver Introduction
### Why Does Recursion Works
This brute force solver that is currently implemented uses recursion to solve the Sudoku board. The recursive method works because of the stacking of the function calls. When a function stack is "stacked" on top of another function stack, the location and every variable in the function is stored, which means the value and the location of a certain block is going to be stored there. Whenever a recursive method doesn't satisfy a certain condition, the solver will keep on going back or "up the stack" until one of the stack will work and make it keep going. This will eventually finish until the end of the Sudoku where the board is solved. 

### Sorted Available Blocks vs Not Sorted
The available blocks that can be filled in in the board that the user choose to input is all stored in an array, and its informations will be stored there as well, there is a separated class dedicated to that called EmptyBlockInfo. That class would figure out what numbers could be filled in a block, the location of the block in the board, as well as how many different numbers can be filled in that specific block. When a block is not sorted so that it starts from the first empty block to the last empty block in the board, the solver would take significantly longer time than a sorted empty blocks array. This is because whenever a block is placed in front that would be easier for a recursive solver to solve. For example, a block with 2 different possibilities will be tried to be filled in with 1 number, and after that number is filled in there is a block with 6 different possibilities that needs to be solved. When the second block can't be filled in any number, the recursive method only has to go up the stack for once because the first number has only 2 ways of filling in something. If the thing is in reverse order, when the recursion method is calling the function itself it consumes a lot of resources, and the function has to be called a maximum of 6 times at the worst case scenario. This will make the solver significantly more efficient despite the sort method itself taking over 40 mililseconds to complete. In an actual benchmark the unsorted Sudoku emptyblock array took about 1100 miliseconds or 1.1 seconds to completely and correctly solve the board, which will cause significant 'lag' when the user is using the programme as it is not telling the user what is happening. However, after sorting the programme would take about 160 miliseconds or 0.16 seconds to complete the solving process, which means it is significantly more efficient. In extreme situations, even after the array is sorted, the solver would take an astonishingly long time like 8 seconds to solve a board. The unsorted ones wouldn't even be able to solve it in a reasonable amount of time because it is way too slow, and this is why I am combining the old logical solver to find some points that definitely have to be there to eliminate a lot of possibilities and sometimes even solving the Sudoku all together if it only has 1 solution. Thus, the speed will be drastically improved after both solver is used at the same time and giving the user a better experience.

### How Does The Speed Compare to Iterative Solver
I am still working on the iteration solver currently, and the iteration solver works on the same principle, whenever it finds a block that can't find all the numbers, it will store that block value down and then start going backwards. When the block works, it will keep on going and going until the board is solved. It uses a while loop which make it basically function like recursion but is way way less resource intensive. With current benchmarks the sorted recursion method usually takes about 160-200 miliseconds to solve a board while the iteration method uses about 40-50 miliseconds. This indicates that the iteration solver is about 4 times faster than the recursive solver, which makes the user interface much snappier as the computer wouldn't spend much time at all trying to solve the Sudoku.
