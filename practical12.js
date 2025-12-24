
let number = 15;

if (number > 20) {
    console.log("The number is greater than 20.");
} else if (number === 20) {
    console.log("The number is exactly 20.");
} else {
    console.log("The number is less than 20.");
}

// Using switch
let grade = "B";

switch (grade) {
    case "A":
        console.log("Excellent!");
        break;
    case "B":
        console.log("Good job!");
        break;
    case "C":
        console.log("You passed.");
        break;
    default:
        console.log("Invalid grade.");
}


// FOR LOOP


console.log("For loop output:");
for (let i = 1; i <= 5; i++) {
    console.log("Iteration:", i);
}


// WHILE LOOP


console.log("While loop output:");
let count = 1;

while (count <= 5) {
    console.log("Count:", count);
    count++;
}


// DO...WHILE LOOP


console.log("Do...while loop output:");
let x = 1;

do {
    console.log("Value:", x);
    x++;
} while (x <= 5);
