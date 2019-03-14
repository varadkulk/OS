# Write a shell script to print the contents of a specified file.
# The file name should be passed as a command line argument.
echo "Enter the file name to print:"
read file_name # File name input
if [ ! -f "$file_name" ]; # Condition Statement
then
  echo "'$file_name' not found"
else
  echo "The contents of '$file_name' are:"
  head $file_name # Display of contents of file
fi
