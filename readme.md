http - run Server.scala

curl --location 'http://localhost:8080/similarity' \
--header 'Content-Type: application/json' \
--form 'file1=@"/path/to/file"' \
--form 'file2=@"/path/to/file"'

console:
run SimilarityCheck