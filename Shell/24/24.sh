echo "Enter the file name:"
read file_name
if [ ! -f "$file_name" ];
then
  echo "'$file_name' not found"
else
  echo "The permissions for '$file_name' are:"
  if [ -w "$file_name" ];
  then
    echo "Write: Yes"
  else
    echo "Write: No"
  fi
  if [ -x "$file_name" ];
  then
    echo "Execute: Yes"
  else
    echo "Execute: No"
  fi
  if [ -r "$file_name" ];
  then
    echo "Read: Yes"
  else
    echo "Read: No"
  fi
  echo "The file extension is: '.${file_name##*.}'"
fi
