# Write a shell script that prints the sum of n even numbers.
# The limit is entered through the command line arguments (using functions)
calculate()#function to calculate
{
    sum=0
    a=0
    b=`expr $1 + 1`
    while [ "$a" -lt "$b" ]
    do
        sum=`expr $sum + $a ` # sum value is added
        a=`expr $a + 2 ` # increment by 2
    done
    echo "The sum is $sum" # Display sum
}
echo "Enter the limit :"
read n # input
calculate $n # function call
}
