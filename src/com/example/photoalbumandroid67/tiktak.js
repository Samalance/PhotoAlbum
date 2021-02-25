//Tiktackto the videogame
//by Samuel Chang
//Practice for the real world
//Started 2/12/2021

const readLine = require('readline');

//later after getting it to work in the console change it to work in the chrome browser

/*
GAMEPLAN:
Going to have 2 players setup with the ability to send a link to someone and play tik tak toe with someone you share the link to. 
Once connected the link will still be availiable but only as a spectator. Player 1 gets dibs on X or O.
*/
let winner = 0;
//create the tik tak toe board. Holds X's O's or Blanks. Can only enter blanks
let board = new Array(2, 2);

//need to create a enter function to place an x or o in the board
//Also need to make sure that there's a fucntion call at the end of a move to check for if there is a win
//Think it would be cool to make it so that it can calculate if there's no possible way for either player to win (TieCheck)
/*
    x x x
    x x x
    x x x
*/
//For making the move
function MakeMove(x,y){

}

//Creating player Object for the 2 players in tik tak toe. Can be used for other games as a baseline
function Player( wins, losses, username,choice) {
    this.wins = wins;
    this.losses = losses;
    this.username = username;
    this.choice = choice;
}

//Forchecking the win. Wanted to get it done in one shot so checking for row/column/diagnal all at once
function CheckWin() {
    //need to check to see if board has a winning board.
    //check horizontal
    //check3 = if there's 3 in a row
    let hCheck3 = 0;
    let vCheck3 = 0;
    let diagCheck3 = 0;
    let reverseDiagCheck3 = 0;

    for (i = 0; i < board.length; ++i) {
        for (j = 0; j < board.length; ++j) {
            if (Player.choice == board[i][j]) {
                hCheck3++;
            }
            if (Player.choice == board[j][i]) {
                vCheck3++;
            }
        }
        if (Player.choice == board[j][i]) {
            diagCheck3++;
        }
        if (Player.choice == board[i][j]) {
            reverseDiagCheck3++;
        }

        //after checking see if the winner exists.
        if (hCheck3 == 3 || vCheck3 == 3 || diagCheck3 == 3 || reverseDiagCheck3 == 0) {
            winner = 1;
            player.wins++;
        }

    }
}
//create player1
let player1 = new Player(0,0,"",'X');
//create player 2
let player2 = new Player( 0, 0, "", 'O');

let turn = 1;
let x = 0;
let y = 0;

console.log("Player " + turn + " , Please enter where you would like to place your " + player1.choice);

x = window.prompt("Row:");
y = window.prompt("Column:");


