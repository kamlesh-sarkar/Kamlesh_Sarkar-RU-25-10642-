// Function Declaration
function add(a, b) {
    return a + b;
}


const multiply = function(a, b) {
    return a * b;
};


const greet = name => `Hello, ${name}!`;


console.log("Addition:", add(5, 3));          
console.log("Multiplication:", multiply(4, 2)); 
console.log(greet("Alice"));                  




function scopeDemo() {
    if (true) {
        var x = "I am var";     
        let y = "I am let";     
        const z = "I am const"; 

        console.log("Inside block:", x); 
        console.log("Inside block:", y); 
        console.log("Inside block:", z); 
    }

    console.log("Outside block:", x); 
}
scopeDemo();
