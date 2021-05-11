#! /bin/bash

IFS=$'\n'
modules=($(find . -name pom.xml | sed -E 's@/pom.xml@@g' | sed -E 's@^\./@@g' | grep -vE '^\.$' | sort))
count=${#modules[@]}
partition=$(expr $count / 4 + 1)

json=$'{\n'
json+=$'    "include": [\n'
json+=$'    {\n'
i=0
partition_index=1
for module in ${modules[@]}; do
    if [ $i -gt $partition ]; then
        json+=$'"\n    },\n    {\n'
        i=0
    fi
    if [ $i -eq 0 ]; then
        json+="        \"category\": \"Native Tests - $partition_index\","
        json+=$'\n        "test-modules": "'
        ((partition_index=partition_index+1))
        i=0
    fi
    json+="$module,"
    ((i=i+1))
done
json+=$'"\n    }\n    ]\n}\n'

echo "$json"
